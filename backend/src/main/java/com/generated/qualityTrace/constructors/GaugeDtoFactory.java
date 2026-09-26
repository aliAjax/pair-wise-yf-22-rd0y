package com.generated.qualityTrace.constructors;
import java.util.*;
import com.generated.qualityTrace.constants.GaugeCheckResult;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.utils.Formatters;
public final class GaugeDtoFactory {
  public static Map<String, Object> view(Gauge g, GaugeCheckResult effective) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("id", g.id);
    m.put("gaugeNo", g.gaugeNo);
    m.put("gaugeName", g.gaugeName);
    m.put("calibrationDueDate", g.calibrationDueDate);
    m.put("status", g.status);
    m.put("gaugeStatus", effective.name());
    m.put("gaugeStatusText", Formatters.gaugeStatusText(effective));
    return m;
  }
}
