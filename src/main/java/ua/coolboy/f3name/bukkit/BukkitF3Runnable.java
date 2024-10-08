package ua.coolboy.f3name.bukkit;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ua.coolboy.f3name.core.F3Group;
import ua.coolboy.f3name.core.F3Runnable;

public class BukkitF3Runnable extends BukkitRunnable implements F3Runnable {

    private final List<String> names;
    private int current;
    private final F3NameBukkit plugin;
    private final F3Group group;

    private static final Random random = new Random();

    private final List<Player> players;

    public BukkitF3Runnable(F3NameBukkit plugin, F3Group group) {
        this.plugin = plugin;
        //I don't trust this line
        this.players = new CopyOnWriteArrayList<>();
        this.group = group;

        if (group.getNamesList() == null || group.getNamesList().isEmpty()) {
            throw new IllegalArgumentException("List must contain at least one string!");
        }

        this.names = new ArrayList<>();

        for (String string : group.getNamesList()) {
            this.names.add(ChatColor.translateAlternateColorCodes('&',string) + ChatColor.RESET);
        }
        
        plugin.getLoggerUtil().info(this.names);

        current = -1;
    }

    @Override
    public String getCurrentString() {
        return current < 0 ? names.get(0) : names.get(current);
    }

    @Override
    public List<String> getStrings() {
        return names;
    }

    public List<Player> getPlayers() {
        return ImmutableList.copyOf(players); //clone list
    }

    @Override
    public F3Group getGroup() {
        return group;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public void run() {
        if (group.isShuffle()) {
            current = random.nextInt(names.size());
        } else {
            current++;
        }
        if (names.size() <= current) {
            current = 0;
        }
        for(Player player : players) {
            if (!player.isOnline()) {
                continue;
            }
            plugin.send(player, names.get(current));
        }
        players.removeIf((Player t) -> !t.isOnline());
    }

}
