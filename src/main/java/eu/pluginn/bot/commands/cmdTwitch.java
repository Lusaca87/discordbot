package eu.pluginn.bot.commands;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class cmdTwitch implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

        String action = args[0];
        String streamName = args[1];

        switch (action)
        {
            case "add":
                String DiscordID = Bot.getPlainID(args[2]);

                if(Bot.isUserInRole(DiscordID, "609833008773464085"))
                {
                    Map<String, String> urlParams = new HashMap<>();
                    urlParams.put("discordID", DiscordID);
                    String webResult = Tools.sendPost(Bot.buildCustomPhpUrl("actions.php"), "getTs3ID", urlParams);
                    urlParams.clear();

                    if(!webResult.equals("null"))
                    {
                        urlParams.put("ts3ID", webResult);
                        urlParams.put("discordUserID", DiscordID);
                        urlParams.put("streamName", streamName);
                    }
                    else
                    {
                        urlParams.put("ts3ID", "none");
                        urlParams.put("discordUserID", DiscordID);
                        urlParams.put("streamName", streamName);
                    }

                    String twitchResult = Tools.sendPost("https://api.plug-inn.eu/actions.php", "addStreamer", urlParams);

                    String[] parts;
                    parts = twitchResult.split(",");


                    if(parts[0].equals("done_without_ts"))
                    {
                        Bot.addRoleToUser(DiscordID, "585572127646679298", event);
                        event.getTextChannel().sendMessage("Streamer " + parts[1] + " wurde ohne Teamspeak Verknüpfung hinzugefügt.").queue();
                    }
                    else if(parts[0].equals("done_with_ts"))
                    {
                        Bot.addRoleToUser(DiscordID, "585572127646679298", event);
                        event.getTextChannel().sendMessage("Streamer " + parts[1] + " wurde hinzugefügt.").queue();
                    }
                    else
                    {
                        event.getTextChannel().sendMessage("Streamer " + streamName + " wurde nicht hinzugefügt!\nBitte an Delta wenden!").queue();
                    }
                }
                else
                {
                    event.getTextChannel().sendMessage(String.format("Der Benutzer '%s' hat keine Rolle als Mitglied.\nMöglicherweise wurde sein Konto nicht mit dem Forum authentifiziert?", Bot.getJda().getUserById(DiscordID).getName())).queue();
                }
                break;
            case "delete":
                Map<String, String> urlParams = new HashMap<>();
                urlParams.put("streamName", streamName);
                String twitchResult = Tools.sendPost("https://api.plug-inn.eu/actions.php", "deleteStreamer", urlParams);

                String[] parts;
                parts = twitchResult.split(",");
                if(parts[0].equals("deldone"))
                {
                    Bot.deleteRoleFromUser(parts[1], "585572127646679298", event);
                    event.getTextChannel().sendMessage("Streamer " + parts[2] + " wurde entfernt!").queue();
                }
                break;
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help(MessageReceivedEvent event) {
        return null;
    }
}
