package net.potatocloud.api.service;

import net.potatocloud.api.group.ServiceGroup;

import java.util.List;

public interface ServiceManager {

    /**
     * Gets a service by its name.
     *
     * @param name the name of the service
     * @return the service
     */
    Service getService(String name);

    /**
     * Gets all services.
     *
     * @return a list of all services
     */
    List<Service> getAllServices();

    /**
     * Gets all online services.
     *
     * @return a list of all online services
     */
    default List<Service> getOnlineServices() {
        return getAllServices().stream().filter(Service::isOnline).toList();
    }

    /**
     * Gets all services in the given group.
     *
     * @param groupName the name of the group
     * @return a list of all services in the given group
     */
    default List<Service> getAllServices(String groupName) {
        return getAllServices().stream().filter(service -> service.getServiceGroup().getName().equals(groupName)).toList();
    }

    /**
     * Gets all online services in the given group.
     *
     * @param groupName the name of the group
     * @return a list of all online services in the given group
     */
    default List<Service> getOnlineServices(String groupName) {
        return getOnlineServices().stream().filter(service -> service.getServiceGroup().getName().equals(groupName)).toList();
    }


    /**
     * Updates an existing service.
     *
     * @param service the service to update
     */
    void updateService(Service service);


    /**
     * Starts a service in the given group
     *
     * @param groupName the name of the group
     */
    void startService(String groupName);

    /**
     * Starts a service in the given group
     *
     * @param group the group
     */
    default void startService(ServiceGroup group) {
        startService(group.getName());
    }

    /**
     * Starts services in the given group
     *
     * @param groupName the name of the group
     * @param amount     the amount of services to start
     */
    void startServices(String groupName, int amount);

    /**
     * Starts services in the given group
     *
     * @param group the group
     * @param amount the amount of services to start
     */
    default void startServices(ServiceGroup group, int amount) {
        startServices(group.getName(), amount);
    }

    /**
     * Gets the current service the api is running on.
     * <p>
     * This only works if the API is used from within a plugin.
     * </p>
     *
     * @return the current service
     */
    Service getCurrentService();

}
