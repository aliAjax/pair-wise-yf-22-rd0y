package com.generated.qualityTrace.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.generated.qualityTrace.models.Gauge;

@Repository
public class GaugeRepository {
  private final Map<String, Gauge> store = new ConcurrentHashMap<>();

  public GaugeRepository() {
    save(new Gauge(1L, "GAUGE-001", "数显卡尺", LocalDate.of(2026, 1, 10), LocalDate.of(2027, 1, 10), false));
    save(new Gauge(2L, "GAUGE-002", "千分尺", LocalDate.of(2024, 6, 1), LocalDate.of(2025, 6, 1), false));
    save(new Gauge(3L, "GAUGE-003", "高度规", LocalDate.of(2026, 3, 1), LocalDate.of(2027, 3, 1), true));
  }

  public List<Gauge> findAll() { return List.copyOf(store.values()); }

  public Optional<Gauge> findByCode(String gaugeCode) { return Optional.ofNullable(store.get(gaugeCode)); }

  public Gauge save(Gauge gauge) { store.put(gauge.gaugeCode, gauge); return gauge; }
}
