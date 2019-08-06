package listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.*;
import utils.Tools;

import java.util.HashMap;
import java.util.Map;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        boolean isAllowed = false;
        boolean isAllowedforBot = false;
        String discCommand = "";

        if(event.getMessage().getContentRaw().startsWith(Tools.getPrefix()))
        {
            String[] splitBeheaded = event.getMessage().getContentRaw().split(" ");
            discCommand = splitBeheaded[0];

            if(Tools.getAllowedBotCommands().containsKey(discCommand))
            {
                isAllowedforBot = true;
                isAllowed = false;
            }

            if(Tools.getAllowedUserCommands().containsKey(discCommand))
            {
                isAllowedforBot = false;
                isAllowed = true;
            }

            splitBeheaded = null;
            discCommand = null;
        }


        if (event.getChannelType().toString().contains("TEXT") && !isAllowed && isAllowedforBot)
        {
            if (event.getMessage().getContentRaw().startsWith(Tools.getPrefix()) && event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            {
                try {
                    commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        else if (event.getChannelType().toString().contains("TEXT") && isAllowed && !isAllowedforBot)
        {
            if (event.getMessage().getContentRaw().startsWith(Tools.getPrefix()) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            {
                try {
                    commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}