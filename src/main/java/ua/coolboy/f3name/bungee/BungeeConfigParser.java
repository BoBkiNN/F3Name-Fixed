package ua.coolboy.f3name.bungee;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import ua.coolboy.f3name.core.ConfigParser;
import ua.coolboy.f3name.core.F3Group;

public class BungeeConfigParser implements ConfigParser {

    private final List<F3Group> groups;
    
    private final List<String> excludedServers;
    
    private final boolean coloredConsole, onlyApi, checkForUpdates;
    
    public BungeeConfigParser(Plugin plugin) throws IOException {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) throw new IllegalStateException("Cannot create data folder");
        }

        File file = new File(dataFolder, "config.yml");

        if (!file.exists()) {
            InputStream in = plugin.getResourceAsStream("bungee_config.yml");
            Files.copy(in, file.toPath());
        }
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        
        coloredConsole = config.getBoolean("colored-console", true);
        onlyApi = config.getBoolean("only-api", false);
        checkForUpdates = config.getBoolean("check-for-updates", true);
        
        excludedServers = config.getStringList("excluded-servers");
        
        groups = new ArrayList<>();
        for (String key : config.getSection("groups").getKeys()) {
            Configuration section = config.getSection("groups." + key);
            List<String> messages = section.getStringList("f3names");
            int updateTime = section.getInt("update-time", 200);
            boolean shuffle = section.getBoolean("shuffle", false);
            groups.add(new F3Group(key, messages, updateTime, shuffle));
        }
    }
    
    public List<String> getExcludedServers() {
        return ImmutableList.copyOf(excludedServers);
    }
    
    public boolean isOnlyAPI() {
        return onlyApi;
    }
    
    @Override
    public boolean checkForUpdates() {
        return checkForUpdates;
    }
    
    public void excludeServer(String name) {
        excludedServers.add(name);
    }
    
    public void removeExcludedServer(String name) {
        excludedServers.remove(name);
    }
    
    @Override
    public boolean isColoredConsole() {
        return coloredConsole;
    }

    @Override
    public F3Group getF3Group(String name) {
        for (F3Group gds : groups) {
            if (gds.getGroupName().equals(name)) {
                return gds;
            }
        }
        return null;
    }

    @Override
    public List<F3Group> getF3GroupList() {
        return groups;
    }

}
