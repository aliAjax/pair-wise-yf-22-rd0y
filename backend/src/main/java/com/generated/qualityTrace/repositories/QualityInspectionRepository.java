package com.generated.qualityTrace.repositories;
import java.util.*; import java.util.concurrent.ConcurrentHashMap; import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import com.generated.qualityTrace.constants.ReportHandling;
import com.generated.qualityTrace.models.QualityInspection;
@Repository
public class QualityInspectionRepository {
  private final Map<Long, QualityInspection> store = new ConcurrentHashMap<>();
  private final AtomicLong seq = new AtomicLong(0);
  public QualityInspection save(QualityInspection in) {
    if (in.id == null) in.id = seq.incrementAndGet();
    store.put(in.id, in);
    return in;
  }
  public Optional<QualityInspection> findById(Long id) { return Optional.ofNullable(store.get(id)); }
  public Optional<QualityInspection> findPendingByBatchNo(String batchNo) {
    return store.values().stream()
      .filter(r -> r.batchNo.equals(batchNo) && ReportHandling.PENDING_REVIEW.name().equals(r.reportHandling))
      .findFirst();
  }
  public List<Map<String, Object>> findAll() {
    List<Map<String, Object>> out = new ArrayList<>();
    for (QualityInspection r : store.values()) {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("id", r.id); m.put("batchNo", r.batchNo); m.put("gaugeNo", r.gaugeNo);
      m.put("resultStatus", r.resultStatus); m.put("reportHandling", r.reportHandling);
      out.add(m);
    }
    return out;
  }
}
