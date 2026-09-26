# 制造业质量追溯 API 服务

面向小型制造工厂的批次质量追溯后端服务，覆盖工单、批次、检验项、不良记录和追溯查询。

## 快速启动

```bash
cp .env.example .env && docker compose up -d
```

## 访问地址或 CLI 示例

后端健康检查：<http://localhost:21114/health>

### 量具校验 + 终检放行流程

终检提交时会按量具编号检查校准日期和停用状态：

- 量具正常：报告合格（PASS / ACCEPTED），批次直接放行（RELEASED）。
- 量具校准过期或已停用：报告保留为待复核（RECHECK / PENDING_RECHECK），批次继续等待（WAITING_INSPECTION）。
- 量具重新校准后，用同一批次号再次提交，原报告转为复核通过（PASS / CONFIRMED_AFTER_RECHECK），批次放行。

```bash
# 查看量具及校准状态（种子：GAUGE-001 正常 / GAUGE-002 过期 / GAUGE-003 停用）
curl http://localhost:21114/api/gauge
curl http://localhost:21114/api/gauge/GAUGE-002/status

# 提交终检（过期量具 -> 待复核，批次继续等待）
curl -X POST http://localhost:21114/api/quality-inspection/final \
  -H 'Content-Type: application/json' \
  -d '{"batchNo":"BATCH-1001","inspectorId":"INSP-01","gaugeCode":"GAUGE-002"}'

# 量具重新校准
curl -X POST http://localhost:21114/api/gauge/GAUGE-002/recalibrate \
  -H 'Content-Type: application/json' \
  -d '{"calibrationDueAt":"2027-06-30"}'

# 原报告再次提交 -> 复核通过并放行；详情返回量具状态、报告处理、批次放行结果
curl -X POST http://localhost:21114/api/quality-inspection/final \
  -H 'Content-Type: application/json' \
  -d '{"batchNo":"BATCH-1001","inspectorId":"INSP-01","gaugeCode":"GAUGE-002"}'
curl http://localhost:21114/api/quality-inspection/1
```

## 本地开发方式

- 后端：进入 `backend` 后按技术栈运行开发命令，接口统一挂在 `/api`。

## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | - |
| 后端 | Spring Boot 3 + Java 17 + MyBatis-Plus |
| 数据库 | PostgreSQL 15 |
| 部署 | Docker Compose |

## 项目目录结构

```text
backend/src/routes, controllers, services, models, repositories, middlewares, constants, constructors, validators, utils, types, config
```

## 环境变量说明

- `COMPOSE_PROJECT_NAME`: Compose 项目名，默认 `quality-trace`
- `BACKEND_PORT`: 后端端口，默认 `21114`
- `DB_PORT`: 数据库宿主机端口
- `DB_USER/DB_PASSWORD/DB_NAME`: 本地数据库凭据

## Docker 部署说明

- 根 Compose 文件不写 `version`，顶层 `name: quality-trace`。
- 容器名均使用 `${COMPOSE_PROJECT_NAME:-quality-trace}` 前缀。
- 数据库使用命名卷，避免绑定中文路径。
- 常见问题：端口占用时修改 `.env` 中端口后重启；需要重置数据时执行 `docker compose down -v`。

## 枚举/常量出现位置清单

- WorkOrderStatus: constants/WorkOrderStatus、types/WorkOrderStatus、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
- InspectionResultStatus: constants/InspectionResultStatus（PASS/FAIL/CONDITIONAL_PASS/RECHECK）、services/QualityInspectionService（终检判定与待复核）、constructors/QualityInspectionDtoFactory、database/init.sql。
- DefectSeverity: constants/DefectSeverity、types/DefectSeverity、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
- GaugeCalibrationStatus: constants/GaugeCalibrationStatus（NORMAL/EXPIRED/DISABLED）、types/GaugeCheckResult、services/GaugeService、constructors/GaugeDtoFactory、utils/Formatters、constants/LogTemplates。
- ReportHandlingStatus: constants/ReportHandlingStatus（ACCEPTED/PENDING_RECHECK/CONFIRMED_AFTER_RECHECK）、services/QualityInspectionService、constructors/QualityInspectionDtoFactory、utils/Formatters。
- BatchStatus: constants/BatchStatus（WAITING_INSPECTION/RELEASED/ON_HOLD）、services/QualityInspectionService、repositories/ProductBatchRepository、constructors/ProductBatchDtoFactory、utils/Formatters、database/init.sql。

## 为什么会牵一发动全身

实体字段、枚举、日志模板、错误消息、构造器、筛选器和展示组件被刻意拆散到多个目录；修改一个状态值通常需要同步类型、构造器、服务、控制器、store、页面、README 与数据库种子。量具校验接入终检后，量具状态同时影响检验报告处理（ReportHandlingStatus）与批次放行（BatchStatus），改动会贯穿 constants、types、models、repositories、services、controllers、constructors、utils、validators 与 init.sql。

## License

MIT
