package com.study.javaedge.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author JavaEdge
 */
public class NettyRpcServer extends RpcServer {
	private static Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

	private Channel channel;

	public NettyRpcServer(int port, String protocol, RequestHandler handler) {
		super(port, protocol, handler);
	}

	@Override
	public void start() {
		// 配置服务器
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new ChannelRequestHandler());
						}
					});

			// 启动服务
			ChannelFuture f = b.bind(port).sync();
			logger.info("完成服务端端口绑定与启动");
			channel = f.channel();
			// 等待服务通道关闭
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放线程组资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@Override
	public void stop() {
		this.channel.close();
	}

	private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			logger.info("激活");
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			logger.info("服务端收到消息：" + msg);

			ByteBuf msgBuf = (ByteBuf) msg;
			byte[] req = new byte[msgBuf.readableBytes()];
			msgBuf.readBytes(req);
			// 处理请求,得到响应的字节
			byte[] res = handler.handleRequest(req);
			logger.info("发送响应：" + msg);

			ByteBuf respBuf = Unpooled.buffer(res.length);
			respBuf.writeBytes(res);
			ctx.write(respBuf);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
			ctx.flush();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			// Close the connection when an exception is raised.
			cause.printStackTrace();
			logger.error("发生异常：" + cause.getMessage());
			ctx.close();
		}
	}

}
