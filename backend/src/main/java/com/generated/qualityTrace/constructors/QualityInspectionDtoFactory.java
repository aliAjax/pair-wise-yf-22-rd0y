package com.generated.qualityTrace.constructors;

import java.util.LinkedHashMap;
import java.util.Map;
import com.generated.qualityTrace.constants.BatchStatus;
import com.generated.qualityTrace.constants.ReportHandlingStatus;
import com.generated.qualityTrace.models.ProductBatch;
import com.generated.qualityTrace.models.QualityInspection;
import com.generated.qualityTrace.types.GaugeCheckResult;
import com.generated.qualityTrace.utils.Formatters;

public final class QualityInspectionDtoFactory {
  public static Map<String, Object> toDto(QualityInspection inspection) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", inspection.id);
    dto.put("batchId", inspection.batchId);
    dto.put("inspectorId", inspection.inspectorId);
    dto.put("inspectionType", inspection.inspectionType);
    dto.put("gaugeCode", inspection.gaugeCode);
    dto.put("resultStatus", inspection.resultStatus);
    dto.put("reportHandling", inspection.reportHandling);
    dto.put("inspectedAt", inspection.inspectedAt);
    return dto;
  }

  /** 检验详情：量具状态 + 报告处理 + 批次放行结果 */
  public static Map<String, Object> detail(QualityInspection inspection, GaugeCheckResult check, ProductBatch batch) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("inspection", toDto(inspection));
    dto.put("gaugeStatus", GaugeDtoFactory.toDto(check.gauge(), check.status()));
    dto.put("reportHandling", inspection.reportHandling);
    dto.put("reportHandlingText", Formatters.reportHandlingText(ReportHandlingStatus.valueOf(inspection.reportHandling)));
    boolean released = BatchStatus.RELEASED.name().equals(batch.batchStatus);
    Map<String, Object> batchRelease = new LinkedHashMap<>();
    batchRelease.put("batchNo", batch.batchNo);
    batchRelease.put("batchStatus", batch.batchStatus);
    batchRelease.put("batchStatusText", Formatters.batchStatusText(batch.batchStatus));
    batchRelease.put("released", released);
    dto.put("batchRelease", batchRelease);
    return dto;
  }
}
