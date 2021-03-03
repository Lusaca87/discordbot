package eu.pluginn.bot.commands;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class cmdWhitelist  implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        if(event.getMessage().getTextChannel().getId().equals("809095634694307848")) {
            String DiscordMessage = ""; // Message which is send
            String UserNickname = "";
            String PostUserID = event.getMessage().getAuthor().getId(); //Person which creates the message.



            for(String m : args){
                DiscordMessage = String.format("%s %s", DiscordMessage, m);
            }

            if(DiscordMessage.startsWith(" ")){
                DiscordMessage = DiscordMessage.substring(1);
            }

            event.getMessage().delete().queue();

            String ModMessage = String.format("%s \nNeuer Whitelist-Antrag von %s:\n```%s```\n:white_check_mark: Antrag erlauben \n:x: Antrag ablehnen\n:middle_finger: Falsche Angaben \n:no_entry: Antrag bearbeitet (wird automatisch gesetzt!)\nDer User wird je nach extra benachrichtigt.", event.getGuild().getRoleById("585570922610556943").getAsMention(), event.getAuthor().getAsMention(), DiscordMessage);
            String ModMessageID = event.getJDA().getTextChannelById("809094589528277032").sendMessage(ModMessage).complete().getId();

            event.getJDA().getTextChannelById("809094589528277032").addReactionById(ModMessageID, "U+2705").queue(); //Yes
            event.getJDA().getTextChannelById("809094589528277032").addReactionById(ModMessageID, "U+274c").queue(); //No
            event.getJDA().getTextChannelById("809094589528277032").addReactionById(ModMessageID, "U+1f595").queue(); //Wrong Information

            Member m = event.getJDA().getGuildById(Bot.getDiscordID()).retrieveMemberById(event.getAuthor().getId()).complete();
            UserNickname = Optional.ofNullable(m.getNickname()).orElse(event.getAuthor().getName());

            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("discordUserid", PostUserID);
            urlParams.put("discordUserName", UserNickname);
            urlParams.put("origMessage", DiscordMessage);
            urlParams.put("ModMessageID", ModMessageID);

            Tools.sendPost(Bot.buildCustomPhpUrl("actions.php"), "insertWhiteList", urlParams);
            urlParams.clear();


            String PrivateMessageForUser = String.format(Tools.readFileLinebyLine("whiteListEntry.txt"), UserNickname, DiscordMessage);
            Bot.SendPrivateMessage(PrivateMessageForUser, PostUserID);

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
