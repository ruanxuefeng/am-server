#第一行必须指令基于的基础镜像
FROM adoptopenjdk/openjdk8:latest
#创建一个可以从本地主机或其他容器挂载的挂载点，一般用来存放数据库和需要保持的数据等。
VOLUME /tmp
#该命令将复制指定的 到容器中的,并且重命名
ADD am-server.jar am-server.jar
#配置容器启动后执行的命令，并且不可被 docker run 提供的参数覆盖
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/am-server.jar"]
