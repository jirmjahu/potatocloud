package net.potatocloud.node.properties;

import lombok.Getter;
import net.potatocloud.api.property.Property;
import net.potatocloud.api.property.PropertyHolder;
import net.potatocloud.core.networking.NetworkServer;
import net.potatocloud.core.networking.packet.packets.property.PropertyAddPacket;
import net.potatocloud.core.networking.packet.packets.property.PropertyUpdatePacket;
import net.potatocloud.core.networking.packet.packets.property.RequestPropertiesPacket;

import java.util.HashMap;
import java.util.Map;

@Getter
public class NodePropertiesHolder implements PropertyHolder {

    private final NetworkServer server;

    private final Map<String, Property<?>> propertyMap = new HashMap<>();

    public NodePropertiesHolder(NetworkServer server) {
        this.server = server;

        server.on(RequestPropertiesPacket.class, (connection, packet) -> {
            propertyMap.values().forEach(property -> connection.send(new PropertyAddPacket(property)));
        });

        server.on(PropertyAddPacket.class, (connection, packet) -> {
            propertyMap.put(packet.getProperty().getName(), packet.getProperty());

            // Add the property on all other connectors as well
            server.getConnectedSessions().stream()
                    .filter(conn -> !conn.equals(connection))
                    .forEach(conn -> conn.send(packet));
        });

        server.on(PropertyUpdatePacket.class, (connection, packet) -> {
            final Property<?> property = propertyMap.get(packet.getName());
            if (property != null) {
                property.setValueObject(packet.getValue());
            }

            // Update the property on all other connectors as well
            server.getConnectedSessions().stream()
                    .filter(conn -> !conn.equals(connection))
                    .forEach(conn -> conn.send(packet));
        });
    }

    @Override
    public <T> void setProperty(Property<T> property, T value, boolean fireEvent) {
        final Property<T> existing = getProperty(property.getName());
        PropertyHolder.super.setProperty(property, value, fireEvent);

        if (existing == null) {
            // Property was just created, so send the add packet to the connector
            server.generateBroadcast().broadcast(new PropertyAddPacket(property));
        } else {
            // Property was just updated, so send the update packet to the connector
            server.generateBroadcast().broadcast(new PropertyUpdatePacket(property.getName(), value));
        }
    }

    @Override
    public String getPropertyHolderName() {
        return "Global";
    }
}
