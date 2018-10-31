package com.aiparker.net.srv;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.aiparker.net.codec.HexDecode;
import com.aiparker.net.codec.HexEncoder;
import com.aiparker.net.common.NativeSupport;
import com.aiparker.net.domain.enums.CodeType;
import com.aiparker.net.event.listener.ChannelEventListener;
import com.aiparker.net.test.EventChannel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 服务端通用方法
 *
 * DefaultCommonSrvAcceptor
 * 
 * @author Hypnos
 * 
 *         2017年1月17日 上午10:30:27
 * 
 * @version 1.0.0
 *
 */
public class DefaultCommonSrvAcceptor extends NettySrvAcceptor {

	private final ChannelEventListener channelEventListener;

	/*
	 * SSLContext
	 */
	private SslContext sslCtx;

	/*
	 * 空闲监控实现
	 */
	private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger(this.nettyEventExecuter);

	/*
	 * 连接监控
	 */
	private final NettyConnetManageHandler connetManage = new NettyConnetManageHandler(this.nettyEventExecuter);

	/*
	 * 读写控制
	 */
	private final NettyReadWriteHandler readWriteHandler = new NettyReadWriteHandler(this.nettyEventExecuter);

	/*
	 * 读空闲时间(秒)
	 */
	private final int Reader_IDLE;
	/*
	 * 写空闲时间(秒)
	 */
	private final int Writer_IDEL;
	/*
	 * 总空闲时间(秒)
	 */
	private final int ALL_IDEL;
	/*
	 * 解码方法
	 */
	private Class<?> decoderClass;
	/*
	 * 编码方法
	 */
	private Class<?> encoderClass;

	/*
	 * 构造|端口号|回调对象
	 */
	public DefaultCommonSrvAcceptor(final int port, final ChannelEventListener channelEventListener) {
		this(port, channelEventListener, null, null, null);
	}

	/*
	 * 端口号|回调对象|解码编码类型|解码|编码
	 */
	public DefaultCommonSrvAcceptor(final int port, final ChannelEventListener channelEventListener,
			final CodeType codeType, Class<?> decoder, Class<?> encoder) {
		this(port, channelEventListener, 90, 20, 120, codeType, decoder, encoder, false);
	}

	/*
	 * 端口号|回调对象|解码编码类型|读超时时间|写超时时间|总超时时间|编码解码类型|解码|编码
	 * 
	 */
	public DefaultCommonSrvAcceptor(final int port, final ChannelEventListener channelEventListener,
			final int Reader_IDLE, final int Writer_IDEL, final int ALL_IDEL, CodeType codeType, Class<?> decoder,
			Class<?> encoder,boolean SSL) {
		super(new InetSocketAddress(port));
		try {
			if (SSL) {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
//				System.out.println(ssc.certificate());
//				System.out.println(ssc.privateKey());
				sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
			} else {
				sslCtx = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.Reader_IDLE = Reader_IDLE;
		this.Writer_IDEL = Writer_IDEL;
		this.ALL_IDEL = ALL_IDEL;
		this.decoderClass = decoder;
		this.encoderClass = encoder;
		this.init();
		this.channelEventListener = channelEventListener;
		this.nettyEventExecuter.start();
	}

	/*
	 * (non-Javadoc) 初始化
	 * 
	 * @see com.aiparker.net.srv.NettySrvAcceptor#init()
	 */
	@Override
	protected void init() {
		super.init();
		/**
		 * backlog参数的含义:
		 * 一个未完成连接的队列，此队列维护着那些已收到了客户端SYN分节信息，等待完成三路握手的连接，socket的状态是SYN_RCVD
		 * .一个已完成的连接的队列，此队列包含了那些已经完成三路握手的连接，socket的状态是ESTABLISHED
		 * backlog参数历史上被定义为上面两个队列的大小之和
		 * 当客户端的第一个SYN到达的时候，TCP会在未完成队列中增加一个新的记录然后回复给客户端三路握手中的第二个分节(服务端的SYN和针对客户端的ACK)
		 * ，这条记录会在未完成队列中一直存在，直到三路握手中的最后一个分节到达，或者直到超时(Berkeley时间将这个超时定义为75秒)
		 * 如果当客户端SYN到达的时候队列已满，TCP将会忽略后续到达的SYN，但是不会给客户端发送RST信息，因为此时允许客户端重传SYN分节，如果返回错误
		 * 信息，那么客户端将无法分清到底是服务端对应端口上没有相应应用程序还是服务端对应端口上队列已满这两种情况
		 */
		bootstrap().option(ChannelOption.SO_BACKLOG, 32768)
				/**
				 * [TCP/IP协议详解]中描述: 当TCP执行一个主动关闭, 并发回最后一个ACK
				 * ,该连接必须在TIME_WAIT状态停留的时间为2倍的MSL.
				 * 这样可让TCP再次发送最后的ACK以防这个ACK丢失(另一端超时并重发最后的FIN).
				 * 这种2MSL等待的另一个结果是这个TCP连接在2MSL等待期间, 定义这个连接的插口对(TCP四元组)不能再被使用.
				 * 这个连接只能在2MSL结束后才能再被使用.
				 *
				 * 许多具体的实现中允许一个进程重新使用仍处于2MSL等待的端口(通常是设置选项SO_REUSEADDR),
				 * 但TCP不能允许一个新的连接建立在相同的插口对上。
				 */
				.option(ChannelOption.SO_REUSEADDR, true)
				/**
				 * SO_REUSEADDR用于对TCP套接字处于TIME_WAIT状态下的socket，可以重复绑定使用
				 */
				.childOption(ChannelOption.SO_REUSEADDR, true)
				/**
				 * 为TCP套接字设置keepalive选项时, 如果在2个小时（实际值与具体实现有关）内在
				 * 任意方向上都没有跨越套接字交换数据, 则 TCP 会自动将 keepalive 探头发送到对端.
				 * 此探头是对端必须响应的TCP段.
				 *
				 * 期望的响应为以下三种之一: 1. 收到期望的对端ACK响应 不通知应用程序(因为一切正常),
				 * 在另一个2小时的不活动时间过后，TCP将发送另一个探头。 2. 对端响应RST 通知本地TCP对端已崩溃并重新启动,
				 * 套接字被关闭. 3. 对端没有响 套接字被关闭。
				 *
				 * 此选项的目的是检测对端主机是否崩溃, 仅对TCP套接字有效.
				 */
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				/**
				 * 对此连接禁用 Nagle 算法. 在确认以前的写入数据之前不会缓冲写入网络的数据. 仅对TCP有效.
				 *
				 * Nagle算法试图减少TCP包的数量和结构性开销, 将多个较小的包组合成较大的包进行发送. 但这不是重点,
				 * 关键是这个算法受TCP延迟确认影响, 会导致相继两次向连接发送请求包, 读数据时会有一个最多达500毫秒的延时.
				 *
				 * 这叫做“ACK delay”, 解决办法是设置TCP_NODELAY。
				 */
				.childOption(ChannelOption.TCP_NODELAY, true)
				/**
				 * 禁用掉半关闭的状态的链接状态 TCP四次握手关闭连接的时候，step2-step3中出现的状态
				 */
				.childOption(ChannelOption.ALLOW_HALF_CLOSURE, false);
	}

	/*
	 * (non-Javadoc) Nio | Epoll 策略
	 * 
	 * @see com.aiparker.net.srv.NettySrvAcceptor#initEventLoopGroup(int,
	 * java.util.concurrent.ThreadFactory)
	 */
	@Override
	protected EventLoopGroup initEventLoopGroup(final int nthread, final ThreadFactory bossFactory) {
		return NativeSupport.isSupportNativeET() ? new NioEventLoopGroup(nthread, bossFactory)
				: new NioEventLoopGroup(nthread, bossFactory);
	}

	/*
	 * (non-Javadoc) 绑定
	 * 
	 * @see com.aiparker.net.srv.NettySrvAcceptor#bind(java.net.SocketAddress)
	 */
	@Override
	protected ChannelFuture bind(final SocketAddress localAddress) {
		final ServerBootstrap boot = bootstrap();
		boot.handler(new LoggingHandler(LogLevel.INFO));
		boot.channel(NioServerSocketChannel.class);
		boot.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(final SocketChannel ch) throws Exception {
				
				 if (sslCtx != null) {
                     ch.pipeline().addLast(sslCtx.newHandler(ch.alloc()));
                 }
				 
				 ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
				/*
				 * 空闲监测配置
				 */
				ch.pipeline().addLast(new IdleStateHandler(Reader_IDLE, Writer_IDEL, ALL_IDEL, TimeUnit.SECONDS));
				/*
				 * 空闲监测实现
				 */
				ch.pipeline().addLast(idleStateTrigger);
				/*
				 * 解密
				 */
				ch.pipeline().addLast("decoder", (ChannelHandler) decoderClass.newInstance());
				/*
				 * 加密
				 */
				ch.pipeline().addLast("encoder", (ChannelHandler) encoderClass.newInstance());

				/*
				 * 连接监控
				 */
				ch.pipeline().addLast(connetManage);

				/*
				 * 读写模块
				 */
				ch.pipeline().addLast(readWriteHandler);

				/*
				 * 总流量监控
				 */
				// ch.pipeline().addLast(trafficHandler);

				/*
				 * 通道流量监控
				 */
				// ch.pipeline().addLast(channelTrafficHandler);

			}

		});
		// 绑定地址
		return boot.bind(localAddress);
	}

	/*
	 * (non-Javadoc) 获得回调函数
	 * 
	 * @see
	 * com.aiparker.net.srv.NettyEventExecuterAcceptor#getChannelEventListener()
	 */
	@Override
	public ChannelEventListener getChannelEventListener() {
		return channelEventListener;
	}

	@Override
	public Map<String, ChannelId> getClient_ip_ChannelID_map() {
		return client_ip_channelID_map;
	}

	public static void main(final String[] args) throws InterruptedException {
		new DefaultCommonSrvAcceptor(8899, new EventChannel(), 90, 15, 120, CodeType.HEX_BYTE, HexDecode.class,
				HexEncoder.class, false).start();
	}
}
