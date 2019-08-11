package eu.pluginn.bot.listeners;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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




}
