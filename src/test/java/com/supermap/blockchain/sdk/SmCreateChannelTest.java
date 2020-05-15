package com.supermap.blockchain.sdk;

import org.junit.Test;

/**
 * @author liqs
 * @version 1.0
 * @date 2020/5/14 18:28
 * 创建 channel 测试
 */
public class SmCreateChannelTest {

    @Test
    public void createChannelTest() throws Exception {
        SmCreateChannel smCreateChannel = new SmCreateChannel();
        smCreateChannel.creatChannel();
    }

}