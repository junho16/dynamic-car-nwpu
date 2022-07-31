package cps.device.dyncar.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Junho
 * @date 2022/7/31 16:30
 */
@Slf4j
public class HreatBeatServerHandler extends ChannelInboundHandlerAdapter  {

    private int times;

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
//        if ("I am alive".equals(o)) {
//            String user = DynCarChannelManager.findUserId(channelHandlerContext.channel());
//            log.info("{}:I am alive" , user);
////            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("over", CharsetUtil.UTF_8));
//        }
//    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String user = DynCarChannelManager.findUserId(ctx.channel());

        //判断evt是否是IdleStateEvent(用于触发用户事件，包含读空闲/写空闲/读写空闲)
        if (evt instanceof IdleState) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("{}:进入读空闲..." , user);
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("{}:进入写空闲..." , user);
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.info("{}:进入读写空闲..." , user);
            }

            times++;
            if (times > 60) {
                log.info("{}:空闲次数已超过60次 服务端关闭连接" , user);
                DynCarChannelManager.removeChannel(ctx.channel());
                DynCarChannelManager.removeChannelId(ctx.channel());
                ctx.channel().close();
            }
            //super.userEventTriggered(ctx, evt);
        }
    }


}
