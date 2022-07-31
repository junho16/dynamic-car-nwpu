package cps.device.dyncar.netty;

import com.onenet.studio.acc.sdk.OpenApi;
import cps.device.dyncar.instance.DynCarSubInfoTask;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Junho
 * @date 2022/7/26 15:53
 */
@Slf4j
@Component
@Order(value = 1)
public class WebSocketServer implements CommandLineRunner {

    @Value("${websocket.server.port}")
    private Integer port;

    @Value("${dyncarNumLimit}")
    private Integer dyncarNumLimit;

    @Resource
    OpenApi OpenApi;

    private void initServer(){
        log.info("正在启动websocket服务器");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss , work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new WebSocketChannelInitializer());

            Channel channel = bootstrap.bind(port).sync().channel();
            log.info("webSocket服务器启动成功，channel：{}" , channel);

            Thread t = new Thread(new DynCarSubInfoTask(dyncarNumLimit , OpenApi));
            t.run();

            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("websocket服务器启动异常：{}", e.getMessage());
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("websocket服务器已关闭");
        }
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        initServer();
    }
}
