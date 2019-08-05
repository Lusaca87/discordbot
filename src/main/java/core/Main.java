package core;

import net.dv8tion.jda.core.*;
import utils.Tools;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDABuilder builder;

    public static void main(String[] args)
    {
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken("DISCORDTOKEN");
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
