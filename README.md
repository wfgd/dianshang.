# 电商平台项目

## 技术栈
- **后端**: Spring Boot 2.7 + MyBatis-Plus + JWT + BCrypt + SQL Server
- **前端**: Vue3 + Axios + Element Plus (CDN方式)
- **数据库**: SQL Server

## 快速启动

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- SQL Server (2017+)

### 2. 数据库初始化
在 SQL Server 中执行 `sql/init.sql` 脚本，创建数据库和表结构，并插入示例商品数据。

### 3. 修改数据库配置
编辑 `src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=ecommerce;encrypt=false;trustServerCertificate=true
    username: sa
    password: 你的密码
```

### 4. 启动后端
```bash
# 在项目根目录执行
mvn clean package -DskipTests
mvn spring-boot:run
```
后端默认运行在 `http://localhost:8080`

### 5. 访问前端
浏览器打开 `http://localhost:8080/index.html`

### 6. 创建测试用户
使用以下API创建测试用户（也可以用Postman等工具）：
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","phone":"13800138000"}'
```

## 项目结构

```
├── pom.xml                          # Maven依赖配置
├── sql/
│   └── init.sql                     # 数据库初始化脚本
└── src/main/
    ├── java/com/ecommerce/
    │   ├── EcommerceApplication.java      # 启动类
    │   ├── config/                        # 配置类
    │   │   ├── CorsConfig.java            # 跨域配置
    │   │   ├── MybatisPlusConfig.java     # MyBatis-Plus分页插件
    │   │   └── WebConfig.java             # 过滤器注册
    │   ├── controller/                    # 控制器
    │   │   ├── AuthController.java        # 登录接口
    │   │   ├── ProductController.java     # 商品接口
    │   │   ├── CartController.java        # 购物车接口
    │   │   └── OrderController.java       # 订单接口
    │   ├── dto/                           # 数据传输对象
    │   │   ├── LoginRequest.java
    │   │   └── OrderCreateDTO.java
    │   ├── entity/                        # 实体类
    │   │   ├── Users.java
    │   │   ├── Products.java
    │   │   ├── Cart.java
    │   │   ├── Orders.java
    │   │   └── OrderItems.java
    │   ├── filter/                        # 过滤器
    │   │   └── JwtAuthenticationFilter.java
    │   ├── mapper/                        # MyBatis Mapper
    │   │   ├── UsersMapper.java
    │   │   ├── ProductsMapper.java
    │   │   ├── CartMapper.java
    │   │   ├── OrdersMapper.java
    │   │   └── OrderItemsMapper.java
    │   ├── service/                       # 服务接口
    │   │   ├── UsersService.java
    │   │   ├── ProductsService.java
    │   │   ├── CartService.java
    │   │   └── OrdersService.java
    │   ├── service/impl/                  # 服务实现
    │   │   ├── UsersServiceImpl.java
    │   │   ├── ProductsServiceImpl.java
    │   │   ├── CartServiceImpl.java
    │   │   └── OrdersServiceImpl.java
    │   └── util/                          # 工具类
    │       ├── JwtUtil.java               # JWT工具
    │       └── Result.java                # 统一返回结果
    └── resources/
        ├── application.yml                # 应用配置
        └── static/
            └── index.html                 # 前端页面
```

## API接口

| 路径 | 方法 | 认证 | 说明 |
|------|------|------|------|
| /api/auth/login | POST | 无 | 登录，返回token |
| /api/users/register | POST | 无 | 用户注册 |
| /api/products | GET | Bearer | 商品列表（分页+搜索） |
| /api/cart/add | POST | Bearer | 加入购物车 |
| /api/cart/list | GET | Bearer | 购物车列表 |
| /api/orders/create | POST | Bearer | 从购物车下单 |
| /api/orders/my | GET | Bearer | 我的订单（分页） |

## 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 认证流程

1. 登录成功返回JWT Token（有效期2小时），前端存储到localStorage
2. 后续请求在Header中携带 `Authorization: Bearer <token>`
3. 后端JwtAuthenticationFilter拦截验证，将userId注入Request域
4. Controller通过 `@RequestAttribute("userId")` 获取当前用户ID
5. Token无效返回401，前端axios拦截器自动跳转登录页