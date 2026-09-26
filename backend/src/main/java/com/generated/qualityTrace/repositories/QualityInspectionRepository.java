package com.generated.qualityTrace.repositories;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import com.generated.qualityTrace.models.QualityInspection;

@Repository
public class QualityInspectionRepository {
  private final Map<Long, QualityInspection> store = new ConcurrentHashMap<>();
  private final AtomicLong seq = new AtomicLong(0);

  public List<QualityInspection> findAll() { return List.copyOf(store.values()); }

  public Optional<QualityInspection> findById(Long id) { return Optional.ofNullable(store.get(id)); }

  public Optional<QualityInspection> findLatestByBatchId(Long batchId) {
    return store.values().stream()
        .filter(i -> Objects.equals(i.batchId, batchId))
        .max(Comparator.comparing(i -> i.id));
  }

  public QualityInspection save(QualityInspection inspection) {
    if (inspection.id == null) inspection.id = seq.incrementAndGet();
    store.put(inspection.id, inspection);
    return inspection;
  }
}
