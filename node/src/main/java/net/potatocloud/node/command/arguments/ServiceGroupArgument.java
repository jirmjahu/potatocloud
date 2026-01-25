package net.potatocloud.node.command.arguments;

import net.potatocloud.api.group.ServiceGroup;
import net.potatocloud.node.Node;
import net.potatocloud.node.command.ArgumentType;

import java.util.List;

public class ServiceGroupArgument extends ArgumentType<ServiceGroup> {

    public ServiceGroupArgument(String name) {
        super(name);
    }

    @Override
    public ParseResult<ServiceGroup> parse(String input) {
        final ServiceGroup group = Node.getInstance()
                .getServiceGroupManager()
                .getServiceGroup(input);

        if (group == null) {
            return ParseResult.error("Group &a" + input + " &7does &cnot &7exist");
        }

        return ParseResult.success(group);
    }

    @Override
    public List<String> suggest(String input) {
        return Node.getInstance()
                .getServiceGroupManager()
                .getAllServiceGroups()
                .stream()
                .map(ServiceGroup::getName)
                .filter(name -> name.startsWith(input))
                .toList();
    }
}
