package simple.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import simple.handler.EchoClientHandler;

import java.net.InetSocketAddress;

/**
 * Created by tianjian on 2017/7/24.
 */
public class EchoClient {
    private final String host;

    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(port)).handler(new ChannelInitializer<SocketChannel>()  {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new EchoClientHandler());
            }
        });

        ChannelFuture f = null;
        try {
            f = b.connect().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if(args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + "<host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new EchoClient(host, port).start();
    }
}
