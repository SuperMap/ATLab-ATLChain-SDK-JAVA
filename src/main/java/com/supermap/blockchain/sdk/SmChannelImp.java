package com.supermap.blockchain.sdk;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.logging.Logger;

public class SmChannelImp implements SmChannel {

    Logger logger = Logger.getLogger(SmChannelImp.class.getName());

    private HFClient hfClient;
    private Channel channel;

    public SmChannelImp(HFClient hfClient, Channel channel) {
        this.hfClient = hfClient;
        this.channel = channel;
    }

    @Override
    public SmBlockchainInfo getBlockchainInfo() {
        SmBlockchainInfo smBlockChainInfo = new SmBlockchainInfo();
        BlockchainInfo blockchainInfo = null;
        try {
            blockchainInfo = channel.queryBlockchainInfo(channel.getPeers().iterator().next());
            smBlockChainInfo.setCurrentBlockHash(Hex.encodeHexString(blockchainInfo.getCurrentBlockHash()));
            smBlockChainInfo.setPreviousBlockHash(Hex.encodeHexString(blockchainInfo.getPreviousBlockHash()));
            smBlockChainInfo.setHeight(blockchainInfo.getHeight());
        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return smBlockChainInfo;
    }

    @Override
    public SmBlockInfo getBlockInfoByNumber(Peer peer, long number) {
        BlockInfo blockInfo = null;
        SmBlockInfo smBlockInfo = new SmBlockInfo();
        try {
            blockInfo = channel.queryBlockByNumber(peer, number);
            smBlockInfo.setCurrentBlockHash(Hex.encodeHexString(blockInfo.getDataHash()));
            smBlockInfo.setPreviousBlockHash(Hex.encodeHexString(blockInfo.getPreviousHash()));
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }
        return smBlockInfo;
    }

    @Override
    public Set<String> listChannelOfPeerJoined(Peer peer) {
        Set<String> channelSet = null;
        peer = channel.getPeers().iterator().next();
        try {
            channelSet = hfClient.queryChannels(peer);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }
        return channelSet;
    }

    @Override
    public void update() {
    }
}
