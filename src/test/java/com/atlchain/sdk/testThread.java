package com.atlchain.sdk;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class RunnableDemoQuery implements Runnable {
    private Thread t;
    private String threadName;
    private int count;
    private ATLChain atlChain;
    private List<String> list = new ArrayList<String>();

    RunnableDemoQuery(String name, int count) {
        threadName = name;
        this.count = count;
        File networkFile = new File(this.getClass().getResource("/network-config-test.yaml").getPath());
        atlChain = new ATLChain(networkFile);
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        int startIndex = TestThread.count * count;
        int endIndex = TestThread.count * (count + 1);
        for(int i = startIndex; i < endIndex; i++) {
            String key = "tkey" + i;
            try {
                byte[][] result = atlChain.queryByte(
                        "bcgiscc",
                        "GetRecordByKey",
                        new byte[][]{key.getBytes()}
                );
//                list.add(new String(result[0]));
//                for (byte[] res : result) {
//                    System.out.println(key + ": " + new String(res));
//                    list.add(new String(res));
//                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        TestThread.threadCount += 1;
        System.out.println(TestThread.threadCount);
//        list.sort(Comparator.naturalOrder());
//        System.out.println(list);

        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - startTime) / 1000L;
        System.out.println(threadName + "=>Time: " + totalTime + "s ,TPS: " + TestThread.count / totalTime);
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}

class RunnableDemoWrite implements Runnable {
    private Thread t;
    private String threadName;
    private int count;
    private ATLChain atlChain;

    RunnableDemoWrite(String name, int count) {
        threadName = name;
        this.count = count;
        File networkFile = new File(this.getClass().getResource("/network-config-test.yaml").getPath());
        atlChain = new ATLChain(networkFile);
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        int startIndex = TestThread.count * count;
        int endIndex = TestThread.count * (count +1);

        for(int i = startIndex; i < endIndex; i++) {
            // 文件存HDFS，并返回hash
//            String fileHash = TestThread.testHDFSUpload();
//            String key = fileHash + i;

            // 文件存储链上
//            byte[] fileBytes = new byte[0];
//            try {
//                fileBytes = Files.readAllBytes(Paths.get("/home/cy/Downloads/Hyperledger Global Use Cases - CDEL.pptx"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            String key = "tkey" + i;

            try {
                String result = atlChain.invokeByte(
                        "bcgiscc",
                        "PutRecordBytes",
                        new byte[][]{key.getBytes(), ("value" + i).getBytes()} // fileBytes} //
                );
//                System.out.println(i + ": " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        TestThread.threadCount += 1;
//        System.out.println(TestThread.threadCount);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("chainTime: " + totalTime);
        System.out.println(threadName + "=>Time: " + totalTime + "s ,TPS: " + 1000 * TestThread.count / totalTime);
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}

class TestThread {
    static int threadCount = 0;
    static int totalCount = 10000;
    static int threadNumber = 5;
    static int count = totalCount / threadNumber;
    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
//        testHDFSUpload();
        for (int i = 0; i < threadNumber; i++) {
//            RunnableDemoWrite R = new RunnableDemoWrite("Thread-" + i, i);
            RunnableDemoQuery R = new RunnableDemoQuery("Thread-" + i, i);
            R.start();
        }

        while (true) {
            if (threadCount >= threadNumber) {
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
//        System.out.println(endTime - startTime);
        System.out.println("Time: " + totalTime + "s ,TPS: " + 1000 * totalCount / totalTime);
    }

    public static String testHDFSUpload() {
        String fileName = "nginx-1.16.0.tar.gz";
        FileSystem fs = null;
        String ipAddress = "hdfs://127.0.0.1:9000/";
        Configuration conf = new Configuration();
        try {
            fs = FileSystem.get(new URI(ipAddress), conf, "cy");
        }catch (Exception e){
        }

        String filePath = "/home/cy/Downloads/" + fileName; // "/home/cy/Documents/Practice/nginx-1.16.0.tar.gz";
        File file = new File(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] buffer = null;
        byte[] b = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n;
        try {
            while ((n = inputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0, n);
            }
            buffer = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        long startTime = System.currentTimeMillis();
        String fileHash = Utils.getSHA256(buffer);
//        long endTime = System.currentTimeMillis();
//        System.out.println("hash time: " + (endTime - startTime));

//        String upload_Location = "/hdfs_upload.tar.gz";
//        System.out.println("upload_Location: " + fileName);
        org.apache.hadoop.fs.Path dst = new org.apache.hadoop.fs.Path(ipAddress + fileName);
        FSDataOutputStream os = null;
        try {
            os = fs.create(dst);
            IOUtils.copy(inputStream, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileHash;
    }
}
