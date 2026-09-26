package com.generated.qualityTrace.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.generated.qualityTrace.constants.ErrorCodes;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.constants.GaugeCalibrationStatus;
import com.generated.qualityTrace.constants.LogTemplates;
import com.generated.qualityTrace.constructors.GaugeDtoFactory;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.repositories.GaugeRepository;
import com.generated.qualityTrace.types.GaugeCheckResult;
import com.generated.qualityTrace.types.GaugePayload;
import com.generated.qualityTrace.utils.ServiceException;

@Service
public class GaugeService {
  private static final Logger log = LoggerFactory.getLogger(GaugeService.class);

  private final GaugeRepository repo;

  public GaugeService(GaugeRepository repo) { this.repo = repo; }

  public List<Map<String, Object>> list() {
    return repo.findAll().stream().map(g -> GaugeDtoFactory.toDto(g, statusOf(g))).toList();
  }

  /** 按量具编号检查校准日期和停用状态 */
  public GaugeCheckResult check(String gaugeCode) {
    Gauge gauge = repo.findByCode(gaugeCode)
        .orElseThrow(() -> new ServiceException(ErrorCodes.GAUGE_NOT_FOUND, ErrorMessages.GAUGE_NOT_FOUND));
    GaugeCalibrationStatus status = statusOf(gauge);
    if (status != GaugeCalibrationStatus.NORMAL) log.info(String.format(LogTemplates.GAUGE_BLOCKED, gaugeCode, status));
    return new GaugeCheckResult(gauge, status);
  }

  /** 重新校准：刷新校准日期，默认解除停用 */
  public Map<String, Object> recalibrate(String gaugeCode, GaugePayload payload) {
    Gauge gauge = repo.findByCode(gaugeCode)
        .orElseThrow(() -> new ServiceException(ErrorCodes.GAUGE_NOT_FOUND, ErrorMessages.GAUGE_NOT_FOUND));
    gauge.calibratedAt = LocalDate.now();
    if (payload != null && payload.calibrationDueAt() != null && !payload.calibrationDueAt().isBlank()) {
      gauge.calibrationDueAt = LocalDate.parse(payload.calibrationDueAt());
    } else {
      gauge.calibrationDueAt = LocalDate.now().plusYears(1);
    }
    gauge.disabled = payload != null && payload.disabled() != null ? payload.disabled() : false;
    repo.save(gauge);
    log.info(String.format(LogTemplates.GAUGE_RECALIBRATED, gauge.gaugeCode, gauge.calibrationDueAt, gauge.disabled));
    return GaugeDtoFactory.toDto(gauge, statusOf(gauge));
  }

  private GaugeCalibrationStatus statusOf(Gauge gauge) {
    if (gauge.disabled) return GaugeCalibrationStatus.DISABLED;
    if (gauge.calibrationDueAt != null && gauge.calibrationDueAt.isBefore(LocalDate.now())) {
      return GaugeCalibrationStatus.EXPIRED;
    }
    return GaugeCalibrationStatus.NORMAL;
  }
}
