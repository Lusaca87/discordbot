package eu.pluginn.bot.commands;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class cmdTicket implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        if(args.length > 0)
        {
            String messageToSend = "";

            for(int i = 0;i<args.length;i++)
            {
                messageToSend = String.format("%s%s ", messageToSend, args[i]);
            }

            //smessageToSend = messageToSend.replace("'", "\\'");
            //messageToSend = messageToSend.replace("\"", "\\\"");

            String UserName = Optional.ofNullable(event.getJDA().getGuildById(Bot.getDiscordID()).getMemberById(event.getAuthor().getId()).getNickname()).orElse(event.getAuthor().getName());
            String UserTag = event.getAuthor().getAsTag();
            String UserID = event.getAuthor().getId();

            JSONObject jsonToSend = new JSONObject();
            jsonToSend.put("discordUserID", UserID);
            jsonToSend.put("discordUserTag", UserTag);
            jsonToSend.put("discordUserName", UserName);
            jsonToSend.put("discordMessage", messageToSend);

            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("discParams", jsonToSend.toString());
            String ticketResult = Tools.sendPost("http://api.plug-inn.eu/actions.php", "discsupport", urlParams);

            if(ticketResult.equals("supportDone"))
            {
                String ResponseMessage = "Dein Ticket wurde an unseren Support übermittelt. Ein Supporter wird sich so bald als möglich bei Dir per Privatnachricht im Discord melden, bitte habe solange noch etwas Geduld. \n" +
                        "\n" +
                        "Vielen Dank, Dein Team von P.L.U.G.-Inn";
                Bot.SendPrivateMessage(ResponseMessage, UserID);
            }
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
