package ru.whitebeef.messagesender;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.GenericApplicationContext;
import ru.whitebeef.meridianbot.plugin.BeefPlugin;
import ru.whitebeef.meridianbot.plugin.PluginClassLoader;
import ru.whitebeef.meridianbot.plugin.PluginInfo;

@Slf4j
public class MessageSender extends BeefPlugin {

    public MessageSender(@NotNull PluginInfo info, PluginClassLoader pluginClassLoader, GenericApplicationContext applicationContext) {
        super(info, pluginClassLoader, applicationContext);
    }

    @Override
    public void onEnable() {
        log.info("Enabled!");
    }
}
