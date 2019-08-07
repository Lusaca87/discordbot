package eu.pluginn.bot.core;

import eu.pluginn.bot.commands.cmdConfig;
import eu.pluginn.bot.commands.cmdPing;
import eu.pluginn.bot.listeners.CommandListener;
import net.dv8tion.jda.core.*;
import org.json.JSONArray;
import org.json.JSONObject;
import eu.pluginn.bot.utils.Tools;
import eu.pluginn.bot.utils.UrlData;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static JDABuilder builder;

    public static void main(String[] args) throws Exception
    {
        //Erzeuge eine neue Map in der die Post Parameter gespeichert werden.
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("application", "discord");
        urlParams.put("task", "getDiscordToken");

        //Sende die Postdaten an die Adresse und verarbeite den Inhalt in der Variabe.
        String discordToken = "";
        discordToken = UrlData.sendPost(Tools.buildCustomPhpUrl("actions.php"), "getConfig", urlParams);


        //TEST für Json Not final.
        String restresponse = UrlData.getPlainSite(Tools.buildCustomPhpUrl("jsontest.php"));
        JSONArray jsonarray = new JSONArray(restresponse);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String username= jsonobject.getString("username");
            String user_fname= jsonobject.getString("user_fname");
            String user_work= jsonobject.getString("user_work");
        }











        //Erstelle Discord Bot und anschließenden start.
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(discordToken);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);

        Tools.addUserCommand("!ping");
        Tools.addBotCommand("!config");


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
