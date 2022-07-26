package cps.device.dyncar.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Junho
 * @date 2022/7/26 15:55
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${websocket.server.ip}")
    private String ip;

    @Value("${websocket.server.port}")
    private Integer port;

    @Value("${websocket.server.suffix}")
    private String suffix;

    private final String WEB_SOCKET_LINKURL = "ws://" + ip + ":" + port + suffix;

    @Override
    protected void initChannel(SocketChannel ch) {

        //log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));

        ch.pipeline().addLast("http-codec",new HttpServerCodec());

        //聚合器，使用websocket会用到
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));

        //用于大数据的分区传输
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());

        ch.pipeline().addLast("handler",new WebSocketHandler());

//        ch.pipeline().addLast(new WebSocketServerProtocolHandler(WEB_SOCKET_LINKURL, null, true, 10485760));

    }

}
