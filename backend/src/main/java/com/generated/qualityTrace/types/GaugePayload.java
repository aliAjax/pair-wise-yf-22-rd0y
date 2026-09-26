package com.generated.qualityTrace.types;

/** 量具重新校准入参：新的校准到期日，可选恢复/保持停用 */
public record GaugePayload(String calibrationDueAt, Boolean disabled) {}
