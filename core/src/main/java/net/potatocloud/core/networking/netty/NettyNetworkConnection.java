package net.potatocloud.core.networking.netty;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.potatocloud.core.networking.NetworkConnection;
import net.potatocloud.core.networking.packet.Packet;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NettyNetworkConnection implements NetworkConnection {

    private final UUID id = UUID.randomUUID();

    private final Channel channel;

    @Override
    public void send(Packet packet) {
        channel.writeAndFlush(packet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NettyNetworkConnection other = (NettyNetworkConnection) obj;
        return channel.id().equals(other.channel.id());
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }

    @Override
    public void close() {
        channel.close();
    }
}
