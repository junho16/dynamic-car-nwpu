package cps.device.dyncar.netty;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.trace.Trace;
import cps.device.dyncar.entity.TracePos;
import cps.device.dyncar.instance.DynCarInstance;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

/**
 * @author Junho
 * @date 2022/7/26 15:57
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Value("${websocket.server.ip}")
    private String ip;

    @Value("${websocket.server.port}")
    private Integer port;

    @Value("${websocket.server.suffix}")
    private String suffix;

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("收到消息："+msg);
        if (msg instanceof FullHttpRequest){
            //以http请求形式接入，但是处理为websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof  WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg );
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress());
        //添加连接
        log.debug("客户端加入连接："+ctx.channel());
        DynCarChannelManager.addChannelId(ctx.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.debug("客户端断开连接："+ctx.channel());
        DynCarChannelManager.removeChannelId(ctx.channel());
        DynCarChannelManager.removeChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){

        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.debug("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        String positionInfo = ((TextWebSocketFrame) frame).text();
        log.debug("服务端收到：" + positionInfo);
        try {
            TracePos tp = JSONObject.parseObject(positionInfo , TracePos.class);
            String userid = DynCarChannelManager.findUserId(ctx.channel());
            DynCarInstance.getDynCarMap().get(userid).add(tp);
        }catch (Exception e){
            log.error("系统中未找到此用户的信息（channel/trace）！");
        }



//        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString()
//                + ctx.channel().id() + "：" + request);
//        // 群发
//        DynCarChannelManager.send2All(tws);
//        // 返回【谁发的发给谁】
//        //ctx.channel().writeAndFlush(tws);
    }

    /**
     * 唯一的一次http请求，用于创建websocket
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        String uri = req.uri();
        DynCarChannelManager.addChannel(uri , ctx.channel());
        DynCarInstance.getDynCarMap().put(uri , new ConcurrentLinkedQueue<>());

        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(
                ctx,
                req,
                new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.BAD_REQUEST
                )
            );
            return;
        }
        String websocketURL = "ws://" + ip + ":" + port + suffix;
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                websocketURL , null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     * @param ctx
     * @param req
     * @param res
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer( res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
