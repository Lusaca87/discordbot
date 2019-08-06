package utils;

import java.util.HashMap;
import java.util.Map;

public class Tools {

    private static String homepageUrl = "http://www.plug-inn.eu";
    private static String Splitter = "Ξ";
    private static String prefix = "!";

    private static Map<String, String> AllowedBotCommands = new HashMap<>();
    private static Map<String, String> AllowedUserCommands = new HashMap<>();

    public static void AddBotCommand(String command)
    {
        AllowedBotCommands.put(command, "yes");
    }

    public static void AddUserCommand(String command)
    {
        AllowedUserCommands.put(command, "yes");
    }



    public static String getHomepageUrl()
    {
        return homepageUrl;

    }

    public static String buildCustomPhpUrl(String pPhpfile)
    {
        return String.format("%s/%s", getHomepageUrl(), pPhpfile);
    }

    public static String getSplitter()
    {
        return Splitter;
    }

    public static String getPrefix() { return prefix; }

    public static Map<String, String> getAllowedBotCommands()
    {
        return AllowedBotCommands;
    }

    public static Map<String, String> getAllowedUserCommands()
    {
        return AllowedUserCommands;
    }

}
