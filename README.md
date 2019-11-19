# Apc-Mall 壹平台电子商城


## 项目结构

    apc-mall
        |- mall-common
            |- common-spring-boot-starter
            |- database-spring-boot-starter
            |- dubbo-spring-boot-starter
            |- elasticsearch-spring-boot-starter
            |- mq-spring-boot-starter
            |- redis-spring-boot-starter
            |- security-spring-boot-starter
            |- swagger-spring-boot-starter
            
        |- mall-api
            |- base-api               6002
            |- buyer-api
            |- seller-api
            |- manager-api
            
        |- mall-client
            |- base-client
            |- user-client
            |- seller-client
            |- manager-client
            
        |- mall-service
            |- base-svc               7001
            |- user-svc
            |- seller-svc
            |- manager-svc
            
        |- mall-ui
            |- buyer
            |- manager-seller
            |- manager-admin
            

## 配置说明
    dubbo:
        protocol:
            name: dubbo
            port: -1   # -1表示随机递增

## 分层领域模型规约：

    1. DO（Data Object）：与数据库表结构一一对应，通过DAO层向上传输数据源对象。
    2. DTO（Data Transfer Object）：数据传输对象，Service或Manager向外传输的对象。
    3. BO（Business Object）：业务对象。由Service层输出的封装业务逻辑的对象。
    4. AO（Application Object）：应用对象。在Web层与Service层之间抽象的复用对象模型，极为贴近展示层，复用度不高。
    5. VO（View Object）：显示层对象，通常是Web向模板渲染引擎层传输的对象。
    6. Query：数据查询对象，各层接收上层的查询请求。注意超过2个参数的查询封装，禁止使用Map类来传输。
    

- DAO 层
    - 入参，使用 DO（Data Object）。
    - 出参，使用 DO（Data Object）。
- Service 层
    - 入参，使用 DTO（Data Transfer Object）。
    - 出参，使用 BO（Business Object）。
- Controller 层
    - 入参，使用 DTO（Data Transfer Object）。
    - 出参，使用 BO（Business Object）。