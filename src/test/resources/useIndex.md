## 如何在 Fabric 中使用索引
在 Hyperledger Fabric 项目中，目前可以支持的状态数据库有两种：

- LevelDB：LevelDB 是嵌入在 Peer 中的默认键值对（key-value）状态数据库。

- CouchDB：CouchDB 是一种可选的替代 levelDB 的状态数据库。与 LevelDB 键值存储一样，CouchDB 不仅可以根据 key 进行相应的查询，还可以根据不同的应用场景需求实现复杂查询。

使用 CouchDB 状态数据库，当存储的数据量较大以后，富查询的速度会变慢，可能会导致查询超时，为避免这种情况发生，就必须定义索引提高查询速度

### 1.定义索引
索引一般和链码放在一起便于管理，示例链码放在  `\resources\META-INF\statedb\couchdb\indexes` 文件夹下的  `bcgisIndextest.json` 中

### 2.使用索引
采用 `json` 格式定义的索引如下

```shell script
{
  "index":{
    "fields":["hashIndex"]
  },
  "ddoc":"test",
  "name":"bcgisIndextest",
  "type":"json"
}
```
`fields` 字段用于定义索引
`ddoc`   字段用于定义该索引的注释
`name`   字段用于定义索引名称（和 `json` 名一样）
`type`   字段用于定义索引类型

若想定义多个索引，在`fields`字段中添加对应索引的字段即可

#### 2.1 安装索引
使用`fabric java sdk` 安装索引，在安装链码时需定义`installProposalRequest`如下

```shell script
$ installProposalRequest.setChaincodeMetaInfLocation(new File(metaInfo ));
```
其中`metaInfo` 即是我们刚才建立的文件夹`META-INF/statedb/couchdb/indexes/`的上级目录即可,示例中到`resources`

查看节点 `docker` 容器的日志，打开一个新的终端窗口，然后运行下边的命令来匹配索 引被创建的确认信息

```shell script
$ docker logs peer0.org1.example.com  2>&1 | grep "CouchDB index"
```
你将会看到类似下边的结果

```shell script
[couchdb] CreateIndex -> INFO 0be Created CouchDB index [bcgisIndextest] in state database [mychannel_marbles] using design document [_design/test]
```

#### 2.2 使用索引

当存入的值为`json`格式时，在进行复查询时使用