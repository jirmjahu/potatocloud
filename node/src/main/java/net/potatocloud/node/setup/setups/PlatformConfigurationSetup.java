package net.potatocloud.node.setup.setups;

import net.potatocloud.api.platform.Platform;
import net.potatocloud.api.platform.PlatformManager;
import net.potatocloud.node.console.Console;
import net.potatocloud.node.console.Logger;
import net.potatocloud.node.screen.ScreenManager;
import net.potatocloud.node.setup.AnswerResult;
import net.potatocloud.node.setup.Setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlatformConfigurationSetup extends Setup {

    private final PlatformManager platformManager;
    private final Logger logger;

    public PlatformConfigurationSetup(Console console, ScreenManager screenManager, PlatformManager platformManager, Logger logger) {
        super(console, screenManager);
        this.platformManager = platformManager;
        this.logger = logger;
    }

    @Override
    public void initQuestions() {
        question("name")
                .text("What is the name of the platform?")
                .customValidator(input -> {
                    if (platformManager.exists(input)) {
                        return AnswerResult.error("A platform with the same name already exists");
                    }
                    return AnswerResult.success();
                })
                .add();

        question("base")
                .text("What is the base of the platform?")
                .customValidator(input -> {
                    final List<String> supportedBases = List.of("bukkit", "spigot", "paper", "velocity", "limbo");
                    if (!supportedBases.contains(input)) {
                        return AnswerResult.error("This base is not supported");
                    }

                    return AnswerResult.success();
                })
                .suggestions(() -> List.of("bukkit", "spigot", "paper", "velocity", "limbo"))
                .add();
    }

    @Override
    protected void onFinish(Map<String, String> answers) {
        final String name = answers.get("name");
        final String base = answers.get("base");

        // set values based on selected base
        boolean proxy = false;
        String preCache = "";
        List<String> prepareSteps = new ArrayList<>();

        switch (base) {
            case "paper":
                preCache = "paper";
                prepareSteps = List.of("default-files", "eula", "port", "setup-proxy");
                break;
            case "purpur":
                preCache = "purpur";
                prepareSteps = List.of("default-files", "eula", "port", "setup-proxy");
                break;
            case "bukkit", "spigot":
                preCache = null;
                prepareSteps = List.of("default-files", "eula", "port", "setup-proxy");
                break;
            case "velocity":
                proxy = true;
                preCache = null;
                prepareSteps = List.of("default-files", "port", "setup-forwarding");
                break;
            case "limbo":
                preCache = null;
                prepareSteps = List.of("default-files", "port", "setup-proxy");
        }

        final Platform platform = platformManager.createPlatform(name, null, true, proxy, base, preCache, null, null, prepareSteps);
        logger.info("&aTip&8: &7Add a version using&8: &aplatform version add " + platform.getName());
    }

    @Override
    public String getName() {
        return "Platform Configuration";
    }
}
