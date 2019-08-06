package core;

import commands.cmdConfig;
import commands.cmdPing;
import listeners.CommandListener;
import net.dv8tion.jda.core.*;
import utils.Tools;
import utils.UrlData;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static JDABuilder builder;

    public static void main(String[] args) throws Exception
    {
        //Erzeuge eine neue Map in der die Post Parameter gespeichert werden.
        Map<String, String> UrlParams = new HashMap<>();
        UrlParams.put("application", "discord");
        UrlParams.put("task", "getDiscordToken");

        //Sende die Postdaten an die Adresse und verarbeite den Inhalt in der Variabe.
        String DiscordToken = "";
        DiscordToken = UrlData.SendPost(Tools.buildCustomPhpUrl("actions.php"), "getConfig", UrlParams);

        //Erstelle Discord Bot und anschließenden start.
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(DiscordToken);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);

        Tools.AddUserCommand("!ping");
        Tools.AddBotCommand("!config");


        addListeners();
        addCommands();

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addListeners()
    {
        builder.addEventListener(new CommandListener());
    }

    private static void addCommands()
    {
        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("config", new cmdConfig());
    }
}
