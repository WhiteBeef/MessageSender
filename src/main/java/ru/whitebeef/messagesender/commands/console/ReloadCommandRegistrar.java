package ru.whitebeef.messagesender.commands.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.whitebeef.meridianbot.command.AbstractCommand;
import ru.whitebeef.meridianbot.command.SimpleCommand;
import ru.whitebeef.meridianbot.registry.CommandRegistry;
import ru.whitebeef.messagesender.MessageSender;
import ru.whitebeef.messagesender.MessageType;

@Component
public class ReloadCommandRegistrar {


    @Autowired
    public ReloadCommandRegistrar(CommandRegistry commandRegistry, MessageType messageType, MessageSender messageSender) {
        AbstractCommand.builder(messageSender.getInfo().getName().toLowerCase() + ":reload", SimpleCommand.class).setOnCommand(strings -> messageType.loadHJsonFiles()).build().register(commandRegistry);

    }
}
