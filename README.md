# Apc-Mall 壹平台电子商城


## 项目结构

    apc-mall
        |- apc-common
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