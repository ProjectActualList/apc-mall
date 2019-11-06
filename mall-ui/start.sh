WEB_PATH=$PWD

# 启动买家端PC端
cd $WEB_PATH/b2b2c_v5
pm2 delete buyer-pc
pm2 start npm --name "buyer-pc" -- run start

# 启动买家WAP端
cd $WEB_PATH/default
pm2 delete buyer-wap
pm2 start npm --name "buyer-wap" -- run start

# 启动nginx
/usr/local/nginx/sbin/nginx -s reload
