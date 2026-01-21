package net.potatocloud.connector.properties;

import lombok.Getter;
import net.potatocloud.api.property.Property;
import net.potatocloud.api.property.PropertyHolder;
import net.potatocloud.core.networking.NetworkClient;
import net.potatocloud.core.networking.packet.packets.property.PropertyAddPacket;
import net.potatocloud.core.networking.packet.packets.property.PropertyUpdatePacket;
import net.potatocloud.core.networking.packet.packets.property.RequestPropertiesPacket;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ConnectorPropertiesHolder implements PropertyHolder {

    private final NetworkClient client;

    private final Map<String, Property<?>> propertyMap = new HashMap<>();

    public ConnectorPropertiesHolder(NetworkClient client) {
        this.client = client;

        client.send(new RequestPropertiesPacket());

        client.on(PropertyAddPacket.class, (connection, packet) -> {
            propertyMap.put(packet.getProperty().getName(), packet.getProperty());
        });

        client.on(PropertyUpdatePacket.class, (connection, packet) -> {
            final Property<?> property = propertyMap.get(packet.getName());
            if (property != null) {
                property.setValueObject(packet.getValue());
            }
        });
    }

    @Override
    public <T> void setProperty(Property<T> property, T value, boolean fireEvent) {
        final Property<T> existing = getProperty(property.getName());
        PropertyHolder.super.setProperty(property, value, fireEvent);

        if (existing == null) {
            // Property was just created, so send the add packet to the node
            client.send(new PropertyAddPacket(property));
        } else {
            // Property was just updated, so send the update packet to the node
            client.send(new PropertyUpdatePacket(property.getName(), value));
        }
    }

    @Override
    public String getPropertyHolderName() {
        return "Global";
    }
}
