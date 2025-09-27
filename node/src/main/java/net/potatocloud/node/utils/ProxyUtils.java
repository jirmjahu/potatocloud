package net.potatocloud.node.utils;

import lombok.experimental.UtilityClass;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.api.property.Property;
import net.potatocloud.node.Node;

import java.util.List;

@UtilityClass
public class ProxyUtils {

    public ServiceGroup getProxyGroup() {
        return getProxyGroups().stream().findFirst().orElse(null);
    }

    public List<ServiceGroup> getProxyGroups() {
        return Node.getInstance().getGroupManager().getAllServiceGroups().stream().filter(group -> group.getPlatform().isProxy()).toList();
    }

    public boolean isProxyModernForwarding() {
        if (getProxyGroup() == null) {
            return false;
        }
        final Property property = getProxyGroup().getProperty("velocity_modern_forwarding");
        if (property == null) {
            return false;
        }

        return (boolean) property.getValue();
    }
}
