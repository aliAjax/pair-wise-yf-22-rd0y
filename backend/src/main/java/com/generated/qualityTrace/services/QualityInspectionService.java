package com.generated.qualityTrace.services;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.constants.GaugeCheckResult;
import com.generated.qualityTrace.constants.InspectionResultStatus;
import com.generated.qualityTrace.constants.LogTemplates;
import com.generated.qualityTrace.constants.ReportHandling;
import com.generated.qualityTrace.constructors.QualityInspectionDtoFactory;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.models.ProductBatch;
import com.generated.qualityTrace.models.QualityInspection;
import com.generated.qualityTrace.repositories.QualityInspectionRepository;
import com.generated.qualityTrace.types.QualityInspectionPayload;
import com.generated.qualityTrace.utils.Formatters;
@Service
public class QualityInspectionService {
  private final QualityInspectionRepository repo;
  private final GaugeService gaugeService;
  private final ProductBatchService batchService;
  public QualityInspectionService(QualityInspectionRepository repo, GaugeService gaugeService, ProductBatchService batchService) {
    this.repo = repo; this.gaugeService = gaugeService; this.batchService = batchService;
  }
  public List<Map<String,Object>> list(){return repo.findAll();}

  public Map<String, Object> submit(QualityInspectionPayload p) {
    Gauge gauge = gaugeService.requireGauge(p.gaugeNo());
    GaugeCheckResult check = gaugeService.evaluate(gauge);
    ProductBatch batch = batchService.requireBatch(p.batchNo());

    QualityInspection report = repo.findPendingByBatchNo(p.batchNo()).orElseGet(QualityInspection::new);
    report.batchNo = p.batchNo();
    report.inspectorId = p.inspectorId();
    report.inspectionType = p.inspectionType();
    report.gaugeNo = p.gaugeNo();
    report.inspectedAt = LocalDateTime.now().toString();

    if (check == GaugeCheckResult.VALID) {
      report.resultStatus = InspectionResultStatus.PASS.name();
      report.reportHandling = ReportHandling.ACCEPTED.name();
      batchService.release(batch);
    } else {
      report.resultStatus = InspectionResultStatus.RECHECK.name();
      report.reportHandling = ReportHandling.PENDING_REVIEW.name();
      batchService.hold(batch);
    }
    repo.save(report);
    System.out.println(LogTemplates.INSPECTION_SUBMIT + " " + Formatters.audit("inspection", report.id)
      + " gauge=" + check + " handling=" + report.reportHandling);
    return QualityInspectionDtoFactory.submission(report, gauge, check, batch);
  }

  public Map<String, Object> detail(Long id) {
    QualityInspection report = repo.findById(id)
      .orElseThrow(() -> new NoSuchElementException(ErrorMessages.INSPECTION_NOT_FOUND));
    Gauge gauge = gaugeService.requireGauge(report.gaugeNo);
    GaugeCheckResult check = gaugeService.evaluate(gauge);
    ProductBatch batch = batchService.requireBatch(report.batchNo);
    return QualityInspectionDtoFactory.detail(report, gauge, check, batch);
  }
}
