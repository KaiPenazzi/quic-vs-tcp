package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.quic.QuicChannel;
import io.netty.handler.codec.quic.QuicClientCodecBuilder;
import io.netty.handler.codec.quic.QuicSslContext;
import io.netty.handler.codec.quic.QuicSslContextBuilder;
import io.netty.handler.codec.quic.QuicStreamChannel;
import io.netty.handler.codec.quic.QuicStreamType;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public final class App {

    public static void main(String[] args) throws Exception {
        QuicSslContext context = QuicSslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .applicationProtocols("quic").build();

        EventLoopGroup group = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());

        try {
            ChannelHandler codec = new QuicClientCodecBuilder()
                    .sslContext(context)
                    .maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
                    .initialMaxData(10000000)
                    .initialMaxStreamDataBidirectionalLocal(1000000)
                    .build();

            Bootstrap bs = new Bootstrap();
            Channel channel = bs.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(codec)
                    .bind(0).sync().channel();

            QuicChannel quicChannel = QuicChannel.newBootstrap(channel)
                    .streamHandler(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            ctx.close();
                        }
                    })
                    .remoteAddress(new InetSocketAddress(NetUtil.LOCALHOST4, 7000))
                    .connect()
                    .get();

            QuicStreamChannel streamChannel = quicChannel.createStream(QuicStreamType.BIDIRECTIONAL,
                    new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            ByteBuf byteBuf = (ByteBuf) msg;
                            System.err.println(byteBuf.toString(CharsetUtil.US_ASCII));
                            byteBuf.release();
                        }

                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                            if (evt == ChannelInputShutdownReadComplete.INSTANCE) {
                                ((QuicChannel) ctx.channel().parent()).close(true, 123,
                                        ctx.alloc().directBuffer(16)
                                                .writeBytes("customError".getBytes()));
                            }
                        }
                    }).sync().getNow();

            streamChannel.writeAndFlush(Unpooled.copiedBuffer("send msg", CharsetUtil.US_ASCII))
                    .addListener(QuicStreamChannel.SHUTDOWN_OUTPUT);

            streamChannel.closeFuture().sync();
            quicChannel.closeFuture().sync();
            channel.close().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
