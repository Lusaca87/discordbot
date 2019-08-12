package eu.pluginn.bot.commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class cmdClear implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

        if(args.length > 1 || args.length == 0 )
        {
            event.getTextChannel().sendMessage("Falscher Wert. !clear <Zahl>").queue();

        }else if(args.length == 1)
        {
            try
            {
                int numberOfMessagesToDelete = Integer.parseInt(args[0]);
                MessageHistory History = event.getChannel().getHistoryBefore(event.getMessage().getId(), numberOfMessagesToDelete).complete();
                event.getMessage().delete().queue();
                for (Message mess : History.getRetrievedHistory())
                {
                    mess.delete().queue();
                }
            }
            catch (Exception ex)
            {
                event.getTextChannel().sendMessage("Falscher Wert. !clear <Zahl>").queue();
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help(MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("!clear <Zahl>").queue();
        return null;
    }
}
