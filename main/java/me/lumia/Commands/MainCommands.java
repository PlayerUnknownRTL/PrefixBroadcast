    package me.lumia.Commands;

    import me.clip.placeholderapi.PlaceholderAPI;
    import me.lumia.RGBcolor;
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
    import java.util.Arrays;
    import java.util.List;

    public class MainCommands implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

            FileConfiguration config = prefixBroadcast.getPlugin().getConfig();

            if (strings[0].equalsIgnoreCase("reload")) {
                prefixBroadcast.getPlugin().reloadConfig();
                prefixBroadcast.getPlugin().saveDefaultConfig();
                String reloadMessage = PlaceholderAPI.setPlaceholders((Player) commandSender, config.getString("Messages.messageReload"));
                commandSender.sendMessage(RGBcolor.RGBcolors.colorize(reloadMessage));
                return true;
            }

            if (strings.length < 3) {
                String errorLenght = PlaceholderAPI.setPlaceholders((Player) commandSender, config.getString("Messages.errorLenght"));
                commandSender.sendMessage(RGBcolor.RGBcolors.colorize(errorLenght));
                return true;
            }

            String prefix = strings[1];
            String message = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));

            if (strings[0].equalsIgnoreCase("all")) {

                String permissionMessage = PlaceholderAPI.setPlaceholders((Player) commandSender, config.getString("Settings.playerPermission"));
                Bukkit.broadcast(RGBcolor.RGBcolors.colorize(prefix + "&r " + message), permissionMessage);

            } else if (strings[0].equalsIgnoreCase("only")) {

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(config.getString("Messages.errorConsole"));
                    return true;
                }

                Player player = (Player) commandSender;
                Location locationSend = player.getLocation();

                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getLocation().distance(locationSend) >= config.getInt("Settings.onlyBlock")) {
                        players.sendMessage(RGBcolor.RGBcolors.colorize(prefix + "&r " + message));
                    }
                }
                return true;
            }
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            List<String> tab = new ArrayList<>();

            if(strings.length == 1) {
                tab.add("all");
                tab.add("only");
                tab.add("reload");
            }
            if(strings.length > 1 && strings[0].equalsIgnoreCase("reload")) {
                return new ArrayList<>();
            } else if (strings.length == 2) {
                tab.add("[LUMIA]");
            } else if (strings.length == 3) {
                tab.add("Hello World");
            }

            return tab;
        }
    }
