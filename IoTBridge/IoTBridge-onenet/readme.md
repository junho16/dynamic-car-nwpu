# 容器编译方式
## 运行项目 - 本地maven构建
```
## 打包
mvn clean package
## 运行项目
java -jar target/IoTBridge-OneNET-0.0.1-SNAPSHOT.jar
## 测试访问地址
http://xxxxx
```

## 运行项目 - 本地docker环境中构建
```
## dockerfile-maven-plugin打包并构建镜像
mvn clean package dockerfile:build
## 或者打包后使用docker命令构建镜像
mvn clean package
docker build -t chenyiming/IoTBridge-OneNET .
## 启动容器
docker run -p 8081:8081 -d --name IoTBridge-OneNET chenyiming/IoTBridge-OneNET:latest
## 测试访问地址
http://localhost:8081/swagger-ui.html
```

# 本项目编译命令
```
docker build -t chenyiming/IoTBridge-OneNET:0.1 .
docker run --rm -it -p 8090:8081  --privileged  -v /data/IoTBridge-OneNET:/app  chenyiming/IoTBridge-OneNET:0.1
docker push chenyiming/IoTBridge-OneNET:0.1
docker run -p 8090:8081 -d --name IoTBridge-OneNET  --privileged  -v /data/IoTBridge-OneNET:/app chenyiming/IoTBridge-OneNET:0.1
```