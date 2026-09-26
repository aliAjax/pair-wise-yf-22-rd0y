package com.generated.qualityTrace.constructors;

import java.util.LinkedHashMap;
import java.util.Map;
import com.generated.qualityTrace.models.ProductBatch;
import com.generated.qualityTrace.utils.Formatters;

public final class ProductBatchDtoFactory {
  public static Map<String, Object> toDto(ProductBatch batch) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", batch.id);
    dto.put("batchNo", batch.batchNo);
    dto.put("workOrderId", batch.workOrderId);
    dto.put("quantity", batch.quantity);
    dto.put("batchStatus", batch.batchStatus);
    dto.put("batchStatusText", Formatters.batchStatusText(batch.batchStatus));
    return dto;
  }
}
