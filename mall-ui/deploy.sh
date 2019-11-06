#! /bin/bash

WEB_PATH=$PWD
cd $WEB_PATH

# 更新git代码
if [[ "$1" == "git-pull" ]];then
  echo "拉取最新的代码..."
  git reset --hard origin/master
  git clean -f
  git pull origin master
  git pull
  echo -e "\033[32m代码拉取完成...\033[0m"
fi

# 拷贝公共文件
if [[ "$1" == "copy" ]];then
  echo "拷贝ui-domain..."
  cp -a $WEB_PATH/ui-domain $WEB_PATH/buyer/pc
  cp -a $WEB_PATH/ui-domain $WEB_PATH/buyer/wap
  cp -a $WEB_PATH/ui-domain $WEB_PATH/manager-seller
  cp -a $WEB_PATH/ui-domain $WEB_PATH/manager-admin
  echo -e "\033[32m拷贝完成\033[0m"
  echo "拷贝ui-components..."
  cp -a $WEB_PATH/ui-components $WEB_PATH/manager-seller
  cp -a $WEB_PATH/ui-components $WEB_PATH/manager-admin
  echo -e "\033[32m拷贝完成\033[0m"
  echo "拷贝ui-utils..."
  cp -a $WEB_PATH/ui-utils $WEB_PATH/buyer/pc
  cp -a $WEB_PATH/ui-utils $WEB_PATH/buyer/wap
  cp -a $WEB_PATH/ui-utils $WEB_PATH/manager-seller
  cp -a $WEB_PATH/ui-utils $WEB_PATH/manager-admin
  echo -e "\033[32m拷贝完成\033[0m"
fi

# 配置后台API模式
PATH_DOMAIN=$WEB_PATH/ui-domain
PATH_BUYER_PC=$WEB_PATH/buyer/pc/ui-domain
PATH_BUYER_WAP=$WEB_PATH/buyer/wap/ui-domain
PATH_SELLER=$WEB_PATH/manager-seller/ui-domain
PATH_ADMIN=$WEB_PATH/manager-admin/ui-domain
PATH_ARRAY=($PATH_BUYER_PC $PATH_BUYER_WAP $PATH_SELLER $PATH_ADMIN)
if [[ "$1" == "api_dev" ]];then
  for path in ${PATH_ARRAY[@]}
  do
    sed -i.bak "s/'pro'/'dev'/" $path/index.js
    rm -rf $path/index.js.bak
  done
  sed -i.bak "s/'pro'/'dev'/" $PATH_DOMAIN/index.js
  rm -rf $PATH_DOMAIN/index.js.bak
  echo -e "\033[32m适配后端API环境更改为[dev]！\033[0m"
fi
if [[ "$1" == "api_pro" ]];then
  for path in ${PATH_ARRAY[@]}
  do
    sed -i.bak "s/'dev'/'pro'/" $path/index.js
    rm -rf $path/index.js.bak
  done
  sed -i.bak "s/'dev'/'pro'/" $PATH_DOMAIN/index.js
  rm -rf $PATH_DOMAIN/index.js.bak
  echo -e "\033[32m适配后端API环境更改为[pro]！\033[0m"
fi

# 部署nodejs、nginx、pm2
if [[ "$1" == "base" ]];then
  # 移除yum lock
  rm -f /var/run/yum.pid

  # 关闭防火墙
  systemctl stop firewalld
  systemctl disable firewalld

  # 安装nodejs
  cd /usr/local/
  wget https://npm.taobao.org/mirrors/node/v8.11.0/node-v8.11.0-linux-x64.tar.gz
  tar -zxvf node-v8.11.0-linux-x64.tar.gz
  rm -rf node-v8.11.0-linux-x64.tar.gz
  mv node-v8.11.0-linux-x64 node
  echo 'export NODE_HOME=/usr/local/node  
export PATH=$NODE_HOME/bin:$PATH'>>/etc/profile
  source /etc/profile

  # 安装pm2
  npm install pm2 -g

  # 链接
  sudo ln -s /usr/local/node/bin/node /usr/bin/node
  sudo ln -s /usr/local/node/bin/node /usr/lib/node
  sudo ln -s /usr/local/node/bin/npm /usr/bin/npm
  sudo ln -s /usr/local/node/bin/pm2 /usr/bin/pm2
  sudo ln -s /usr/local/node/bin/pm2 /usr/lib/pm2

  # nginx安装
  yum install -y gcc gcc-c++
  yum install -y pcre pcre-devel
  yum install -y zlib zlib-devel
  yum install -y openssl openssl-devel

  cd /usr/local
  wget https://nginx.org/download/nginx-1.14.0.tar.gz
  tar -zxvf nginx-1.14.0.tar.gz
  cd nginx-1.14.0
  ./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module
  make && make install
  cd ..
  rm -rf nginx-1.14.0.tar.gz
  rm -rf nginx-1.14.0
fi

if [[ ! -n "$1" || "$1" == "manager-admin" ]];then
  echo "开始部署后台管理(manager-admin)..."
  cd $WEB_PATH/manager-admin
  rm -rf node_modules
  echo "开始安装项目依赖..."
  sudo npm install
  echo "开始打包..."
  sudo npm run build:prod
  echo -e "\033[32m后台管理部署完成...\033[0m"
fi

if [[ ! -n "$1" || "$1" == "manager-seller" ]];then
  echo "开始部署商家中心(manager-seller)..."
  cd $WEB_PATH/manager-seller
  rm -rf node_modules
  echo "开始安装项目依赖..."
  sudo npm install
  echo "开始打包..."
  sudo npm run build:prod
  echo -e "\033[32m商家中心部署完成...\033[0m"
fi

if [[ ! -n "$1" || "$1" == "buyer-pc" ]];then
  echo "开始部署买家PC端(buyer-pc)..."
  cd $WEB_PATH/buyer/pc
  rm -rf node_modules
  echo "开始安装项目依赖..."
  sudo npm install
  sudo npm run build
  echo -e "\033[32m买家PC端build完成\033[0m"
  # 启动买家端PC
  pm2 delete buyer-pc
  pm2 start npm --name "buyer-pc" -- run start
  echo -e "\033[32mbuyer-pc部署完成！\033[0m"
fi

if [[ ! -n "$1" || "$1" == "buyer-wap" ]];then
  echo "开始部署买家WAP端(buyer-wap)..."
  cd $WEB_PATH/buyer/wap
  rm -rf node_modules
  echo "开始安装项目依赖..."
  sudo npm install
  sudo npm run build
  echo "买家WAP端build完成"
  #启动买家端WAP
  pm2 delete buyer-wap
  pm2 start npm --name "buyer-wap" -- run start
  echo -e "\033[32mbuyer-wap部署完成！\033[0m"
fi

