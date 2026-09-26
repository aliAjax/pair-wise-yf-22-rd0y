package com.generated.qualityTrace.constructors;
import java.util.*;
import com.generated.qualityTrace.constants.BatchReleaseStatus;
import com.generated.qualityTrace.constants.GaugeCheckResult;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.models.ProductBatch;
import com.generated.qualityTrace.models.QualityInspection;
import com.generated.qualityTrace.utils.Formatters;
public final class QualityInspectionDtoFactory {
  public static Map<String,Object> create(){ return Map.of("id",1,"name","质量检验"); }

  public static Map<String, Object> submission(QualityInspection r, Gauge g, GaugeCheckResult check, ProductBatch b) {
    Map<String, Object> m = detail(r, g, check, b);
    m.put("message", check == GaugeCheckResult.VALID ? "报告已受理，批次放行" : "量具" + Formatters.gaugeStatusText(check) + "，报告待复核，批次继续等待");
    return m;
  }

  public static Map<String, Object> detail(QualityInspection r, Gauge g, GaugeCheckResult check, ProductBatch b) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("inspectionId", r.id);
    m.put("batchNo", r.batchNo);
    m.put("inspectorId", r.inspectorId);
    m.put("inspectionType", r.inspectionType);
    m.put("inspectedAt", r.inspectedAt);
    m.put("gaugeNo", g.gaugeNo);
    m.put("gaugeStatus", check.name());
    m.put("gaugeStatusText", Formatters.gaugeStatusText(check));
    m.put("calibrationDueDate", g.calibrationDueDate);
    m.put("resultStatus", r.resultStatus);
    m.put("reportHandling", r.reportHandling);
    m.put("batchRelease", Map.of(
      "released", BatchReleaseStatus.RELEASED.equals(b.batchStatus),
      "batchStatus", b.batchStatus));
    return m;
  }
}
