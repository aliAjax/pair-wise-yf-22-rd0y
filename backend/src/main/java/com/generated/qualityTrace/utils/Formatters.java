package com.generated.qualityTrace.utils;

import com.generated.qualityTrace.constants.BatchStatus;
import com.generated.qualityTrace.constants.GaugeCalibrationStatus;
import com.generated.qualityTrace.constants.ReportHandlingStatus;

public final class Formatters {
  public static String audit(String type, long id) { return type + "#" + id; }

  public static String gaugeStatusText(GaugeCalibrationStatus status) {
    return switch (status) {
      case NORMAL -> "正常";
      case EXPIRED -> "校准过期";
      case DISABLED -> "已停用";
    };
  }

  public static String reportHandlingText(ReportHandlingStatus status) {
    return switch (status) {
      case ACCEPTED -> "已受理";
      case PENDING_RECHECK -> "待复核";
      case CONFIRMED_AFTER_RECHECK -> "复核通过";
    };
  }

  public static String batchStatusText(String status) {
    if (BatchStatus.RELEASED.name().equals(status)) return "已放行";
    if (BatchStatus.ON_HOLD.name().equals(status)) return "冻结";
    return "等待检验";
  }
}
