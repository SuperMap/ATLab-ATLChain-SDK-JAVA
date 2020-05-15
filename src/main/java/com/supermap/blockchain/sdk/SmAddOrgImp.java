package com.supermap.blockchain.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.*;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.logging.Logger;

public class SmAddOrgImp {

    Logger logger = Logger.getLogger(SmTransactionImp.class.toString());
    private HFClient hfClient;
    private Channel channel;

    public SmAddOrgImp(HFClient hfClient, Channel channel) {
        this.hfClient = hfClient;
        this.channel = channel;
    };

    /**
     * TODO 尝试根据这种方式实现组织的删除和锚节点的增加
     * 将新增组织信息加入到之前的通道配置信息里面
     *
     */
    public JSONObject addNewOrgInfoToChannelConfigJson(JSONObject channelConfigJson, JSONObject newOrgConfigJson, String newOrgName){
        channelConfigJson.getJSONObject("channel_group").getJSONObject("groups").getJSONObject("Application").getJSONObject("groups").put(newOrgName, newOrgConfigJson);
        return channelConfigJson;
    }

    /**
     * 利用 configtxlator工具 将通道信息的一些配置转化为 JSON 格式
     * @param data
     * @return
     */
    public JSONObject getDecodeData(byte[] data){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://172.16.18.10:7059/protolator/decode/common.Config");
        httpPost.setEntity(new ByteArrayEntity(data));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() != 200){
                logger.info("信息返回错误，请确保 configtxlator工具以运行");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            String string = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            jsonObject = JSONObject.parseObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 利用 configtxlator工具将 JSON 文件转为 通道配置文件
     * @param jsonData
     * @return
     */
    public byte[] getEcodeData(String jsonData){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://172.16.18.10:7059/protolator/encode/common.Config");
        try {
            httpPost.setEntity(new StringEntity(jsonData));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() != 200){
                logger.info("信息返回错误，请确保 configtxlator工具以运行");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取编码后的字节码
        byte[] newConfigBytes = null;
        try {
            newConfigBytes = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newConfigBytes;
    }

    /**
     * 利用 configtxlator工具 计算前后通道配置信息的差异
     * @param original
     * @param updated
     * @param channelName
     * @return
     */
    public byte[] updataFromConfig(byte[] original, byte[] updated, String channelName){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://172.16.18.10:7059/configtxlator/compute/update-from-configs");
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("original", original, ContentType.APPLICATION_OCTET_STREAM, "orifinal.proto")
                .addBinaryBody("updated", updated, ContentType.APPLICATION_OCTET_STREAM, "updated.proto")
                .addBinaryBody("channel", channelName.getBytes()).build();

        httpPost.setEntity(multipartEntity);

        HttpResponse response;
        byte[] updataBytes = null;
        try {
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() != 200){
                logger.info("信息返回错误，请确保 configtxlator工具以运行");
                return null;
            }
            updataBytes = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updataBytes;
    }

    /**
     * JSON 文件 ------>>>>  字符串
     */
    public String jsonFileToString(String  jsonfilepath){

        StringBuffer strbuffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(jsonfilepath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);  //new String(str,"UTF-8")
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return strbuffer.toString();
    }

    /**
     * 构造 AdminUser
     * @param keyFile
     * @param certFile
     * @return
     * @throws Exception
     */
    public User getUser(String userMspId, String userName, String keyFile, String certFile) throws Exception {

//        String userName = "peer0.orga.example.com";
//        String userMspId = "OrgA";
//        String keyFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\keystore\\3ba8424db358ec6083c852a5b3701217a4c8d201d60aaba2e42dd9a7867a0c62_sk";
//        String certFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\signcerts\\Admin@orga.example.com-cert.pem";
        Enrollment enrollment = loadFromPemFile(keyFile, certFile);
        //构造用户
        SmUser user = new SmUser();
        user.setName(userName);
        user.setEnrollment(enrollment);
        user.setMspId(userMspId);
        return user;
    }

    /**
     * 根据证书和秘钥构造 Enrollment 用于构建 User
     * @param keyFile
     * @param certFile
     * @return
     * @throws Exception
     */
    public Enrollment loadFromPemFile(String keyFile, String certFile) throws Exception{
        byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));     //载入私钥PEM文本
        byte[] certPem = Files.readAllBytes(Paths.get(certFile));   //载入证书PEM文本
        CryptoPrimitives suite = new CryptoPrimitives();            //载入密码学套件
        PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);    //将PEM文本转换为私钥对象
        return new X509Enrollment(privateKey, new String(certPem));  //创建并返回X509Enrollment对象
    }


    /**
     * 计算出新增后通道配置信息和之前的信息差异
     * @param newOrgJsonPath
     * @param newOrgName
     * @return
     */
    public UpdateChannelConfiguration getupdateChannelConfiguration(String newOrgJsonPath, String newOrgName){

        // 第一步  得到通道配置信息
        byte[] channelConfigurationBytes = new byte[0];
        try {
            channelConfigurationBytes = channel.getChannelConfigurationBytes();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        }

        // 第二步  转为 json 格式
        JSONObject oldChannelConfigJson = getDecodeData(channelConfigurationBytes);

        // 第三步 得到有新组织生成的 json4 文件
        String newOrgJsonString = jsonFileToString(newOrgJsonPath);
        JSONObject newOrgConfigJson = JSONObject.parseObject(newOrgJsonString);

        // 第四步 将 org4 的内容添加到组织里面
        JSONObject newChannelConfigJson = addNewOrgInfoToChannelConfigJson(oldChannelConfigJson, newOrgConfigJson, newOrgName);

        //  将json文件提交给configtxlator 工具提供的encode方法，解码的消息类型为 common.Config
        byte[] newConfigBytes = getEcodeData(newChannelConfigJson.toJSONString());

        // 获取过通道的字节码，而这里我们有修改后的字节码，只要对这两个做一次对比；就可以获取到需要修改的部分内容
        //  依旧使用configtxlator工具，而这时候需要调用的是计算接口：compute,消息类型为：update-from-configs
        byte[] updataFromConfig = updataFromConfig(channelConfigurationBytes, newConfigBytes, channel.getName());

        // 使用转码的字节  构建 UpdateChannelConfiguration
        UpdateChannelConfiguration updateChannelConfiguration = null;
        try {
            updateChannelConfiguration = new UpdateChannelConfiguration(updataFromConfig);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return updateChannelConfiguration;
    }

}
