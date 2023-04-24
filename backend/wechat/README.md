## 系统BACKEND技术简介
- SpringBoot1.5.2.Release
- openjdk 1.8
- 微信SDK:Weixin-popular
- spring data jpa 1.5.2Release
- Spring security 4.2.2Release
- JWT
- JXLS2.6.0
- Excel导出
- jasypt-spring-boot1.18
  - 加密使用
  - swagger
  - 用于API文档

## 系统运维说明
- 日志
  - 日志配置路径logging.path=/home/ubuntu/wechatserver/log/
- 文件导出位置
  - 导出文件路径export.path=/home/ubuntu/wechatserver/export/
- 项目相关命令
  - 打包命令 mvn -Dmaven.test.skip=true clean package
  - Jar包上传 scp merkle-wechat/target/merkle-wechat-0.0.1-SNAPSHOT.jar ubuntu@10.0.xx.xx:wechatserver
  - 启动服务 java -jar /home/ubuntu/wechatserver/merkle-wechat-0.0.1-SNAPSHOT.jar >> /home/ubuntu/wechatserver/log/catalina.out 2>&1 &
  - 启动服务 java -Dfile.encoding=utf-8 -jar /home/ubuntu/wechatserver/merkle-wechat-0.0.1-SNAPSHOT.jar >> /home/ubuntu/wechatserver/log/catalina.out 2>&1 &
  - 停止服务 
    1.  Ali Load balancer 拿掉对应机器，并查看日志是否停止翻滚
    2.  ps -A | grep java 找到对应的进程ID
    3.  kill xxx 杀掉进程
- 项目分支
  - develop-backend 后端代码
  - AIABranch  友邦保险后端代码包含AIAUnicaBridge项目
- 目前服务器配置:
  - Stage: 10.0.30.137
  - Prod:
    1.  10.0.20.1
    2.  10.0.30.142
- 数据库加密方式
  - 命令：java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password=密钥 algorithm=PBEWithMD5AndDES input=需要加密的内容
  - java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password=root algorithm=PBEWithMD5AndDES input=root
    1.  密钥：jasypt.encryptor.password属性的配置
    2.  jasypt-1.9.2.jar：见附件
  - 加密完的属内容使用ENC（加密完的内容）配置在配置文件中,如上图中的output就是加密完的内容
    1.  如：spring.datasource.username=ENC(Bo2J/fhATh4Uf+CCW+XFusZ7ImxfHoi3)
- Admin账号添加步骤
  - 在user表中新添加一个记录 password 使用数据库加密方式中得出的加密后的内容填充
  - 在user_wechat_public_no_mapping中添加user和 wechatPublicNo之间的关系， userId使用user表中的id字段，wechatPublicNoId使用wechatpublicno 表中的id字段
