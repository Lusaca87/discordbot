package eu.pluginn.bot.listeners;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
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
            String MessageForJoin = "Willkommen im P.L.U.G. - Inn! \n" +
                    "Komm herein, schau Dich um, fühl Dich wohl. Hier ist der Ort, where Players are linked by united Gaming: das ist unsere Community.\n" +
                    "\n" +
                    "Hier findest Du viele verrückte, aber liebenswerte Menschen verschiedener Altersklassen, verschiedener Kulturen und verschiedener Wohnorten, aber mit einer gemeinsamen Leidenschaft: dem Gaming.\n" +
                    "\n" +
                    "Wir stellen unseren Mitgliedern diverse Plattformen zur Kommunikation und zum Austausch zur Verfügung, wie z.B. den Discord, Teamspeak, ein Forum oder auch unterschiedliche Gameserver. Als Gast kannst Du unsere Mitglieder im Discord treffen, Dir stehen allerdings nicht alle Channel und Funktionen zur Verfügung.\n" +
                    "\n" +
                    "Möchtest Du unser gesamtes Angebot nutzen können, so werde Teil unserer Community und registriere Dich im Forum unter http://forum.plug-inn.eu/index.php?willkommen-bei-plug-inn/\n" +
                    "\n" +
                    "Dies bietet Dir außerdem auch die Möglichkeit, Streampartner zu werden. Unsere Streampartner erhalten exklusiven Push über Discord und Forum, so dass die Community immer auf dem Laufenden ist, wer gerade live ist. Außerdem unterstützen unsere Streampartner sich durch Autohostlisten, gegenseitigem Austausch und Hilfe untereinander.\n" +
                    "\n" +
                    "Natürlich bieten wir auch zu allen von uns zur Verfügung gestellten Plattformen entsprechenden Support und Hilfe an. Wende Dich einfach an unser Team!\n" +
                    "\n" +
                    "Durch die Mitgliedschaft kannst Du Kontakt zu den Designern und Entwicklern unserer Community aufnehmen und auch deren Angebot vergünstigt in Anspruch nehmen.\n" +
                    "\n" +
                    "Wir hoffen, unser Angebot immer weiter ausbauen zu können, je weiter unsere Community wächst. Am Besten zusammen mit Dir!\n" +
                    "\n" +
                    "Wir halten unsere Abläufe, Entscheidungen und Planung für unsere Mitglieder transparent und nehmen gerne auch jegliches Feedback zur Verbesserung entgegen. Wir wachsen an der Community und diese wächst an ihren Mitgliedern.\n" +
                    "\n" +
                    "Zusammen bauen wir uns unseren eigenen Gamertreff: das P.L.U.G. - Inn!\n";

            String MemberID = event.getMember().getUser().getId();
            String MemberUserName = event.getMember().getUser().getName();
            String UserString = String.format("`[%s]` %s (%s) hat den Server betreten.", Tools.getCurrentTime(), MemberUserName, MemberID);
            event.getJDA().getTextChannelById("609865389379289092").sendMessage(UserString).queue();
            String UserWelcomeString = String.format("Moin, %s ! Willkommen auf dem P.L.U.G.-Inn Discord. :tada:", event.getMember().getUser().getAsMention());
            event.getJDA().getTextChannelById("603923136060194816").sendMessage(UserWelcomeString).queue();
            if(!event.getMember().getUser().isBot())
            {
               Bot.SendPrivateMessage(MessageForJoin, MemberID);
            }
        }
    }


    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.isWebhookMessage() && event.getTextChannel().getId().equals("603923795299794944")) //SUPPORT!
        {
            String URL = "";

            for(MessageEmbed Mess : event.getMessage().getEmbeds())
            {
                URL = Mess.getUrl();
            }

            String PostID = "";
            String ThreadID = "";

            String[] tempArray;
            String splitter = "index.php";
            tempArray = URL.split(splitter);
            tempArray = tempArray[1].split("/");
            PostID = tempArray[2];
            tempArray = tempArray[1].split("-");
            ThreadID = tempArray[0];
            tempArray = PostID.split("&");
            tempArray = tempArray[1].split("=");
            tempArray = tempArray[1].split("#");
            PostID = tempArray[0];


            Map<String, String> forum = new HashMap<>();
            forum.put("threadID", ThreadID);
            forum.put("postID", PostID);

            String Result = "";
            try {
                Result = Tools.sendPost("http://api.plug-inn.eu/actions.php", "forumsupport", forum);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!Result.equals("None"))
            {
                Result = String.format("http://forum.plug-inn.eu/index.php?thread/%s", Result);
                event.getJDA().getTextChannelById("603923795299794944").sendMessage(String.format("Interner Forenthread:\n%s", Result)).queue();
            }
        }
        else if (!event.getMessage().getContentRaw().startsWith(Bot.getPrefix()) && !event.getAuthor().isBot()) {

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


    public void onGuildMemberLeave(GuildMemberLeaveEvent event)
    {
        String MemberID = event.getMember().getUser().getId();
        String MemberUserName = event.getMember().getUser().getName();
        String UserString = String.format("`[%s]` %s (%s) hat den Server verlassen. (Im Zweifel bitte im AuditLog des Servers kontrollieren.)", Tools.getCurrentTime(), MemberUserName, MemberID);


        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("discordID", MemberID);
        String webResult = "";
        String webResult2 = "";

        try {
            webResult = Tools.sendPost("http://api.plug-inn.eu/actions.php", "checkIfUserWasStreamer", urlParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(webResult.equals("yes_was_streamer"))
        {
            urlParams.clear();
            urlParams.put("discordID", MemberID);
            try {
                webResult2 = Tools.sendPost("http://api.plug-inn.eu/actions.php", "deleteStreamerIfUserLeave", urlParams);
            } catch (Exception e) {
                e.printStackTrace();
            }


            String[] parts;
            parts = webResult2.split(",");
            if(parts[0].equals("deldone"))
            {
                event.getJDA().getTextChannelById("603925441677623307").sendMessage("Streamer " + parts[2] + " wurde entfernt!").queue();
                UserString = String.format("`[%s]` %s (%s) hat den Server verlassen und war Streamer. Wurde aus den StreamProgramm entfernt. (Im Zweifel bitte im AuditLog des Servers kontrollieren.)", Tools.getCurrentTime(), MemberUserName, MemberID);
            }

            event.getJDA().getTextChannelById("609865389379289092").sendMessage(UserString).queue();


        }
    }
}
