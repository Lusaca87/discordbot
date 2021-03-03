package eu.pluginn.bot.listeners;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Moderation extends ListenerAdapter {


    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (Bot.getLogJoinLeaveEvents()) {
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
            event.getJDA().getGuildById("585570094265139223").addRoleToMember(event.getMember(), event.getGuild().getRoleById("716981066371432478")).complete(); // Gast Rolle zuweisen.
            event.getJDA().getTextChannelById("603923136060194816").sendMessage(UserWelcomeString).queue();
            if (!event.getMember().getUser().isBot()) {
                Bot.SendPrivateMessage(MessageForJoin, MemberID);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.isWebhookMessage() && event.getTextChannel().getId().equals("603923795299794944")) //SUPPORT!
        {
            String URL = "";

            for (MessageEmbed Mess : event.getMessage().getEmbeds()) {
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
                Result = Tools.sendPost("https://api.plug-inn.eu/actions.php", "forumsupport", forum);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!Result.equals("None")) {
                Result = String.format("https://forum.plug-inn.eu/index.php?thread/%s", Result);
                event.getJDA().getTextChannelById("603923795299794944").sendMessage(String.format("Interner Forenthread:\n%s", Result)).queue();
            }
        } else if (!event.getMessage().getContentRaw().startsWith(Bot.getPrefix()) && !event.getAuthor().isBot()) {

            if (!Bot.getNonModeratedChannel().containsKey(event.getChannel().getId())) {

                Map<String, String> discLog = new HashMap<>();

                String AttachmentURL = "";
                String UserNickname = "";

                Member m = event.getJDA().getGuildById(Bot.getDiscordID()).retrieveMemberById(event.getAuthor().getId()).complete();
                UserNickname = Optional.ofNullable(m.getNickname()).orElse(event.getAuthor().getName());

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

                m = null;
                try {
                    Tools.sendPost("https://api.plug-inn.eu/actions.php", "discordLog", discLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void onMessageUpdate(MessageUpdateEvent event) {
        Map<String, String> discEditLog = new HashMap<>();

        discEditLog.put("DiscordMessageID", event.getMessage().getId());
        discEditLog.put("Message", event.getMessage().getContentRaw());


        try {
            Tools.sendPost("https://api.plug-inn.eu/actions.php", "discordLogEdit", discEditLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        User user = event.getUser();
        String MemberID = user.getId();
        String MemberUserName = user.getName();
        String UserString = String.format("`[%s]` %s (%s) hat den Server verlassen. (Im Zweifel bitte im AuditLog des Servers kontrollieren.)", Tools.getCurrentTime(), MemberUserName, MemberID);


        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("discordID", MemberID);
        String webResult = "";
        String webResult2 = "";

        try {
            webResult = Tools.sendPost("https://api.plug-inn.eu/actions.php", "checkIfUserWasStreamer", urlParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (webResult.equals("yes_was_streamer")) {
            urlParams.clear();
            urlParams.put("discordID", MemberID);
            try {
                webResult2 = Tools.sendPost("https://api.plug-inn.eu/actions.php", "deleteStreamerIfUserLeave", urlParams);
            } catch (Exception e) {
                e.printStackTrace();
            }


            String[] parts;
            parts = webResult2.split(",");
            if (parts[0].equals("deldone")) {
                event.getJDA().getTextChannelById("603925441677623307").sendMessage("Streamer " + parts[2] + " wurde entfernt!").queue();
                UserString = String.format("`[%s]` %s (%s) hat den Server verlassen und war Streamer. Wurde aus den StreamProgramm entfernt. (Im Zweifel bitte im AuditLog des Servers kontrollieren.)", Tools.getCurrentTime(), MemberUserName, MemberID);
            }

            event.getJDA().getTextChannelById("609865389379289092").sendMessage(UserString).queue();

        } else {
            event.getJDA().getTextChannelById("609865389379289092").sendMessage(UserString).queue();
        }
    }


    //Temporär deaktiviert
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
       /*  if(event.getChannelJoined().getId().equals("716982646319874088"))
       {
            boolean hasRole = false;
            for (Role roles: event.getMember().getRoles()) {
                if(roles.getId().equals("609833008773464085")) { //Member Rolle.
                    hasRole = false;
                }
            }

            if(!hasRole){
                String channelNameToCreateOrCheck = event.getMember().getUser().getId();
                Role role = event.getJDA().getRoleById("585570094265139223");

                long allowed = 66560;
                long denied = 871823697;
                long allowedEveryone = 0;
                long deniedEveryone = 805829713;
                String newChannelID = "";

                if(event.getGuild().getTextChannelsByName(channelNameToCreateOrCheck, true).size() == 0) {
                    event.getGuild().createTextChannel(channelNameToCreateOrCheck)
                            .setParent(event.getGuild().getCategoryById("603924545853849620")) // Support Kategorie
                            .addPermissionOverride(event.getMember(), allowed, denied) // Rechte für den User selbst
                            .addPermissionOverride(role, allowedEveryone, deniedEveryone) // Rechte für Everyonme
                            .addPermissionOverride(event.getGuild().getRoleById("585570922610556943"), allowed, denied) // Rechte für Support.
                            .setTopic(String.format("Textchannel für %s zur Synchronisation.", event.getMember().getUser().getAsTag()))
                            .queue();

                    newChannelID = GetChannel(channelNameToCreateOrCheck, event);

                    String tokenResult = "";
                    Map<String, String> discToken = new HashMap<>();
                    discToken.put("tokenType", "1");
                    discToken.put("details", event.getMember().getUser().getId());
                    discToken.put("channelID", newChannelID);

                    try {
                        tokenResult = Tools.sendPost("https://www.plug-inn.eu/actions.php", "getToken", discToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String mentionUser = event.getMember().getAsMention();
                    String Website = "https://www.plug-inn.eu/index.php?site=controllcp";
                    String channelMessage = String.format("Hallo %s!\nUm deine Synchronisierung für Discord abzuschließen gib das folgende Token auf der Webseite in das Feld ein:\n\nDein Discord Token: %s\n\nWebseite: %s\n\nBeachte bitte dazu das du im Forum registriert sein musst und auf der Seite eingeloggt.\n\nMit freundlichen Grüßen\nDein Team von P.L.U.G.-Inn", mentionUser, tokenResult, Website);


                    event.getGuild().getTextChannelById(newChannelID).sendMessage(channelMessage).queue();

                    tokenResult = "";
                    newChannelID = "";
                    channelMessage = "";

                }
            }
            
        } */
    }


    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if(event.getChannelJoined().getId().equals("716982646319874088"))
        {
            String tokenResult = "";
            Map<String, String> discToken = new HashMap<>();
            discToken.put("tokenType", "1");
            discToken.put("details", event.getMember().getUser().getId());

            try {
                tokenResult = Tools.sendPost("https://www.plug-inn.eu/actions.php", "getToken", discToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(tokenResult);
            System.out.println("ID: " + event.getMember().getUser().getId());
            System.out.println("CHannelID: "+ event.getChannelJoined().getId());
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        if(!event.getMember().getUser().isBot())
        {
            if(event.getChannel().getId().equals("809094589528277032"))
            {
                String UserID = event.getMember().getUser().getId();
                JSONObject webJson = null;
                String UserNickname = "";
                Member m = event.getJDA().getGuildById(Bot.getDiscordID()).retrieveMemberById(UserID).complete();
                UserNickname = Optional.ofNullable(m.getNickname()).orElse(event.getMember().getUser().getName());


                if(event.getReactionEmote().toString().equals("RE:U+2705"))
                {
                    Map<String, String> urlParams = new HashMap<>();
                    urlParams.put("whitelistModeratorUserID", UserID);
                    urlParams.put("whitelistModeratorUserName", UserNickname);
                    urlParams.put("whitelistStatus", "yes");
                    urlParams.put("ModMessageID", event.getMessageId());
                    String webResult = null;
                    try {
                        webResult = Tools.sendPost(Bot.buildCustomPhpUrl("actions.php"), "editWhiteList", urlParams);
                        //System.out.println(webResult);

                        webJson = new JSONObject(webResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    urlParams.clear();
                    Tools.handleWhiteList(webJson, event);
                }


                if(event.getReactionEmote().toString().equals("RE:U+1f595"))
                {
                    Map<String, String> urlParams = new HashMap<>();
                    urlParams.put("whitelistModeratorUserID", UserID);
                    urlParams.put("whitelistModeratorUserName", UserNickname);
                    urlParams.put("whitelistStatus", "wrongInfos");
                    urlParams.put("ModMessageID", event.getMessageId());
                    String webResult = null;
                    try {
                        webResult = Tools.sendPost(Bot.buildCustomPhpUrl("actions.php"), "editWhiteList", urlParams);
                        webJson = new JSONObject(webResult);
                        //System.out.println(webResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    urlParams.clear();
                    Tools.handleWhiteList(webJson, event);

                }



                if(event.getReactionEmote().toString().equals("RE:U+274c"))
                {
                    Map<String, String> urlParams = new HashMap<>();
                    urlParams.put("whitelistModeratorUserID", UserID);
                    urlParams.put("whitelistModeratorUserName", UserNickname);
                    urlParams.put("whitelistStatus", "no");
                    urlParams.put("ModMessageID", event.getMessageId());
                    String webResult = null;
                    try {
                        webResult = Tools.sendPost(Bot.buildCustomPhpUrl("actions.php"), "editWhiteList", urlParams);
                        webJson = new JSONObject(webResult);
                        //System.out.println(webResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    urlParams.clear();
                    Tools.handleWhiteList(webJson, event);

                }
                webJson = null;
                UserNickname = null;
                UserID = null;
            }
        }
    }

    //Temporär deaktiviert.
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        /*if(event.getRoles().get(0).getId().equals("716981066371432478"))
        {
            try{
                if(event.getMember().getVoiceState().getChannel().getId().equals("716982646319874088")){
                    event.getGuild().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelById("609870015327633408")).complete();
                }
            } catch (Exception ex)
            {
                //Reserved
            }

        }*/
    }

    /**
     * Funktion um die ChannelID von einem DiscordChannel zu bekommen.
     * @param channelName der Name des Channels der gesucht ist.
     * @param event interenes Discord event.
     * @return Die ID des Channels als String.
     */
    private String GetChannel(String channelName, Event event)
    {
        boolean noID = true;
        String ID = "";
        try {
            TimeUnit.SECONDS.sleep(1);
            ID = event.getJDA().getGuildById(Bot.getDiscordID()).getTextChannelsByName(channelName, true).get(0).getId();
            noID = false;
        }catch (Exception ex){
            noID = true;
        }

        if(noID)
        {
           ID = GetChannel(channelName, event);
        }
        return ID;
    }
}
