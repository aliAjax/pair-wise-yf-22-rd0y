package com.generated.qualityTrace.repositories;
import java.util.*; import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.generated.qualityTrace.constants.GaugeStatus;
import com.generated.qualityTrace.models.Gauge;
@Repository
public class GaugeRepository {
  private final Map<String, Gauge> store = new ConcurrentHashMap<>();
  public GaugeRepository() {
    save(new Gauge(1L, "GA-001", "数显卡尺", "2027-01-31", GaugeStatus.ACTIVE.name()));
    save(new Gauge(2L, "GA-002", "千分尺", "2025-12-31", GaugeStatus.ACTIVE.name()));
    save(new Gauge(3L, "GA-003", "高度规", "2027-06-30", GaugeStatus.DISABLED.name()));
  }
  public Gauge findByGaugeNo(String gaugeNo) { return gaugeNo == null ? null : store.get(gaugeNo); }
  public Gauge save(Gauge g) { store.put(g.gaugeNo, g); return g; }
  public List<Gauge> findAll() { return List.copyOf(store.values()); }
}
