package eu.pluginn.bot.core;

import eu.pluginn.bot.commands.*;
import eu.pluginn.bot.listeners.CommandListener;
import eu.pluginn.bot.listeners.Moderation;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.Event;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Bot {

    private static String homepageUrl = "http://www.plug-inn.eu";
    private static String Splitter = "Ξ";
    private static String prefix = "!";

    private static String botOwnerID =  "217644884671201281";

    private static String discordID =  "585570094265139223";

    private static Map<String, String> allowedBotCommands = new HashMap<>();
    private static Map<String, String> allowedUserCommands = new HashMap<>();

    private static Map<String, String> allowedStaffCommands = new HashMap<>();
    private static Map<String, String> allowedAdminCommands = new HashMap<>();

    private static JDABuilder builder = null;
    private static JDA jda = null;

    private static boolean LogJoinLeaveEvents = false;



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

    public static JDA getJda()
    {
        return jda;
    }

    private static void setJda(JDA pJda)
    {
        jda = pJda;
    }

    public static JDABuilder getBuilder()
    {
        return builder;
    }

    private static void setBuilder(JDABuilder pBuilder)
    {
        builder = pBuilder;
    }

    private static void getBotConfig()
    {
        setLogJoinLeaveEvents(true);

    }

    // Getter
    public static boolean getLogJoinLeaveEvents()
    {
        return LogJoinLeaveEvents;
    }
    public static String getDiscordID() { return  discordID; }


    // Setter
    public static void setLogJoinLeaveEvents(boolean logEvents)
    {
        LogJoinLeaveEvents = logEvents;
    }
    public static void setDiscordID(String discServerID) { discordID = discServerID; }


    public static void restart(boolean forceRestart) throws Exception
    {

        getBotConfig();

        JDA botJda = getJda();
        if(botJda != null)
        {
            if(forceRestart)
            {
                botJda.shutdown();
                setJda(null);
                setBuilder(null);
                botJda = getJda();
            }
        }


        //Erzeuge eine neue Map in der die Post Parameter gespeichert werden.
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("application", "discord");
        urlParams.put("task", "getDiscordToken");

        //Sende die Postdaten an die Adresse und verarbeite den Inhalt in der Variabe.
        String discordToken = "";
        discordToken = Tools.sendPost(Bot.buildCustomPhpUrl("actions.php"), "getConfig", urlParams);



        //Erstelle Discord Bot und anschließenden start.
        JDABuilder botBuilder = getBuilder();
        if(botBuilder == null)
        {
            botBuilder = new JDABuilder(AccountType.BOT);
            botBuilder.setToken(discordToken);
            botBuilder.setAutoReconnect(true);
            botBuilder.setStatus(OnlineStatus.ONLINE);
            setBuilder(botBuilder);
        }

        //Lösche alle Commands das keine doppelt vorhanden sind.
        deleteAllCommands();

        //Setze die Commands.
        addUserCommand(String.format("%sping", Bot.getPrefix()));

        addBotCommand(String.format("%sconfig", Bot.getPrefix()));
        addBotCommand(String.format("%sforceBotRestart", Bot.getPrefix()));

        addStaffCommand(String.format("%stwitch", Bot.getPrefix()));

        addAdminCommand(String.format("%srestart", Bot.getPrefix()));
        addAdminCommand(String.format("%sclear", Bot.getPrefix()));


        addListeners();
        addCommands();

        try {

            if(botJda == null)
            {
                botJda = builder.buildBlocking();
            }
            setJda(botJda);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addListeners()
    {
        getBuilder().addEventListener(new CommandListener());
        getBuilder().addEventListener(new Moderation());
    }

    private static void addCommands()
    {
        //MUSTHAVE COMMAND
        commandHandler.commands.put("confirm", new cmdConfirm());


        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("config", new cmdConfig());
        commandHandler.commands.put("restart", new cmdRestart());
        commandHandler.commands.put("forceBotRestart", new cmdBotRestart());
        commandHandler.commands.put("clear", new cmdClear());
        commandHandler.commands.put("twitch", new cmdTwitch());
    }

    private static void deleteAllCommands()
    {
        allowedBotCommands.clear();
        allowedUserCommands.clear();
        allowedStaffCommands.clear();
        allowedAdminCommands.clear();
    }

    public static void SendPrivateMessage(String pMessage, String UserID)
    {
            Bot.getJda().getUserById(UserID).openPrivateChannel().queue(privateChannel ->
            {
                privateChannel.sendMessage(pMessage).queue();
            });
    }

    public static String getPlainID(String ID) {
        String DiscordID = "";
        if (ID.startsWith("<@!") && ID.endsWith(">")) {
            String Temp = ID;
            Temp = Temp.substring(0, ID.length() - 1);
            Temp = Temp.substring(3, Temp.length());
            DiscordID = Temp;
            Temp = null;
        }
        else if (ID.startsWith("<@&") && ID.endsWith(">"))
        {
            String Temp = ID;
            Temp = Temp.substring(0, ID.length() - 1);
            Temp = Temp.substring(3, Temp.length());
            DiscordID = Temp;
            Temp = null;
        }
        else if (ID.startsWith("<@") && ID.endsWith(">"))
        {
            String Temp = ID;
            Temp = Temp.substring(0, ID.length() - 1);
            Temp = Temp.substring(2, Temp.length());
            DiscordID = Temp;
            Temp = null;
        }
        else
        {
            DiscordID = "none";
        }


        return DiscordID;
    }

    public static boolean isUserInRole(String discordUserID, String discordRoleIDToCheck)
    {
       return Bot.getJda().getGuildById(Bot.getDiscordID()).getMemberById(discordUserID).getRoles().contains(Bot.getJda().getRoleById(discordRoleIDToCheck));
    }

    public static void addRoleToUser(String UserID, String RoleID, Event event)
    {
        //boolean status = false;
        Member member = event.getJDA().getGuildById(Bot.getDiscordID()).getMemberById(UserID);
        Role role = event.getJDA().getRoleById(RoleID);
        event.getJDA().getGuildById(Bot.getDiscordID()).getController().addSingleRoleToMember(member, role).complete();
        //return true;
    }

    public static void deleteRoleFromUser(String UserID, String RoleID, Event event)
    {
        //boolean status = false;
        Member member = event.getJDA().getGuildById(Bot.getDiscordID()).getMemberById(UserID);
        Role role = event.getJDA().getRoleById(RoleID);
        event.getJDA().getGuildById(Bot.getDiscordID()).getController().removeSingleRoleFromMember(member, role).complete();
        //return true;
    }


}
