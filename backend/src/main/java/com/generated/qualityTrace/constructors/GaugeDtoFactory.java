package com.generated.qualityTrace.constructors;

import java.util.LinkedHashMap;
import java.util.Map;
import com.generated.qualityTrace.constants.GaugeCalibrationStatus;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.utils.Formatters;

public final class GaugeDtoFactory {
  public static Map<String, Object> toDto(Gauge gauge, GaugeCalibrationStatus status) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", gauge.id);
    dto.put("gaugeCode", gauge.gaugeCode);
    dto.put("gaugeName", gauge.gaugeName);
    dto.put("calibratedAt", gauge.calibratedAt == null ? null : gauge.calibratedAt.toString());
    dto.put("calibrationDueAt", gauge.calibrationDueAt == null ? null : gauge.calibrationDueAt.toString());
    dto.put("disabled", gauge.disabled);
    dto.put("calibrationStatus", status.name());
    dto.put("calibrationStatusText", Formatters.gaugeStatusText(status));
    return dto;
  }
}
