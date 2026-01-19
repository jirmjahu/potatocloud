package net.potatocloud.node.command.arguments;

import net.potatocloud.api.service.Service;
import net.potatocloud.node.Node;
import net.potatocloud.node.command.ArgumentType;

import java.util.List;

public class ServiceArgument extends ArgumentType<Service> {

    public ServiceArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<Service> parse(String input) {
        final Service service = Node.getInstance()
                .getServiceManager()
                .getService(input);

        if (service == null) {
            return ParseResult.error("&cNo service found with the name &a" + input);
        }

        return ParseResult.success(service);
    }

    @Override
    public List<String> suggest(String input) {
        return Node.getInstance()
                .getServiceManager()
                .getAllServices()
                .stream()
                .map(Service::getName)
                .filter(name -> name.startsWith(input))
                .toList();
    }
}
