# 制造业质量追溯 API 服务

面向小型制造工厂的批次质量追溯后端服务，覆盖工单、批次、检验项、不良记录和追溯查询。

## 快速启动

```bash
cp .env.example .env && docker compose up -d
```

## 访问地址或 CLI 示例

后端健康检查：<http://localhost:21114/health>

后端健康检查：<http://localhost:21114/health>


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

backend/src/routes, controllers, services, models, repositories, middlewares, constants, constructors, utils, types, config
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
- InspectionResultStatus: constants/InspectionResultStatus、types/InspectionResultStatus、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
- DefectSeverity: constants/DefectSeverity、types/DefectSeverity、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
- GaugeStatus / GaugeCheckResult / ReportHandling / BatchReleaseStatus: constants 下独立文件，被 GaugeService、QualityInspectionService、ProductBatchService、GaugeDtoFactory、QualityInspectionDtoFactory、Formatters 共同引用。

## 量具校验与终检联动

- `POST /api/quality-inspection/submit`：提交终检结果时按 `gaugeNo` 检查量具校准日期与停用状态。量具正常则报告 `PASS` 并放行批次；校准过期（`EXPIRED`）或已停用（`DISABLED`）时报告保留为 `RECHECK` / `PENDING_REVIEW`，批次保持 `WAITING`。
- 同一批次再次提交会复用待复核报告：量具重新校准后再次提交即可得出合格结论并放行批次。
- `GET /api/quality-inspection/{id}`：检验详情返回量具状态（`gaugeStatus`/`gaugeStatusText`）、报告处理（`reportHandling`）和批次放行结果（`batchRelease`）。
- `POST /api/gauge/{gaugeNo}/recalibrate`：量具重新校准（入参 `calibrationDueDate`，格式 `yyyy-MM-dd`），同时恢复为 `ACTIVE`。
- `POST /api/gauge/{gaugeNo}/disable`：停用量具；`GET /api/gauge` 查看全部量具当前状态。
- 种子量具：`GA-001` 正常、`GA-002` 校准过期、`GA-003` 已停用；种子批次：`B2026-001`、`B2026-002`（均为 `WAITING`）。

## 为什么会牵一发动全身

实体字段、枚举、日志模板、错误消息、构造器、筛选器和展示组件被刻意拆散到多个目录；修改一个状态值通常需要同步类型、构造器、服务、控制器、store、页面、README 与数据库种子。

## License

MIT
