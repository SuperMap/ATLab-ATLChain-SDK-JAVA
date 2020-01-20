# ATLab-ATLChain-SDK-JAVA

## 简介

该项目是基于 [Hyperledger Fabric SDK JAVA](https://github.com/hyperledger/fabric-sdk-java) 的封装，以方便基于 Fabric 的应用开发。 

## 功能介绍

1. [交易管理](https://github.com/SuperMap/ATLab-ATLChain-SDK-JAVA/blob/master/src/main/java/com/supermap/blockchain/sdk/SmTransaction.java) ：实现了执行和查询交易等功能。

2. [链码管理](https://github.com/SuperMap/ATLab-ATLChain-SDK-JAVA/blob/master/src/main/java/com/supermap/blockchain/sdk/SmChaincode.java)：实现了链码的安装、实例化、升级、查看等功能。

3. [通道管理](https://github.com/SuperMap/ATLab-ATLChain-SDK-JAVA/blob/master/src/main/java/com/supermap/blockchain/sdk/SmChannel.java)：实现了查看加入通道的节点、区块信息等功能。

4. [CA管理](https://github.com/SuperMap/ATLab-ATLChain-SDK-JAVA/blob/master/src/main/java/com/supermap/blockchain/sdk/SmCA.java)：实现了用户注册、注销、重注册、获取CRL等功能。

## 使用方法

1. 编译 jar 包：

```$xslt
$ gradle jar    # 在项目根目录下执行， jar 包默认生成位置为 /build/libs
```

2. 添加依赖项

Maven:
```$xslt
// Fabric sdk java
<dependency>
    <groupId>org.hyperledger.fabric-sdk-java</groupId>
    <artifactId>fabric-sdk-java</artifactId>
    <version>1.4.4</version>
</dependency>

// ATLChain sdk java
<dependency>
    <groupId>com.supermap.blockchain</groupId>
    <artifactId>sdk</artifactId>
    <scope>system</scope>
    <systemPath>{path/to}/atlchain-sdk-1.0.jar</systemPath>
    <version>1.0</version>
</dependency>
```

Gradle:
```$xslt
// Fabric sdk java
compile group: 'org.hyperledger.fabric-sdk-java', name: 'fabric-sdk-java', version: '1.4.4'

// ATLChain sdk java
compile files('{path/to}/atlchain-sdk-1.0.jar')
```

3. 程序示例请参考[这里](https://github.com/SuperMap/ATLab-ATLChain-SDK-JAVA/tree/master/src/test/java/com/supermap/blockchain/sdk)

4. 生成接口文档
```$xslt
$ gradle javadoc  # 在项目根目录下执行， java 接口文档默认生成位置为 /build/docs
```