package ru.whitebeef.messagesender;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.GenericApplicationContext;
import ru.whitebeef.beefcore.plugin.BeefPlugin;
import ru.whitebeef.beefcore.plugin.PluginClassLoader;
import ru.whitebeef.beefcore.plugin.PluginInfo;


@Slf4j
public class MessageSender extends BeefPlugin {
    @Getter
    private static MessageSender instance;

    public MessageSender(@NotNull PluginInfo info, PluginClassLoader pluginClassLoader, GenericApplicationContext applicationContext) {
        super(info, pluginClassLoader, applicationContext);
        instance = this;
    }

    @Override
    public void onEnable() {
        log.info("Enabled!");
    }

}
