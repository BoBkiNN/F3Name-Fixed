package ua.coolboy.f3name.bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class F3MessageListener implements PluginMessageListener {

    private final F3NameBukkit plugin;

    public F3MessageListener(F3NameBukkit plugin) {
        this.plugin = plugin;

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, F3NameBukkit.PLUGIN_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, F3NameBukkit.PLUGIN_CHANNEL, this);
    }

    /*
        Local API
        check - checking server for plugin
        message - get formatted message from plugin
        group - get player group
     */
    @Override
    public void onPluginMessageReceived(@NotNull String string, @NotNull Player player, byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String code = in.readUTF();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        switch (code) {
            case "check":
                out.writeUTF("check");
                out.writeBoolean(plugin.getConfigParser().isBungeeCord());
                player.sendPluginMessage(plugin, F3NameBukkit.PLUGIN_CHANNEL, out.toByteArray());
                break;
            case "ok":
                plugin.setBungeePlugin();
                plugin.getLoggerUtil().info("Found BungeeCord plugin, using it.");
                break;
            case "message":
                String name = in.readUTF();
                Player pl = Bukkit.getPlayer(name);
                if (pl == null) {
                    return;
                }
                plugin.send(pl, in.readUTF());
                break;
            case "group":
                out.writeUTF(plugin.getPlayerGroup(player));
                player.sendPluginMessage(plugin, F3NameBukkit.PLUGIN_CHANNEL, out.toByteArray());
                break;
        }
    }

}
