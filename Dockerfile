# 1. JDK 17을 포함한 경량화된 OpenJDK 이미지를 사용
FROM eclipse-temurin:17-jdk-alpine

# 2. 환경 변수 설정
ARG JASYPT_ENCRYPTOR_PASSWORD
ENV JASYPT_ENCRYPTOR_PASSWORD=$JASYPT_ENCRYPTOR_PASSWORD

# 3. 작업 디렉토리 설정
WORKDIR /app

# 4. 빌드된 JAR 파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar

# 5. 실행 권한 부여
RUN chmod +x app.jar

# 6. 컨테이너가 시작될 때 실행할 명령어
CMD ["java", "-jar", "app.jar"]
