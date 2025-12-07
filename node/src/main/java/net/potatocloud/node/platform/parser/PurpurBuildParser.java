package net.potatocloud.node.platform.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.potatocloud.api.platform.PlatformVersion;
import net.potatocloud.api.platform.impl.PlatformVersionImpl;
import net.potatocloud.node.platform.BuildParser;
import net.potatocloud.node.utils.RequestUtil;

public class PurpurBuildParser implements BuildParser {

    @Override
    public void parse(PlatformVersion version, String baseUrl) {
        try {
            String versionName = version.getName();

            // Find the latest minecraft version if the user wants the latest
            if (versionName.equalsIgnoreCase("latest")) {
                final JsonArray versionsArray = RequestUtil.request("https://api.purpurmc.org/v2/purpur/").getAsJsonArray("versions");

                versionName = versionsArray.get(versionsArray.size() - 1).getAsString();
            }

            // Get the latest build of the chosen version
            final JsonObject versionInfo = RequestUtil.request("https://api.purpurmc.org/v2/purpur/" + versionName);
            final String latestBuildName = versionInfo.getAsJsonObject("builds").get("latest").getAsString();
            final JsonObject latestBuild = RequestUtil.request("https://api.purpurmc.org/v2/purpur/" + versionName + "/" + latestBuildName);

            final String md5 = latestBuild.get("md5").getAsString();

            // Replace placeholders in the platform download URL
            final String downloadUrl = baseUrl
                    .replace("{version}", versionName)
                    .replace("{build}", latestBuildName);

            if (version instanceof PlatformVersionImpl impl) {
                impl.setFileHash(md5);
                impl.setDownloadUrl(downloadUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "purpur";
    }
}
