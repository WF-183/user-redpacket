#!/bin/bash

# 遇到错误时退出
set -e

# 用户设置
APP_NAME="user-redpacket"

PROFILE_NAME=$2

APOLLO_PARAM=""

cd `dirname $0`

JAVA_HOME="/data/jdk1.8.0_60/bin"

JAR_PATH=`pwd`

FILE_EXTENSION="jar"

LOG_DIR_PATH="/data/applogs/current/${APP_NAME}/"
LOG_FILE_NAME="out.$HOSTNAME.log"

DUMP_DIR_PATH="$JAR_PATH/../jvm/dump/"
GC_DIR_PATH="$JAR_PATH/../jvm/gc/"

GRACEFUL_SHUTDOWN_TIME=10

#获取hostip
IP=`ifconfig -a | grep inet | grep -v 127.0.0.1 | grep -v inet6 | awk '{print $2}' | tr -d "addr:"`

# 堆设置
if [[ $2 = 'test' ]]; then
  JAVA_OPTS="-Xms1g -Xmx1g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
else
  JAVA_OPTS="-Xms2g -Xmx2g -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m"
fi

# skywalking
JAVA_OPTS="$JAVA_OPTS -javaagent:/data/skywalking/skywalking-agent.jar"

# GC 设置
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+CMSClassUnloadingEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+ExplicitGCInvokesConcurrent -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$DUMP_DIR_PATH -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"

# GC 额外设置
JAVA_OPTS="$JAVA_OPTS -Xloggc:$GC_DIR_PATH/gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./gc/heap_%p.hprof"

# JAVA其他设置
JAVA_OPTS="$JAVA_OPTS -Dconfigured.host=$IP -Djava.util.concurrent.ForkJoinPool.common.parallelism=100 -DSW_AGENT_NAME=$APP_NAME -DSW_AGENT_INSTANCE_NAME=$HOSTNAME"

# 使用说明
usage() {
	echo "用法: server.sh start|restart [APP_NAME] [PROFILE_NAME:locale|online|test|yufa]"
	echo "     (启动|重启)"
	echo "  或: server.sh status|stop [APP_NAME]"
	echo "     (状态查询|关闭)"
	echo "  或: server.sh log [APP_NAME]"
	echo "     (查询服务日志)"
	echo "注意事项:"
	echo "- 请在使用前设置好JAR_PATH参数"
	echo "- 默认扩展名为jar，如有需要可以配置FILE_EXTENSION参数"
	echo "- 日志打印路径参数LOG_DIR_PATH，日志名参数LOG_FILE_NAME"
	echo "- 本地启动没有skywalking包时，请注释掉设置#skywalking部分的JAVA_OPTS"
	echo "- 目前服务仅能在一台机器上部署一个实例"
}

# 展示信息
display() {
	pid=$(ps -ef | grep $APP_NAME.$FILE_EXTENSION | grep -v grep | awk '{ print $2 }')
	echo - JAR_NAME: $APP_NAME.$FILE_EXTENSION
	if [ -n "$pid" ]
	then
	    echo - PID: $pid
	fi
	echo - IP/HOST: $IP
	echo - JAR_PATH: $JAR_PATH/$APP_NAME.$FILE_EXTENSION
	echo - LOG_FILE_PATH: $LOG_DIR_PATH/$LOG_FILE_NAME
}

# 启动
start() {
	PID=$(ps -ef | grep $APP_NAME.$FILE_EXTENSION | grep -v grep | awk '{ print $2 }')
	if [ -n "$PID" ]
	then
	    echo $APP_NAME is not stopped
	    exit 0
    fi
	echo start $APP_NAME.$FILE_EXTENSION
	startup
	echo $APP_NAME started
	display
}

# 启动
startup() {
	if [ ! -d $LOG_DIR_PATH ]
	then
		mkdir -p $LOG_DIR_PATH
	fi
	if [ ! -f $LOG_DIR_PATH/$LOG_FILE_NAME ]
	then
	    touch $LOG_DIR_PATH/$LOG_FILE_NAME
	fi
	if [ ! -d $DUMP_DIR_PATH ]
	then
		mkdir -p $DUMP_DIR_PATH
	fi
	if [ ! -d $GC_DIR_PATH ]
	then
		mkdir -p $GC_DIR_PATH
	fi
	nohup $JAVA_HOME/java $APOLLO_PARAM -jar $JAVA_OPTS  "$JAR_PATH/${APP_NAME}.${FILE_EXTENSION}" --spring.profiles.active=$PROFILE_NAME  >$LOG_DIR_PATH/$LOG_FILE_NAME 2>&1 &
}


# 停止
stop() {
	PID=$(ps -ef | grep $APP_NAME.$FILE_EXTENSION | grep -v grep | awk '{ print $2 }')
	if [ -z "$PID" ]
	then
	    echo $APP_NAME is already stopped
	else
	    echo kill $PID
	    kill $PID
	    sleep $GRACEFUL_SHUTDOWN_TIME
	    if [ -z "$PID" ]
	    then
	    	kill -9 $PID
	    fi
	fi
}

# 状态
status(){
	count=$(ps -ef | grep $APP_NAME.$FILE_EXTENSION | grep -v grep | wc -l)
	if [ $count != 0 ]
	then
		echo $APP_NAME.$FILE_EXTENSION is running
	else
		echo $APP_NAME.$FILE_EXTENSION is not running
	fi
	display
}


# 重新启动
restart() {
	echo stopping $APP_NAME
	stop
	echo $APP_NAME stopped
	echo start $APP_NAME
	startup
	echo $APP_NAME started
}

# 查看log
log() {
	tail -f -n 1000 "$LOG_DIR_PATH/$LOG_FILE_NAME"
}


if [ "$1" != "start" ] && [ "$1" != "restart" ] && [ "$1" != "status" ] && [ "$1" != "stop" ] && [ "$1" != "log" ]
then
	echo "请输入正确的命令参数: start|restart|status|stop|log"
	echo ""
	usage
	exit 0
elif [ "$1" = "start" -a $# -ne 2 ] || [ "$1" = "restart" -a $# -ne 2 ] || [ "$1" = "status" -a $# -ne 1 ] || [ "$1" = "stop" -a $# -ne 1 ] || [ "$1" = "log" -a $# -ne 1 ]
then
	echo "命令格式错误"
	echo ""
	usage
	exit 0
fi

if [ "$JAR_PATH" = "[请设置此项目]" ]
then
	echo "请设置JAR_PATH参数"
	exit 0
fi

case $1 in
	'start')
		start
	;;
	'stop')
		stop
	;;
	'restart')
		restart
	;;
	'status')
		status
	;;
	'log')
		log
	;;
	*)
		usage
	;;
esac