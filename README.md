# Tools Hub (Spring Boot + Vue)

一个可扩展的小工具集合，单服务部署（后端托管前端静态资源），适合 Zeabur 免费额度下长期运行。

## 1. 技术选型与成本策略
- **后端**: Spring Boot 3 + Spring Security + JPA + Flyway + PostgreSQL。
- **前端**: Vue 3 + Vite + TailwindCSS（轻量，HeroUI 风格留白和卡片化）。
- **部署策略**: 单个 Docker 服务（前端打包后由 Spring 静态托管），只需再挂一个 Zeabur PostgreSQL，降低维护与资源成本。
- **低资源默认值**:
  - Hikari 连接池最大 5 (`DB_POOL_MAX=5`)
  - JVM `-XX:MaxRAMPercentage=75 -XX:+UseSerialGC`
  - 无 Redis/消息队列等额外付费依赖

## 2. 功能概览
- 登录后进入 Dashboard，按注册表自动显示工具。
- 工具 1: Password Vault Lite（AES-GCM 加密存储密码）。
- 工具 2: Todo Memo（完成项删除线，未完成优先）。
- 无注册：仅管理员可创建用户。
- 固定超级管理员：`test / test123456`（启动时自动确保存在）。

## 3. 安全设计
- 密码哈希：BCrypt。
- 登录认证：JWT 放 `HttpOnly` Cookie (`AUTH_TOKEN`)。
- 最小 CSRF 防护：双提交 Token（`XSRF-TOKEN` cookie + `X-CSRF-Token` header）。
- 登录失败限制：内存级轻量限流（同用户名+IP 连续失败锁定 15 分钟）。
- 密码库加密：`VAULT_MASTER_KEY`（至少 32 字符）用于 AES-GCM；每条记录随机 IV。

> 威胁模型：数据库泄露时攻击者无法直接解密 vault 密码；若应用环境变量（主密钥）也泄露则可能被解密。

## 4. API 路由
- `/api/auth/*`: 登录、登出、当前用户、改密码、token 重置完成。
- `/api/admin/*`: 用户管理（仅 ADMIN）。
- `/api/tools/vault/*`: 密码库 CRUD。
- `/api/tools/todo/*`: 待办 CRUD。
- `/api/tools`: 工具注册表列表。

## 5. 数据库迁移
Flyway 脚本在：`backend/src/main/resources/db/migration/V1__init.sql`

表：
- `users`
- `vault_entries`
- `todos`
- `password_reset_tokens`

都包含 `user_id` 隔离外键与常用索引。

## 6. Zeabur 部署步骤（推荐）
1. 新建项目并连接本仓库。
2. 添加 **PostgreSQL** 服务。
3. 添加本仓库作为服务（使用根目录 `Dockerfile` 构建）。
4. 配置环境变量：

```env
PORT=8080
DB_URL=jdbc:postgresql://<pg-host>:5432/<db>
DB_USERNAME=<db-user>
DB_PASSWORD=<db-password>
DB_POOL_MAX=5
DB_POOL_MIN=1
JWT_SECRET=<至少32字符随机串>
JWT_EXP_MINUTES=720
COOKIE_SECURE=true
VAULT_MASTER_KEY=<至少32字符随机串>
RESET_TOKEN_EXP_MINUTES=30
JAVA_OPTS=-XX:MaxRAMPercentage=75 -XX:+UseSerialGC
```

5. 健康检查路径：`/actuator/health`
6. 部署完成后访问域名，使用 `test / test123456` 登录。

## 7. 本地开发（可选）
- 前端：`cd frontend && npm i && npm run dev`
- 后端：`cd backend && mvn spring-boot:run`

## 8. 管理员操作流程
1. 登录管理员。
2. Dashboard -> `Admin User Manager`。
3. 可创建用户、禁用/启用、重置临时密码、生成一次性 reset token。
4. 普通用户可在右上角“修改密码”自行更改密码。
5. 若管理员生成 token，用户可在登录页进入“重置 Token”页面完成重置。

## 9. 扩展第三个工具（脚手架示例）
1. 后端新增模块：
   - `backend/src/main/java/com/varios/toolshub/tools/<newtool>/`
   - `Controller + Service + Entity + Repository`
   - Flyway 增加 `V2__newtool.sql`
   - 路由前缀：`/api/tools/<newtool>`
2. 在 `ToolRegistryService` 增加一条 `ToolDefinition`。
3. 前端新增页面：`frontend/src/pages/NewToolPage.vue`。
4. 前端路由增加 `/tools/<newtool>`。
5. Dashboard 将自动显示注册表返回的工具卡片。

## 10. 默认账号
- username: `test`
- password: `test123456`

> 首次上线后建议立即修改默认管理员密码。
