#!/bin/sh

APP_NAME="user-redpacket"
ENV=$2
USERNAME="work"
PASSWORD="yjyqhyxxcyshys"
SERVICE_DIR_PATH="/data/$APP_NAME"

SERVER_IP_INFO=`grep "^$1_" /usr/local/bin/ip.list | tr ' ' '\t' `
SERVER_IP=`echo "$SERVER_IP_INFO" | awk '{print $2}'`

case "$ENV" in
"test")
	SERVER_IP=$1;
  USERNAME="jindi"
  PASSWORD="jindi"
	;;
"yufa")
  USERNAME="work"
  PASSWORD="yjyqhyxxcyshys"
	;;
"online")
  USERNAME="work"
  PASSWORD="yjyqhyxxcyshys"
	;;
esac


mvn -U clean package -DskipTests

SOURCE_DIR="target/$APP_NAME"

TARGET_DIR="$SERVICE_DIR_PATH/`date '+%Y%m%d%H%M%S'`"

RUNTIME_DIR="$SERVICE_DIR_PATH/runtime"

# 拷贝文件，启动服务
expect -c "\
     set timeout 1200; \
	   spawn rsync -avz $SOURCE_DIR/ $USERNAME@$SERVER_IP:$TARGET_DIR/
     expect \"password:\"; \
     send \"$PASSWORD\r\"; \
     expect \"total size\"; \
     spawn ssh -P22 $USERNAME@$SERVER_IP;\
     expect \"password:\"; \
     send \"$PASSWORD\r\"; \
     expect \"@\"; \
     send \"rm -f $RUNTIME_DIR \r \";\
     send \"ln -s $TARGET_DIR $RUNTIME_DIR \r \"; \
     send \"bash $RUNTIME_DIR/server.sh restart $ENV\r\"; \
     expect \"started\"; \
     send \"exit\r\"; \
         "
# 登录机器查看日志
/usr/local/bin/sshpass -p $PASSWORD ssh -o "StrictHostKeyChecking no" $USERNAME@$SERVER_IP 'tail -f $SERVICE_DIR_PATH/logs/out.$HOSTNAME.log'


