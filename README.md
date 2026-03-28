# 开源数据采集系统 API

## 项目简介

基于 Spring Boot 3.2.1 开发的开源数据采集系统后端服务，提供完整的数据采集、信源管理、任务调度、节点监控等功能。

## 技术栈

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **Spring Security**
- **MySQL / H2**
- **Redis**
- **JWT**
- **Swagger/OpenAPI 3**

## 项目结构

```
data-collection-api/
├── src/main/java/com/datacollection/
│   ├── config/                 # 配置类
│   ├── controller/             # 控制器层
│   ├── dto/                    # 数据传输对象
│   ├── entity/                 # 实体类
│   ├── enums/                  # 枚举类
│   ├── repository/             # 数据访问层
│   ├── service/                # 服务接口
│   │   └── impl/               # 服务实现
│   └── DataCollectionApplication.java
├── src/main/resources/
│   └── application.yml         # 配置文件
└── pom.xml
```

## API 接口文档

### 1. 认证管理 `/api/auth`

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /captcha | 获取验证码 |
| POST | /login | 用户登录 |
| POST | /refresh | 刷新Token |
| POST | /logout | 登出 |
| GET | /ukey/check | 检查UKey状态 |

### 2. 任务管理 `/api/tasks`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | / | 创建任务 |
| PUT | /{id} | 更新任务 |
| DELETE | /{id} | 删除任务 |
| GET | /{id} | 获取任务详情 |
| GET | /search | 搜索任务 |
| POST | /{taskId}/start | 启动任务 |
| POST | /{taskId}/stop | 停止任务 |
| POST | /{taskId}/pause | 暂停任务 |
| POST | /{taskId}/resume | 恢复任务 |
| GET | /stats | 获取任务统计 |
| GET | /running | 获取运行中的任务 |
| GET | /{taskId}/logs | 获取任务日志 |

### 3. 节点管理 `/api/nodes`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | / | 添加节点 |
| PUT | /{id} | 更新节点 |
| DELETE | /{id} | 删除节点 |
| GET | /{id} | 获取节点详情 |
| GET | / | 获取所有节点 |
| GET | /online | 获取在线节点 |
| POST | /{nodeId}/restart | 重启节点 |
| POST | /{nodeId}/shutdown | 关闭节点 |
| POST | /{nodeId}/heartbeat | 更新心跳 |
| GET | /stats | 获取节点统计 |

### 4. 网站信源管理 `/api/sources/websites`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | / | 添加网站 |
| POST | /batch | 批量导入 |
| PUT | /{id} | 更新网站 |
| DELETE | /{id} | 删除网站 |
| GET | /{id} | 获取网站详情 |
| GET | /search | 搜索网站 |
| GET | /count | 获取网站数量 |

### 5. 社交账号管理 `/api/sources/social`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | / | 添加账号 |
| POST | /batch | 批量导入 |
| PUT | /{id} | 更新账号 |
| DELETE | /{id} | 删除账号 |
| GET | /{id} | 获取账号详情 |
| GET | /search | 搜索账号 |
| GET | /platform/{platform} | 按平台获取 |
| GET | /count | 获取账号数量 |

### 6. 人物管理 `/api/sources/persons`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | / | 添加人物 |
| PUT | /{id} | 更新人物 |
| DELETE | /{id} | 删除人物 |
| GET | /{id} | 获取人物详情 |
| GET | /search | 搜索人物 |
| GET | /count | 获取人物数量 |

### 7. 组织管理 `/api/sources/organizations`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | / | 添加组织 |
| PUT | /{id} | 更新组织 |
| DELETE | /{id} | 删除组织 |
| GET | /{id} | 获取组织详情 |
| GET | /search | 搜索组织 |
| GET | /count | 获取组织数量 |

### 8. 数据中心 `/api/data`

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /search | 搜索数据 |
| GET | /{id} | 获取数据详情 |
| POST | /export | 批量导出 |
| GET | /count | 获取数据总量 |
| GET | /count/today | 获取今日采集量 |

### 9. 数据监控 `/api/monitor`

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /alerts | 获取预警列表 |
| GET | /alerts/{id} | 获取预警详情 |
| POST | /alerts/{id}/process | 处理预警 |
| POST | /alerts/{id}/resolve | 解决预警 |
| POST | /alerts | 创建预警 |
| GET | /stats | 获取监控统计 |

### 10. 概览统计 `/api/overview`

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /stats | 获取概览统计 |
| GET | /sources/stats | 获取信源统计 |

## 启动项目

```bash
# 编译项目
mvn clean package

# 运行项目
java -jar target/data-collection-api-1.0.0.jar
```

## 访问文档

启动项目后访问:
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/v3/api-docs

## 许可证

Apache License 2.0
