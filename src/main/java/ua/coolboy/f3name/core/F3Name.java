package ua.coolboy.f3name.core;

import java.util.UUID;

public interface F3Name {

    public LoggerUtil getLoggerUtil();

    public ServerType getServerType();

    public void send(UUID uuid, String brand);

    public F3Name getInstance();
    
    public ConfigParser getConfigParser();
    
    public static final String PREFIX = "§3[F3Name] §r";
    
    public static final String BRAND_CHANNEL = "minecraft:brand";
    public static final String PLUGIN_CHANNEL = "bukkit:f3name";

    public enum ServerType {
        BUKKIT, BUNGEE;
    }
}
