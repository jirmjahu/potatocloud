package net.potatocloud.core.networking.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketManager;

@RequiredArgsConstructor
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final PacketManager packetManager;
    private final NetworkConnection connection;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Packet packet) {
            packetManager.onPacket(connection, packet);
        }
    }
}
