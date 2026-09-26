CREATE TABLE IF NOT EXISTS work_order (
  id INTEGER PRIMARY KEY,
  order_no TEXT,
  product_code TEXT,
  product_name TEXT,
  planned_qty TEXT,
  line_code TEXT,
  start_at TEXT,
  status TEXT
);

CREATE TABLE IF NOT EXISTS product_batch (
  id INTEGER PRIMARY KEY,
  batch_no TEXT,
  work_order_id TEXT,
  quantity TEXT,
  material_lot_no TEXT,
  produced_at TEXT,
  batch_status TEXT
);

CREATE TABLE IF NOT EXISTS quality_inspection (
  id INTEGER PRIMARY KEY,
  batch_id TEXT,
  inspector_id TEXT,
  inspection_type TEXT,
  standard_version TEXT,
  gauge_code TEXT,
  result_status TEXT,
  report_handling TEXT,
  inspected_at TEXT
);

CREATE TABLE IF NOT EXISTS inspection_item_result (
  id INTEGER PRIMARY KEY,
  inspection_id TEXT,
  item_code TEXT,
  item_name TEXT,
  measured_value TEXT,
  limit_min TEXT,
  limit_max TEXT,
  item_status TEXT
);

CREATE TABLE IF NOT EXISTS defect_record (
  id INTEGER PRIMARY KEY,
  batch_id TEXT,
  defect_type TEXT,
  defect_qty TEXT,
  severity TEXT,
  root_cause TEXT,
  disposition_status TEXT
);

CREATE TABLE IF NOT EXISTS gauge (
  id INTEGER PRIMARY KEY,
  gauge_code TEXT,
  gauge_name TEXT,
  calibrated_at TEXT,
  calibration_due_at TEXT,
  disabled TEXT
);

CREATE TABLE IF NOT EXISTS audit_log (
  id INTEGER PRIMARY KEY,
  actor TEXT,
  action TEXT,
  target_type TEXT,
  target_id TEXT,
  created_at TEXT
);

-- 量具种子数据：GAUGE-001 正常 / GAUGE-002 校准过期 / GAUGE-003 已停用
INSERT INTO gauge (id, gauge_code, gauge_name, calibrated_at, calibration_due_at, disabled) VALUES
  (1, 'GAUGE-001', '数显卡尺', '2026-01-10', '2027-01-10', 'false'),
  (2, 'GAUGE-002', '千分尺', '2024-06-01', '2025-06-01', 'false'),
  (3, 'GAUGE-003', '高度规', '2026-03-01', '2027-03-01', 'true');

-- 批次种子数据：等待终检放行
INSERT INTO product_batch (id, batch_no, work_order_id, quantity, material_lot_no, produced_at, batch_status) VALUES
  (1, 'BATCH-1001', 'WO-1', '500', 'MAT-01', '2026-09-20', 'WAITING_INSPECTION'),
  (2, 'BATCH-1002', 'WO-1', '300', 'MAT-02', '2026-09-21', 'WAITING_INSPECTION');
