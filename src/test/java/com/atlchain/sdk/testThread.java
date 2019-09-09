package com.atlchain.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class RunnableDemo implements Runnable {
    private Thread t;
    private String threadName;
    private int count;
    private ATLChain atlChain;
    private List<String> list = new ArrayList<String>();

    RunnableDemo(String name, int count) {
        threadName = name;
        this.count = count;
        File networkFile = new File(this.getClass().getResource("/network-config-test.yaml").getPath());
        atlChain = new ATLChain(networkFile);
//        System.out.println("Creating " +  threadName );
    }

    public void run() {
//        System.out.println("Running " +  threadName );
        long startTime = System.currentTimeMillis();
        int loop = 20000;
        for(int i = count; i < count + loop; i++) {
            String key = "tkey" + i;
            try {
                byte[][] result = atlChain.queryByte(
                        "bcgiscc",
                        "GetRecordByKey",
                        new byte[][]{key.getBytes()}
                );
                list.add(new String(result[0]));
//                for (byte[] res : result) {
//                    System.out.println(key + ": " + new String(res));
//                    list.add(new String(res));
//                }
//                Thread.sleep(1);
//                Assert.assertNotEquals("", result);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        list.sort(Comparator.naturalOrder());
//        System.out.println(list);

        long endTime = System.currentTimeMillis();
        System.out.println("endTime:" + endTime);
//        System.out.println(threadName + "=>Time: " + (endTime - startTime) + "ms ,TPS: " + 200.0*1000/(endTime - startTime));
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
//        System.out.println("Creating " +  threadName );
    }

    public void run() {
//        System.out.println("Running " +  threadName );
        long startTime = System.currentTimeMillis();
        int loop = 20000;
        for(int i = count; i< count + loop; i++) {
            String key = "tkey" + i;
            try {
                String result = atlChain.invokeByte(
                        "bcgiscc",
                        "PutRecordBytes",
                        new byte[][]{key.getBytes(), ("value" + i).getBytes()}
                );
//                System.out.println(i + ": " + result);
//                Assert.assertNotEquals("", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("endTime:" + endTime);
//        System.out.println(threadName + "=>Time: " + (endTime - startTime) + "ms ,TPS: " + 200.0*1000/(endTime - startTime));
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
    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        System.out.println("startTime:" + startTime);
        for (int i = 0; i < 1; i++) {
            RunnableDemoWrite R = new RunnableDemoWrite("Thread-" + i, i * 50);
//            RunnableDemo R = new RunnableDemo("Thread-" + i, i * 20000);
            R.start();
        }

//        long endTime = System.currentTimeMillis();
//        System.out.println("Time: " + (endTime - startTime) + "ms ,TPS: " + 1000.0* 1000/(endTime - startTime));
    }
}
