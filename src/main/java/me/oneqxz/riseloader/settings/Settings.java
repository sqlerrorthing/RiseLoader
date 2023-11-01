package me.oneqxz.riseloader.settings;

import me.oneqxz.riseloader.utils.OSUtils;
import org.bspfsystems.yamlconfiguration.configuration.Configuration;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Settings {

    protected static Settings currentConfig;
    protected static final File configFile = new File(OSUtils.getRiseFolder().toFile(), "settings.yml");

    private final YamlConfiguration yamlConfig = new YamlConfiguration();

    private Settings() throws IOException {
        if(!configFile.exists())
            configFile.createNewFile();

        try {
            yamlConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            System.exit(0);
        }

        setDefault("preferences.memory", 2048);

        setDefault("preferences.resolution.fullscreen", false);
        setDefault("preferences.resolution.width", 854);
        setDefault("preferences.resolution.height", 480);

        saveConfig();
    }

    public static Settings getSettings()
    {
        if (currentConfig == null)
        {
            try{
                currentConfig = new Settings();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }

        return currentConfig;
    }


    private void setDefault(String path, Object value)
    {
        if(!yamlConfig.isSet(path))
            yamlConfig.set(path, value);
    }

    private void saveConfig()
    {
        try {
            yamlConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Object set(String path, Object value)
    {
        yamlConfig.set(path, value);
        saveConfig();
        return value;
    }


    public boolean isBoolean(String s) {
        return yamlConfig.isBoolean(s);
    }


    public boolean getBoolean(String s) {
        return yamlConfig.getBoolean(s);
    }


    public boolean getBoolean(String s, boolean b) {
        return yamlConfig.getBoolean(s, b);
    }


    public boolean isByte(String s) {
        return yamlConfig.isByte(s);
    }


    public byte getByte(String s) {
        return yamlConfig.getByte(s);
    }


    public byte getByte(String s, byte b) {
        return yamlConfig.getByte(s, b);
    }


    public boolean isShort(String s) {
        return yamlConfig.isShort(s);
    }


    public short getShort(String s) {
        return yamlConfig.getShort(s);
    }


    public short getShort(String s, short i) {
        return yamlConfig.getShort(s, i);
    }


    public boolean isInt(String s) {
        return yamlConfig.isInt(s);
    }


    public int getInt(String s) {
        return yamlConfig.getInt(s);
    }


    public int getInt(String s, int i) {
        return yamlConfig.getInt(s, i);
    }


    public boolean isLong(String s) {
        return yamlConfig.isLong(s);
    }


    public long getLong(String s) {
        return yamlConfig.getLong(s);
    }

    public long getLong(String s, long l) {
        return yamlConfig.getLong(s, l);
    }


    public boolean isFloat(String s) {
        return yamlConfig.isFloat(s);
    }


    public float getFloat(String s) {
        return yamlConfig.getFloat(s);
    }


    public float getFloat(String s, float v) {
        return yamlConfig.getFloat(s, v);
    }


    public boolean isDouble(String s) {
        return yamlConfig.isDouble(s);
    }


    public double getDouble(String s) {
        return yamlConfig.getDouble(s);
    }


    public double getDouble(String s, double v) {
        return yamlConfig.getDouble(s, v);
    }


    public boolean isChar(String s) {
        return yamlConfig.isChar(s);
    }


    public char getChar(String s) {
        return yamlConfig.getChar(s);
    }


    public char getChar(String s, char c) {
        return yamlConfig.getChar(s, c);
    }


    public boolean isString(String s) {
        return yamlConfig.isString(s);
    }


    public String getString(String s) {
        return yamlConfig.getString(s);
    }


    public String getString(String s, String s1) {
        return yamlConfig.getString(s, s1);
    }


    public boolean isList(String s) {
        return yamlConfig.isList(s);
    }


    public List<?> getList(String s) {
        return yamlConfig.getList(s);
    }


    public List<?> getList(String s, List<?> list) {
        return yamlConfig.getList(s, list);
    }


    public boolean isConfigurationSection(String s) {
        return yamlConfig.isConfigurationSection(s);
    }


    public ConfigurationSection getConfigurationSection(String s) {
        return yamlConfig.getConfigurationSection(s);
    }


    public <T> T getObject(String s, Class<T> aClass) {
        return yamlConfig.getObject(s, aClass);
    }


    public <T> T getObject(String s, Class<T> aClass, T t) {
        return yamlConfig.getObject(s, aClass, t);
    }


    public <T extends ConfigurationSerializable> T getSerializable(String s, Class<T> aClass) {
        return yamlConfig.getSerializable(s, aClass);
    }


    public <T extends ConfigurationSerializable> T getSerializable(String s, Class<T> aClass, T t) {
        return yamlConfig.getSerializable(s, aClass, t);
    }


    public List<Boolean> getBooleanList(String s) {
        return yamlConfig.getBooleanList(s);
    }


    public List<Byte> getByteList(String s) {
        return yamlConfig.getByteList(s);
    }


    public List<Short> getShortList(String s) {
        return yamlConfig.getShortList(s);
    }


    public List<Integer> getIntList(String s) {
        return yamlConfig.getIntList(s);
    }


    public List<Long> getLongList(String s) {
        return yamlConfig.getLongList(s);
    }


    public List<Float> getFloatList(String s) {
        return yamlConfig.getFloatList(s);
    }


    public List<Double> getDoubleList(String s) {
        return yamlConfig.getDoubleList(s);
    }


    public List<Character> getCharList(String s) {
        return yamlConfig.getCharList(s);
    }


    public List<String> getStringList(String s) {
        return yamlConfig.getStringList(s);
    }


    public List<Map<?, ?>> getMapList(String s) {
        return yamlConfig.getMapList(s);
    }


    public Set<String> getKeys(boolean b) {
        return yamlConfig.getKeys(b);
    }


    public Map<String, Object> getValues(boolean b) {
        return yamlConfig.getValues(b);
    }


    public String getCurrentPath() {
        return yamlConfig.getCurrentPath();
    }


    public String getName() {
        return yamlConfig.getName();
    }


    public Configuration getRoot() {
        return yamlConfig.getRoot();
    }


    public ConfigurationSection getParent() {
        return yamlConfig.getParent();
    }


    public ConfigurationSection getDefaultSection() {
        return yamlConfig.getDefaultSection();
    }


    public void addDefault(String s, Object o) {
        yamlConfig.addDefault(s, o);
    }

    public boolean isSet(String path)
    {
        return yamlConfig.isSet(path);
    }
}
