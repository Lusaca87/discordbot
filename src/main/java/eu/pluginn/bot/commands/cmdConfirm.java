package eu.pluginn.bot.commands;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class cmdConfirm implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

        String result = "";
        String combinedID = String.format("%s|||%s", event.getAuthor().getId(), event.getAuthor().getAsTag());
        System.out.println("T:" + combinedID);

        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("username", args[0]);
        urlParam.put("discordid", combinedID);

        result = Tools.sendPost(Bot.buildCustomPhpUrl("confirm.php"), "confirmDiscord", urlParam);
        System.out.println("Result: " + result);

        if(!result.equals("")) {
            String[] tempArray;
            String splitter = "Ξ";
            tempArray = result.split(splitter);

            if (tempArray[0].equals("done")) {
                String ForenUserName = tempArray[1];
                Member member = event.getJDA().getGuildById("585570094265139223").getMemberById(event.getAuthor().getId());
                Role role = event.getJDA().getRoleById("585571208163229737");
                event.getJDA().getGuildById("585570094265139223").getController().addSingleRoleToMember(member, role).complete();
                String WisperMessage = String.format("Dein Forenbenutzername '%s' wurde mit diesem Discordkonto erfolgreich verknüpft. Vielen Dank", ForenUserName);
                Bot.SendPrivateMessage(WisperMessage, event.getAuthor().getId(), event);
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
