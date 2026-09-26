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
  result_status TEXT,
  gauge_no TEXT,
  inspected_at TEXT
);

CREATE TABLE IF NOT EXISTS gauge (
  id INTEGER PRIMARY KEY,
  gauge_no TEXT,
  gauge_name TEXT,
  calibration_due_date TEXT,
  status TEXT
);

INSERT INTO gauge (id, gauge_no, gauge_name, calibration_due_date, status) VALUES
  (1, 'GA-001', '数显卡尺', '2027-01-31', 'ACTIVE'),
  (2, 'GA-002', '千分尺', '2025-12-31', 'ACTIVE'),
  (3, 'GA-003', '高度规', '2027-06-30', 'DISABLED')
ON CONFLICT (id) DO NOTHING;

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

CREATE TABLE IF NOT EXISTS audit_log (
  id INTEGER PRIMARY KEY,
  actor TEXT,
  action TEXT,
  target_type TEXT,
  target_id TEXT,
  created_at TEXT
);
