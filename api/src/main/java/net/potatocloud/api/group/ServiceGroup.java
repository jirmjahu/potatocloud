package net.potatocloud.api.group;

import net.potatocloud.api.CloudAPI;
import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PlatformVersion;
import net.potatocloud.api.player.CloudPlayer;
import net.potatocloud.api.property.PropertyHolder;
import net.potatocloud.api.service.Service;

import java.util.List;
import java.util.Set;

public interface ServiceGroup extends PropertyHolder {

    /**
     * Gets the name of the group.
     *
     * @return the name of the group
     */
    String getName();

    /**
     * Gets the name of the platform of the group.
     *
     * @return the name of the platform
     */
    String getPlatformName();

    /**
     * Gets the platform of the group as an object.
     *
     * @return the platform
     */
    default Platform getPlatform() {
        return CloudAPI.getInstance().getPlatformManager().getPlatform(getPlatformName());
    }

    /**
     * Gets the name of the platform version of the group.
     *
     * @return the name of the platform version
     */
    String getPlatformVersionName();

    /**
     * Gets the platform version of the group as an object.
     *
     * @return the platform version
     */
    default PlatformVersion getPlatformVersion() {
        return getPlatform().getVersion(getPlatformVersionName());
    }

    /**
     * Gets the list of service templates of the group.
     *
     * @return the list of service templates
     */
    List<String> getServiceTemplates();

    /**
     * Gets the minimum online count of the group.
     *
     * @return the minimum online count
     */
    int getMinOnlineCount();

    /**
     * Sets the minimum online count of the group.
     *
     * @param minOnlineCount the minimum online count
     */
    void setMinOnlineCount(int minOnlineCount);

    /**
     * Gets the maximum online count of the group.
     *
     * @return the maximum online count
     */
    int getMaxOnlineCount();

    /**
     * Sets the maximum online count of the group.
     *
     * @param maxOnlineCount the maximum online count
     */
    void setMaxOnlineCount(int maxOnlineCount);

    /**
     * Gets the online players of the group.
     *
     * @return the online players
     */
    default Set<CloudPlayer> getOnlinePlayers() {
        return CloudAPI.getInstance().getPlayerManager().getOnlinePlayersByGroup(this);
    }

    /**
     * Gets the online player count of the group.
     *
     * @return the online player count
     */
    default int getOnlinePlayerCount() {
        return getOnlinePlayers().size();
    }

    /**
     * Gets the maximum players of the group.
     *
     * @return the maximum players
     */
    int getMaxPlayers();

    /**
     * Sets the maximum players of the group.
     *
     * @param maxPlayers the maximum players
     */
    void setMaxPlayers(int maxPlayers);

    /**
     * Gets the maximum memory of the group.
     *
     * @return the maximum memory
     */
    int getMaxMemory();

    /**
     * Sets the maximum memory of the group.
     *
     * @param maxMemory the maximum memory
     */
    void setMaxMemory(int maxMemory);

    /**
     * Gets the fallback status of the group.
     *
     * @return the fallback status
     */
    boolean isFallback();

    /**
     * Sets the fallback status of the group.
     *
     * @param fallback the fallback status
     */
    void setFallback(boolean fallback);

    /**
     * Gets the static status of the group.
     *
     * @return the static status
     */
    boolean isStatic();

    /**
     * Gets the start priority of the group.
     *
     * @return the start priority
     */
    int getStartPriority();

    /**
     * Sets the start priority of the group.
     *
     * @param startPriority the start priority
     */
    void setStartPriority(int startPriority);

    /**
     * Gets the start percentage of the group.
     *
     * @return the start percentage
     */
    int getStartPercentage();

    /**
     * Sets the start percentage of the group.
     *
     * @param startPercentage the start percentage
     */
    void setStartPercentage(int startPercentage);

    /**
     * Gets the java command used to start services of the group.
     *
     * @return the java command
     */
    String getJavaCommand();

    /**
     * Gets the custom jvm flags of the group.
     *
     * @return the custom jvm flags
     */
    List<String> getCustomJvmFlags();

    /**
     * Adds a custom jvm flag to the group.
     *
     * @param flag the custom jvm flag
     */
    void addCustomJvmFlag(String flag);

    /**
     * Adds a service template to the group.
     *
     * @param template the service template
     */
    void addServiceTemplate(String template);

    /**
     * Removes a service template from the group.
     *
     * @param template the service template
     */
    void removeServiceTemplate(String template);

    /**
     * Gets all services of the group.
     *
     * @return the list of services
     */
    default List<Service> getAllServices() {
        return CloudAPI.getInstance().getServiceManager().getAllServices(getName());
    }

    /**
     * Gets the online services of the group.
     *
     * @return the list of online services
     */
    default List<Service> getOnlineServices() {
        return CloudAPI.getInstance().getServiceManager().getOnlineServices(getName());
    }

    /**
     * Gets the online service count of the group.
     *
     * @return the online service count
     */
    default int getOnlineServiceCount() {
        return getOnlineServices().size();
    }

    /**
     * Updates the group.
     */
    default void update() {
        CloudAPI.getInstance().getServiceGroupManager().updateServiceGroup(this);
    }
}
