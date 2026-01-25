package net.potatocloud.core.networking;

import net.potatocloud.core.networking.packet.Packet;
import net.potatocloud.core.utils.Closeable;

import java.util.UUID;

public interface NetworkConnection extends Closeable {

    UUID getId();

    void send(Packet packet);

}
