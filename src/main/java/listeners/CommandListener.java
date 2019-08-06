package listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.*;
import utils.Tools;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        boolean isAllowd = true;


        if (event.getChannelType().toString().contains("TEXT") && isAllowd) {
            if (event.getMessage().getContentRaw().startsWith(Tools.getPrefix()) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {


                try {
                    commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw(), event));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
}