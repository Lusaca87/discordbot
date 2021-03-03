package eu.pluginn.bot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdDie implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception
    {
        event.getTextChannel().sendMessage(event.getMessage().getAuthor().getAsMention()+ " Bot wird beendet. :wave:").queue();
        event.getJDA().shutdown();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help(MessageReceivedEvent event) {
        return null;
    }
}
