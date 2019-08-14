package eu.pluginn.bot.listeners;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Moderation extends ListenerAdapter
{
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        if(Bot.getLogJoinLeaveEvents())
        {
            String MessageForJoin = "Message folgt!";

            String MemberID = event.getMember().getUser().getId();
            String MemberUserName = event.getMember().getUser().getName();
            String UserString = String.format("`[%s]` %s (%s) hat den Server betreten.", Tools.getCurrentTime(), MemberUserName, MemberID);
            event.getJDA().getTextChannelById("609865389379289092").sendMessage(UserString).queue();
            String UserWelcomeString = String.format("Moin, %s ! Willkommen auf dem P.L.U.G.-Inn Discord. :tada:", event.getMember().getUser().getAsMention());
            event.getJDA().getTextChannelById("603923136060194816").sendMessage(UserWelcomeString).queue();
            if(!event.getMember().getUser().isBot())
            {
               //Bot.SendPrivateMessage(MessageForJoin, MemberID);
            }
        }
    }


    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.getMessage().getContentRaw().startsWith(Bot.getPrefix()) && !event.getAuthor().isBot()) {

            if (!Bot.getNonModeratedChannel().containsKey(event.getChannel().getId())) {

                Map<String, String> discLog = new HashMap<>();

                String AttachmentURL = "";
                String UserNickname = "";
                UserNickname = Optional.ofNullable(event.getJDA().getGuildById(Bot.getDiscordID()).getMemberById(event.getAuthor().getId()).getNickname()).orElse(event.getAuthor().getName());

                if (event.getMessage().getAttachments().size() >= 1) {
                    AttachmentURL = event.getMessage().getAttachments().get(0).getProxyUrl();
                }

                discLog.put("DiscordMessageID", event.getMessage().getId());
                discLog.put("Message", event.getMessage().getContentRaw());
                discLog.put("OldMessage", "");
                discLog.put("Attachment", AttachmentURL);
                discLog.put("Channel", event.getMessage().getChannel().getName());
                discLog.put("ChannelID", event.getMessage().getChannel().getId());
                discLog.put("UnixTimestamp", String.format("%s", Tools.getUnixTimestamp()));
                discLog.put("ServerUsername", UserNickname);
                discLog.put("UserName", event.getAuthor().getAsTag());
                discLog.put("DiscordUserID", event.getAuthor().getId());

                try {
                    Tools.sendPost("http://api.plug-inn.eu/actions.php", "discordLog", discLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void onMessageUpdate(MessageUpdateEvent event)
    {
        Map<String, String> discEditLog = new HashMap<>();

        discEditLog.put("DiscordMessageID", event.getMessage().getId());
        discEditLog.put("Message", event.getMessage().getContentRaw());


        try {
                Tools.sendPost("http://api.plug-inn.eu/actions.php", "discordLogEdit", discEditLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
