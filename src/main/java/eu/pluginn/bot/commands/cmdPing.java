package eu.pluginn.bot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdPing implements Command {



    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        event.getTextChannel().sendMessage("Pong!").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("Ping wurde ausgeführt");
    }

    @Override
    public String help(MessageReceivedEvent event) {
        System.out.println("Ping Help");
        return null;
    }
}
