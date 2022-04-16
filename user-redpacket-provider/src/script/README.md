# 自动化部署脚本

自动化部署脚本是为了快速部署、重启springboot生成的jar包而编写的shell脚本，可以通过参数调整、配置快速的拼接各种参数，完成
复杂的启动过程


1、使用mvn命令在core-data-provider下打包，参考命令：

    mvn -s "/Users/baodinglan/environment/settings.xml"  -Dmaven.test.skip=true -U -X clean package
    
2、复制包与本目录下server.sh脚本到相应环境下

3、修改server.sh中JAR_PATH参数为Jar包所在目录

4、执行server.sh脚本，命令参考：
    
    ./server.sh start core-data locale
    
    用法: server.sh start|restart [APP_NAME] [PROFILE_NAME:locale|online|test|yufa]
         (启动|重启)
      或: server.sh status|stop [APP_NAME]
         (状态查询|关闭)
      或: server.sh log [APP_NAME]
         (查询服务日志)
         
1、原理上来说不同服务可以共用该脚本，如需单独配置，可以修改${APP_NAME}与${PROFILE_NAME}保证部署准确

2、JVM参数在不同环境可能需要进行调节，修改JAVA_OPTS参数即可

3、本地启动没有skywalking包时，请注释掉设置#skywalking部分的JAVA_OPTS

4、日志打印路径参数LOG_DIR_PATH；日志名参数LOG_FILE_NAME；JVM dump路径为DUMP_DIR_PATH；GC日志打印路径为GC_DIR_PATH，
请确保上述目录的写权限

5、服务预留的平滑关闭时间为10秒，参数为GRACEFUL_SHUTDOWN_TIME，如需调整修改该参数即可


1、pom中<packaging>jar</packaging>设置由war改为jar（使用war包也可以正常启动，但是为了明确性考虑，建议改为jar）

2、pom中

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>
    
  需要注释掉（未注释掉时也可以用 java -jar 正常启动，原因未知）

3、配置文件结构进行调整，参考provider下src/main/resources/config，配置文件分成了5个，application.yml为主配置文件，
其他4个配置文件分别对应4个环境

4、logback进行调整，不同的环境对应了不同的日志级别，测试使用了debug级，线上使用info

5、增加了EnvironmentPrinter类，打印环境配置文件（如：application-locale.yml）中，spring.profiles的内容，方便查询
正在生效的配置文件