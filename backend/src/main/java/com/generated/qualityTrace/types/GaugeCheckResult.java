package com.generated.qualityTrace.types;

import com.generated.qualityTrace.constants.GaugeCalibrationStatus;
import com.generated.qualityTrace.models.Gauge;

/** 量具校验结果：量具 + 校准状态 */
public record GaugeCheckResult(Gauge gauge, GaugeCalibrationStatus status) {
  public boolean usable() { return status == GaugeCalibrationStatus.NORMAL; }
}
