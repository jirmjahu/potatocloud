package net.potatocloud.core.networking.packet;

import net.potatocloud.core.networking.NetworkConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class PacketManager {

    private final Map<Integer, Supplier<? extends Packet>> packets = new HashMap<>();
    private final Map<Class<? extends Packet>, List<PacketListener<? extends Packet>>> listeners = new HashMap<>();

    public void register(int id, Supplier<? extends Packet> packet) {
        packets.put(id, packet);
    }

    public <T extends Packet> void on(Class<T> packetClass, PacketListener<T> listener) {
        listeners.computeIfAbsent(packetClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public Packet createPacket(int id) {
        return packets.get(id).get();
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> void onPacket(NetworkConnection connection, T packet) {
        if (connection == null) {
            throw new IllegalStateException("Connection is null");
        }

        final List<PacketListener<? extends Packet>> packetListeners = listeners.get(packet.getClass());
        if (packetListeners == null) {
            return;
        }

        for (PacketListener<? extends Packet> listener : packetListeners) {
            ((PacketListener<T>) listener).onPacket(connection, packet);
        }
    }
}
