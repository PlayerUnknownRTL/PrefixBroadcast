package me.lumia.Commands;

import me.lumia.Color.RGBcolor;
import me.lumia.prefixBroadcast;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        FileConfiguration config = prefixBroadcast.getPlugin().getConfig();

        if(strings.length < 3) {
            commandSender.sendMessage(Objects.requireNonNull(RGBcolor.RGBcolors.colorize(config.getString("Messages.errorLenght"))));
            return true;
        }

        Player player = (Player) commandSender;
        String prefix = strings[1];
        String message = strings[2];
        Location locationSend = player.getLocation();

        if(strings[0].equalsIgnoreCase("all")) {

            Bukkit.broadcast(RGBcolor.RGBcolors.colorize(prefix + "&r" + message), "pb.player");

        } else if (strings[0].equalsIgnoreCase("only")) {

            for(Player players : Bukkit.getOnlinePlayers()) {
                if(players.getLocation().distance(locationSend) >= config.getInt("Settings.onlyBlock")) {
                    players.sendMessage(RGBcolor.RGBcolors.colorize(prefix + "&r" + message));
                }
            }

        } else if (strings[0].equalsIgnoreCase("reload")) {

            prefixBroadcast.getPlugin().reloadConfig();
            prefixBroadcast.getPlugin().saveDefaultConfig();
            player.sendMessage(Objects.requireNonNull(RGBcolor.RGBcolors.colorize(config.getString("Messages.messageReload"))));

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tab = new ArrayList<>();

        if(strings.length == 1) {
            tab.add("all");
            tab.add("only");
            tab.add("reload");
        } else if (strings.length == 2) {
            tab.add("[LUMIA]");
        } else if (strings.length == 3) {
            tab.add("HelloWorld");
        }

        return tab;
    }
}
