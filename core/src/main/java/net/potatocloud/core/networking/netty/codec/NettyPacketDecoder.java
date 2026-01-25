package net.potatocloud.core.networking.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import net.potatocloud.core.networking.netty.PacketBuffer;
import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.networking.packet.PacketManager;
import net.potatocloud.core.networking.packet.PacketToBigException;

import java.util.List;

@RequiredArgsConstructor
public class NettyPacketDecoder extends ByteToMessageDecoder {

    private final PacketManager packetManager;

    private static final int MAX_PACKET_SIZE = 65536;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();

        // Read packet length and stop if too big
        final int length = in.readInt();
        if (length > MAX_PACKET_SIZE) {
            ctx.close();
            throw new PacketToBigException(length);
        }

        // Wait until the full packet is received
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        final int packetId = in.readInt();
        final Packet packet = packetManager.createPacket(packetId);
        if (packet == null) {
            return;
        }

        // Let the packet read its content
        packet.read(new PacketBuffer(in));

        out.add(packet);
    }
}
