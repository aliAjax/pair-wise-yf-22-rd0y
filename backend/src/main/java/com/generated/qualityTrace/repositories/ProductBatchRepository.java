package com.generated.qualityTrace.repositories;
import java.util.*; import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.generated.qualityTrace.constants.BatchReleaseStatus;
import com.generated.qualityTrace.models.ProductBatch;
@Repository
public class ProductBatchRepository {
  private final Map<String, ProductBatch> store = new ConcurrentHashMap<>();
  public ProductBatchRepository() {
    save(new ProductBatch(1L, "B2026-001", BatchReleaseStatus.WAITING));
    save(new ProductBatch(2L, "B2026-002", BatchReleaseStatus.WAITING));
  }
  public ProductBatch findByBatchNo(String batchNo) { return batchNo == null ? null : store.get(batchNo); }
  public ProductBatch save(ProductBatch b) { store.put(b.batchNo, b); return b; }
  public List<Map<String, Object>> findAll() {
    List<Map<String, Object>> out = new ArrayList<>();
    for (ProductBatch b : store.values()) out.add(Map.of("id", b.id, "batchNo", b.batchNo, "batchStatus", b.batchStatus));
    return out;
  }
}
