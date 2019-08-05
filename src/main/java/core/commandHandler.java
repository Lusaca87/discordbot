package core;

import commands.Command;

import java.util.HashMap;

public class commandHandler {

    public static final commandParser parser = new commandParser();
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) throws Exception {

        if (commands.containsKey(cmd.invoke)) {

            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if (!safe)
            {
                //System.out.println("Länge der Argumente: " + cmd.args.length);

                if(cmd.args.length > 0 && cmd.args[0].contains("help"))
                {
                    commands.get(cmd.invoke).help(cmd.event);
                }
                else
                {
                    commands.get(cmd.invoke).action(cmd.args, cmd.event);
                    commands.get(cmd.invoke).executed(safe, cmd.event);
                }
            }
            else
            {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }

        }
        else
        {
            System.out.println("[ERROR] Command " + cmd.invoke + " existiert nicht.");
        }

    }

}