package eu.pluginn.bot.utils;

import java.util.HashMap;
import java.util.Map;

public class Tools {

    private static String homepageUrl = "http://www.plug-inn.eu";
    private static String Splitter = "Ξ";
    private static String prefix = "!";

    private static String botOwnerID =  "217644884671201281";

    private static Map<String, String> allowedBotCommands = new HashMap<>();
    private static Map<String, String> allowedUserCommands = new HashMap<>();

    private static Map<String, String> allowedStaffCommands = new HashMap<>();
    private static Map<String, String> allowedAdminCommands = new HashMap<>();


    public static void addBotCommand(String command)
    {
        allowedBotCommands.put(command, "yes");
    }

    public static void addUserCommand(String command)
    {
        allowedUserCommands.put(command, "yes");
    }

    public static void addStaffCommand(String command)
    {
        allowedStaffCommands.put(command, "yes");
    }

    public static void addAdminCommand(String command)
    {
        allowedAdminCommands.put(command, "yes");
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
        return allowedBotCommands;
    }

    public static Map<String, String> getAllowedUserCommands()
    {
        return allowedUserCommands;
    }

    public static Map<String, String> getAllowedStaffCommands()
    {
        return allowedStaffCommands;
    }

    public static Map<String, String> getAllowedAdminCommands()
    {
        return allowedAdminCommands;
    }

    public static String getBotOwnerID() { return botOwnerID; }

}
