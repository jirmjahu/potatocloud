package net.potatocloud.node.utils;

import lombok.experimental.UtilityClass;
import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.node.Node;

@UtilityClass
public class ProxyUtils {

    public ServiceGroup getProxyGroup() {
        return Node.getInstance().getGroupManager().getAllServiceGroups().stream().filter(group -> group.getPlatform().isProxy()).findFirst().orElse(null);
    }

    public boolean isProxyModernForwarding() {
        return Boolean.parseBoolean((String) getProxyGroup().getProperty("potatocloud_velocity_modern").getValue());
    }
}
