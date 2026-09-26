package com.generated.qualityTrace.utils;
import com.generated.qualityTrace.constants.GaugeCheckResult;
public final class Formatters {
  public static String audit(String type, long id){ return type + "#" + id; }
  public static String gaugeStatusText(GaugeCheckResult r) {
    return switch (r) { case VALID -> "量具正常"; case EXPIRED -> "校准过期"; case DISABLED -> "已停用"; };
  }
}
