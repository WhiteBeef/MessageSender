package ru.whitebeef.messagesender;


import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import ru.whitebeef.meridianbot.plugin.BeefPlugin;
import ru.whitebeef.meridianbot.plugin.PluginClassLoader;
import ru.whitebeef.meridianbot.plugin.PluginInfo;

@Log4j2
public class MessageSender extends BeefPlugin {

    public MessageSender(@NotNull PluginInfo info, PluginClassLoader pluginClassLoader, ApplicationContext applicationContext) {
        super(info, pluginClassLoader, applicationContext);
    }

    @Override
    public void onEnable() {
        log.info("Enabled!");
    }
}
