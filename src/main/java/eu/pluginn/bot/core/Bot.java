package eu.pluginn.bot.core;

import eu.pluginn.bot.commands.*;
import eu.pluginn.bot.listeners.CommandListener;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Bot {

    private static String homepageUrl = "http://www.plug-inn.eu";
    private static String Splitter = "Ξ";
    private static String prefix = "!";

    private static String botOwnerID =  "217644884671201281";

    private static Map<String, String> allowedBotCommands = new HashMap<>();
    private static Map<String, String> allowedUserCommands = new HashMap<>();

    private static Map<String, String> allowedStaffCommands = new HashMap<>();
    private static Map<String, String> allowedAdminCommands = new HashMap<>();

    private static JDABuilder builder = null;
    private static JDA jda = null;


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

    private static JDA getJda()
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

    public static void setBuilder(JDABuilder pBuilder)
    {
        builder = pBuilder;
    }



    public static void restart(boolean forceRestart) throws Exception
    {
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
        addAdminCommand(String.format("%srestart", Bot.getPrefix()));


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
    }

    private static void addCommands()
    {
        //MUSTHAVE COMMAND
        commandHandler.commands.put("confirm", new cmdConfirm());


        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("config", new cmdConfig());
        commandHandler.commands.put("restart", new cmdRestart());
        commandHandler.commands.put("forceBotRestart", new cmdBotRestart());
    }

    private static void deleteAllCommands()
    {
        allowedBotCommands.clear();
        allowedUserCommands.clear();
        allowedStaffCommands.clear();
        allowedAdminCommands.clear();
    }


}
