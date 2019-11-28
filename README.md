# flakesoup-auth
Auth module based on Spring Security


###
    CREATE DATABASE  `flakesoup-auth` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
    
    
### 生成非对称密钥
    1. 生成 JKS Java KeyStore 文件
    keytool -genkeypair -alias flakesoup-auth -keyalg RSA -keypass flakesoup-auth-pass -keystore flakesoup-auth.jks -storepass flakesoup-auth-pass  -storetype PKCS12
    
    2. 导出公钥
    keytool -list -rfc --keystore flakesoup-auth.jks | openssl x509 -inform pem -pubkey
    
    3. 将公钥保存为 pubkey.txt，将 flakesoup-auth.jks（）授权服务器） 和 pubkey.txt（资源服务器） 放到 resource 目录下