# ============================================================
# VincentCRM 后端多阶段构建 Dockerfile
# Stage 1: Maven 构建
# Stage 2: JRE 运行时
# ============================================================

# -------------------- Stage 1: 构建阶段 --------------------
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 先复制 POM 文件，利用 Docker 缓存加速依赖下载
COPY pom.xml .
COPY framework/pom.xml framework/
COPY crm/pom.xml crm/
COPY app/pom.xml app/

# 下载依赖（仅在 POM 变更时重新执行）
RUN mvn dependency:go-offline -B

# 复制完整源码
COPY framework/ framework/
COPY crm/ crm/
COPY app/ app/

# 执行构建，跳过测试
RUN mvn clean package -DskipTests -B

# -------------------- Stage 2: 运行时阶段 --------------------
FROM eclipse-temurin:17-jre

LABEL maintainer="VincentCRM"
LABEL description="VincentCRM 后端服务"

WORKDIR /app

# 从构建阶段复制 JAR 文件
COPY --from=builder /build/app/target/*.jar app.jar

# 创建非 root 用户运行应用
RUN groupadd -r crm && useradd -r -g crm crm && \
    chown -crm:crm /app/app.jar
USER crm

# 暴露后端服务端口
EXPOSE 8082

# 健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8082/actuator/health || exit 1

# JVM 参数可通过 JAVA_OPTS 环境变量传入
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 数据库和 Redis 连接可通过环境变量覆盖
ENV SPRING_DATASOURCE_URL="" \
    SPRING_DATASOURCE_USERNAME="" \
    SPRING_DATASOURCE_PASSWORD="" \
    SPRING_DATA_REDIS_HOST="" \
    SPRING_DATA_REDIS_PORT="" \
    SPRING_DATA_REDIS_PASSWORD=""

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
