package com.generated.qualityTrace.services;
import java.util.*;
import org.springframework.stereotype.Service;
import com.generated.qualityTrace.constants.BatchReleaseStatus;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.constants.LogTemplates;
import com.generated.qualityTrace.models.ProductBatch;
import com.generated.qualityTrace.repositories.ProductBatchRepository;
import com.generated.qualityTrace.utils.Formatters;
@Service
public class ProductBatchService {
  private final ProductBatchRepository repo;
  public ProductBatchService(ProductBatchRepository repo){this.repo=repo;}
  public List<Map<String,Object>> list(){return repo.findAll();}

  public ProductBatch requireBatch(String batchNo) {
    ProductBatch b = repo.findByBatchNo(batchNo);
    if (b == null) throw new NoSuchElementException(ErrorMessages.BATCH_NOT_FOUND);
    return b;
  }

  public void release(ProductBatch b) {
    b.batchStatus = BatchReleaseStatus.RELEASED;
    repo.save(b);
    System.out.println(LogTemplates.BATCH_RELEASE + " " + Formatters.audit("batch", b.id) + " " + b.batchNo);
  }

  public void hold(ProductBatch b) {
    if (BatchReleaseStatus.RELEASED.equals(b.batchStatus)) return;
    b.batchStatus = BatchReleaseStatus.WAITING;
    repo.save(b);
    System.out.println(LogTemplates.STATUS + " " + Formatters.audit("batch", b.id) + " " + BatchReleaseStatus.WAITING);
  }
}
