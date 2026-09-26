package com.generated.qualityTrace.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.generated.qualityTrace.constants.BatchStatus;
import com.generated.qualityTrace.models.ProductBatch;

@Repository
public class ProductBatchRepository {
  private final Map<Long, ProductBatch> store = new ConcurrentHashMap<>();

  public ProductBatchRepository() {
    save(new ProductBatch(1L, "BATCH-1001", "WO-1", 500, BatchStatus.WAITING_INSPECTION.name()));
    save(new ProductBatch(2L, "BATCH-1002", "WO-1", 300, BatchStatus.WAITING_INSPECTION.name()));
  }

  public List<ProductBatch> findAll() { return List.copyOf(store.values()); }

  public Optional<ProductBatch> findById(Long id) { return Optional.ofNullable(store.get(id)); }

  public Optional<ProductBatch> findByBatchNo(String batchNo) {
    return store.values().stream().filter(b -> b.batchNo.equals(batchNo)).findFirst();
  }

  public ProductBatch save(ProductBatch batch) { store.put(batch.id, batch); return batch; }
}
