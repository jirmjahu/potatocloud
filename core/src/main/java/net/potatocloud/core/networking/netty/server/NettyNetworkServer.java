package net.potatocloud.core.networking.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.netty.NettyUtils;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketListener;
import net.potatocloud.core.networking.packet.PacketManager;
import net.potatocloud.core.networking.packet.PacketRegistry;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@RequiredArgsConstructor
public class NettyNetworkServer implements NetworkServer {

    private final PacketManager packetManager;
    private final List<NetworkConnection> connectedSessions = new CopyOnWriteArrayList<>();

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;
    private int port;

    @Override
    public void start(String hostname, int port) {
        this.port = port;
        PacketRegistry.registerPackets(packetManager);

        bossGroup = NettyUtils.createEventLoopGroup();
        workerGroup = NettyUtils.createEventLoopGroup();

        channel = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new NettyNetworkServerInitializer(packetManager, this))
                .bind(new InetSocketAddress(hostname, port)).syncUninterruptibly().channel();
    }

    @Override
    public void close() {
        for (NetworkConnection session : connectedSessions) {
            session.close();
        }
        channel.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public boolean isRunning() {
        return channel != null && channel.isActive();
    }

    @Override
    public <T extends Packet> void on(Class<T> packetClass, PacketListener<T> listener) {
        packetManager.on(packetClass, listener);
    }

    @Override
    public void send(NetworkConnection connection, Packet packet) {
        connection.send(packet);
    }
}
