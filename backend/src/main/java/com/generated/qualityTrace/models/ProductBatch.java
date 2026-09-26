package com.generated.qualityTrace.models;

public class ProductBatch {
  public Long id;
  public String batchNo;
  public String workOrderId;
  public Integer quantity;
  public String batchStatus;

  public ProductBatch() {}

  public ProductBatch(Long id, String batchNo, String workOrderId, Integer quantity, String batchStatus) {
    this.id = id;
    this.batchNo = batchNo;
    this.workOrderId = workOrderId;
    this.quantity = quantity;
    this.batchStatus = batchStatus;
  }
}
