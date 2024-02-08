package ru.whitebeef.messagesender.commands.console;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.whitebeef.meridianbot.command.AbstractCommand;
import ru.whitebeef.meridianbot.command.SimpleCommand;
import ru.whitebeef.meridianbot.registry.CommandRegistry;
import ru.whitebeef.messagesender.MessageSender;
import ru.whitebeef.messagesender.MessageType;

@Component
public class ReloadCommandRegistrar {

    private final CommandRegistry commandRegistry;
    private final MessageType messageType;
    private final MessageSender messageSender;

    public ReloadCommandRegistrar(CommandRegistry commandRegistry, MessageType messageType, MessageSender messageSender) {
        this.commandRegistry = commandRegistry;
        this.messageType = messageType;
        this.messageSender = messageSender;
    }

    @PostConstruct
    public void registerCommand() {
        AbstractCommand.builder(messageSender.getInfo().getName().toLowerCase() + ":reload", SimpleCommand.class).setOnCommand(strings -> messageType.loadHJsonFiles()).build().register(commandRegistry);
    }
}
