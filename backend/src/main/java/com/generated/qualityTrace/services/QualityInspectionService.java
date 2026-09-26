package com.generated.qualityTrace.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.generated.qualityTrace.constants.BatchStatus;
import com.generated.qualityTrace.constants.ErrorCodes;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.constants.InspectionResultStatus;
import com.generated.qualityTrace.constants.LogTemplates;
import com.generated.qualityTrace.constants.ReportHandlingStatus;
import com.generated.qualityTrace.constructors.QualityInspectionDtoFactory;
import com.generated.qualityTrace.models.ProductBatch;
import com.generated.qualityTrace.models.QualityInspection;
import com.generated.qualityTrace.repositories.ProductBatchRepository;
import com.generated.qualityTrace.repositories.QualityInspectionRepository;
import com.generated.qualityTrace.types.FinalInspectionSubmission;
import com.generated.qualityTrace.types.GaugeCheckResult;
import com.generated.qualityTrace.utils.ServiceException;
import com.generated.qualityTrace.validators.FinalInspectionValidator;

@Service
public class QualityInspectionService {
  private static final Logger log = LoggerFactory.getLogger(QualityInspectionService.class);
  private static final String DEFAULT_TYPE = "FINAL";

  private final QualityInspectionRepository repo;
  private final ProductBatchRepository batchRepo;
  private final GaugeService gaugeService;

  public QualityInspectionService(QualityInspectionRepository repo, ProductBatchRepository batchRepo, GaugeService gaugeService) {
    this.repo = repo;
    this.batchRepo = batchRepo;
    this.gaugeService = gaugeService;
  }

  public List<Map<String, Object>> list() {
    return repo.findAll().stream().map(QualityInspectionDtoFactory::toDto).toList();
  }

  /**
   * 终检提交：按量具编号检查校准日期和停用状态。
   * 过期/停用 -> 报告保留为待复核(RECHECK)，批次继续等待；
   * 正常 -> 合格结论并放行批次；量具重新校准后原报告再次提交可复核通过并放行。
   */
  public Map<String, Object> submitFinalInspection(FinalInspectionSubmission req) {
    FinalInspectionValidator.validate(req);
    ProductBatch batch = batchRepo.findByBatchNo(req.batchNo())
        .orElseThrow(() -> new ServiceException(ErrorCodes.BATCH_NOT_FOUND, ErrorMessages.BATCH_NOT_FOUND));
    GaugeCheckResult check = gaugeService.check(req.gaugeCode());
    String type = req.inspectionType() == null || req.inspectionType().isBlank() ? DEFAULT_TYPE : req.inspectionType();
    log.info(String.format(LogTemplates.INSPECTION_SUBMIT, batch.batchNo, req.gaugeCode(), type));

    // 同批次存在待复核报告时，在原报告上再次提交
    QualityInspection inspection = repo.findLatestByBatchId(batch.id)
        .filter(i -> InspectionResultStatus.RECHECK.name().equals(i.resultStatus))
        .orElseGet(QualityInspection::new);
    boolean resubmit = inspection.id != null;
    inspection.batchId = batch.id;
    inspection.inspectorId = req.inspectorId();
    inspection.inspectionType = type;
    inspection.gaugeCode = req.gaugeCode();
    inspection.inspectedAt = LocalDateTime.now().toString();

    if (check.usable()) {
      inspection.resultStatus = InspectionResultStatus.PASS.name();
      inspection.reportHandling = resubmit
          ? ReportHandlingStatus.CONFIRMED_AFTER_RECHECK.name()
          : ReportHandlingStatus.ACCEPTED.name();
    } else {
      inspection.resultStatus = InspectionResultStatus.RECHECK.name();
      inspection.reportHandling = ReportHandlingStatus.PENDING_RECHECK.name();
    }
    repo.save(inspection);

    if (check.usable()) {
      batch.batchStatus = BatchStatus.RELEASED.name();
      batchRepo.save(batch);
      log.info(String.format(LogTemplates.BATCH_RELEASED, batch.batchNo, inspection.id));
    } else {
      if (!BatchStatus.RELEASED.name().equals(batch.batchStatus)) {
        batch.batchStatus = BatchStatus.WAITING_INSPECTION.name();
        batchRepo.save(batch);
      }
      log.info(String.format(LogTemplates.REPORT_PENDING_RECHECK, inspection.id, batch.batchNo));
    }
    return detail(inspection.id);
  }

  /** 检验详情：量具状态、报告处理和批次放行结果 */
  public Map<String, Object> detail(long id) {
    QualityInspection inspection = repo.findById(id)
        .orElseThrow(() -> new ServiceException(ErrorCodes.INSPECTION_NOT_FOUND, ErrorMessages.INSPECTION_NOT_FOUND));
    GaugeCheckResult check = gaugeService.check(inspection.gaugeCode);
    ProductBatch batch = batchRepo.findById(inspection.batchId)
        .orElseThrow(() -> new ServiceException(ErrorCodes.BATCH_NOT_FOUND, ErrorMessages.BATCH_NOT_FOUND));
    return QualityInspectionDtoFactory.detail(inspection, check, batch);
  }
}
