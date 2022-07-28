package cps.device.dyncar.netty;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 通道管理 单例获取
 * @author Junho
 * @date 2022/7/26 15:29
 */
public class DynCarChannelManager {

    private static ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 在建立连接 即 channelActive 时记录通道id
     */
    private static ConcurrentMap<String, ChannelId> ChannelIdMap = new ConcurrentHashMap();

    /**
     * 在建立连接后首次通信 即 创建websocket时记录channel
     * key:userid ; value:channel
     */
    private static ConcurrentMap<String, Channel> ChannelMap = new ConcurrentHashMap();

    public static ConcurrentMap<String, Channel> getChannelMap(){
        return ChannelMap;
    }

    /**
     * 在建立连接后首次通信 即 创建websocket时 逆向记录channel
     * key:channel ; value:userid
     */
    private static ConcurrentMap<Channel,String> UserMap = new ConcurrentHashMap();

    public static void addChannel(String userKey , Channel channel){
        ChannelMap.put(userKey , channel);
        UserMap.put(channel , userKey);
    }

    public static void removeChannel(Channel userChannel){
        String userKey = UserMap.get(userChannel);
        ChannelMap.remove(userKey);
        UserMap.remove(userChannel);
    }

    public static Channel findChannel(String userKey){
        return ChannelMap.get(userKey);
    }

    public static String findUserId(Channel userChannel){
        return UserMap.get(userChannel);
    }

    public static void addChannelId(Channel channel){
        GlobalGroup.add(channel);
        ChannelIdMap.put(channel.id().asShortText(),channel.id());
    }

    public static void removeChannelId(Channel channel){
        GlobalGroup.remove(channel);
        ChannelIdMap.remove(channel.id().asShortText());
    }

    public static Channel findChannelId(String id){
        return GlobalGroup.find(ChannelIdMap.get(id));
    }

    /**
     * 群发消息
     * @param tws
     */
    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
}
