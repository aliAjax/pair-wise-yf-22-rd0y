package com.generated.qualityTrace.services;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.constants.GaugeCheckResult;
import com.generated.qualityTrace.constants.GaugeStatus;
import com.generated.qualityTrace.constants.LogTemplates;
import com.generated.qualityTrace.constructors.GaugeDtoFactory;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.repositories.GaugeRepository;
import com.generated.qualityTrace.utils.Formatters;
@Service
public class GaugeService {
  private final GaugeRepository repo;
  public GaugeService(GaugeRepository repo) { this.repo = repo; }

  public Gauge requireGauge(String gaugeNo) {
    Gauge g = repo.findByGaugeNo(gaugeNo);
    if (g == null) throw new NoSuchElementException(ErrorMessages.GAUGE_NOT_FOUND);
    return g;
  }

  public GaugeCheckResult evaluate(Gauge g) {
    System.out.println(LogTemplates.GAUGE_CHECK + " " + Formatters.audit("gauge", g.id) + " " + g.gaugeNo);
    if (GaugeStatus.DISABLED.name().equals(g.status)) return GaugeCheckResult.DISABLED;
    if (LocalDate.parse(g.calibrationDueDate).isBefore(LocalDate.now())) return GaugeCheckResult.EXPIRED;
    return GaugeCheckResult.VALID;
  }

  public Gauge recalibrate(String gaugeNo, String calibrationDueDate) {
    Gauge g = requireGauge(gaugeNo);
    try {
      LocalDate.parse(calibrationDueDate);
    } catch (Exception e) {
      throw new IllegalArgumentException(ErrorMessages.INVALID_CALIBRATION_DATE);
    }
    g.calibrationDueDate = calibrationDueDate;
    g.status = GaugeStatus.ACTIVE.name();
    repo.save(g);
    System.out.println(LogTemplates.GAUGE_RECALIBRATE + " " + Formatters.audit("gauge", g.id) + " " + calibrationDueDate);
    return g;
  }

  public Gauge disable(String gaugeNo) {
    Gauge g = requireGauge(gaugeNo);
    g.status = GaugeStatus.DISABLED.name();
    repo.save(g);
    System.out.println(LogTemplates.STATUS + " " + Formatters.audit("gauge", g.id) + " " + GaugeStatus.DISABLED);
    return g;
  }

  public List<Map<String, Object>> list() {
    List<Map<String, Object>> out = new ArrayList<>();
    for (Gauge g : repo.findAll()) out.add(GaugeDtoFactory.view(g, evaluate(g)));
    return out;
  }
}
