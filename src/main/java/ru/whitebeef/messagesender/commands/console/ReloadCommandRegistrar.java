package ru.whitebeef.messagesender.commands.console;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.whitebeef.meridianbot.command.AbstractCommand;
import ru.whitebeef.meridianbot.command.SimpleCommand;
import ru.whitebeef.meridianbot.registry.CommandRegistry;
import ru.whitebeef.messagesender.MessageSender;
import ru.whitebeef.messagesender.MessageType;

@Component
public class ReloadCommandRegistrar {

    @Bean
    public void registerCommand(CommandRegistry commandRegistry, MessageType messageType, MessageSender messageSender) {
        AbstractCommand.builder(messageSender.getInfo().getName().toLowerCase() + ":reload", SimpleCommand.class).setOnCommand(strings -> messageType.loadHJsonFiles()).build().register(commandRegistry);
    }
}
