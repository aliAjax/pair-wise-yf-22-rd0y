package com.generated.qualityTrace.models;

import java.time.LocalDate;

/** 量具：按 gaugeCode 校验校准有效期与停用状态 */
public class Gauge {
  public Long id;
  public String gaugeCode;
  public String gaugeName;
  public LocalDate calibratedAt;
  public LocalDate calibrationDueAt;
  public boolean disabled;

  public Gauge() {}

  public Gauge(Long id, String gaugeCode, String gaugeName, LocalDate calibratedAt, LocalDate calibrationDueAt, boolean disabled) {
    this.id = id;
    this.gaugeCode = gaugeCode;
    this.gaugeName = gaugeName;
    this.calibratedAt = calibratedAt;
    this.calibrationDueAt = calibrationDueAt;
    this.disabled = disabled;
  }
}
