package eu.pluginn.bot.commands;

import eu.pluginn.bot.core.Bot;
import eu.pluginn.bot.utils.Tools;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("username", args[0]);
        urlParam.put("discordid", combinedID);

        result = Tools.sendPost(Bot.buildCustomPhpUrl("confirm.php"), "confirmDiscord", urlParam);

        if(!result.equals("")) {
            String[] tempArray;
            String splitter = "Ξ";
            tempArray = result.split(splitter);

            if (tempArray[0].equals("done")) {
                String ForenUserName = tempArray[1];

                    String ID = event.getAuthor().getId();
                    Role role = event.getJDA().getRoleById("609833008773464085");

                    if(Bot.isUserInRole(ID, "716981066371432478"))
                    {
                        Bot.deleteRoleFromUser(ID, "716981066371432478", event);
                    }

                    Bot.addRoleToUser(ID, role.getId(), event);
                    String WisperMessage = String.format("Dein Forenbenutzername '%s' wurde mit diesem Discordkonto erfolgreich verknüpft. Vielen Dank", ForenUserName);
                    Bot.SendPrivateMessage(WisperMessage, event.getAuthor().getId());
               // }
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
