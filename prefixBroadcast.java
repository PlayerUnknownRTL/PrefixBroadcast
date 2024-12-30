package me.lumia;

import me.lumia.Commands.MainCommands;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class prefixBroadcast extends JavaPlugin {

    static prefixBroadcast plugin;

    @Override
    public void onEnable() {

        plugin = this; // -> Объявляю плагин для дальнейшего взаимодействия.

        getConfig().options().copyDefaults(); // | Пересоздаю конфиг если его нет в файлах.
        saveDefaultConfig(); // | При каждом перезапуске - сохраняю конфиг.

        Objects.requireNonNull(getCommand("pb")).setExecutor(new MainCommands()); // -> Объявляю команду.
    }

    @Override
    public void onDisable() {



    }

    public static prefixBroadcast getPlugin() {
        return plugin;
    }

}
