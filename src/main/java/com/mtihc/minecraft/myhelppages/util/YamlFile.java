package com.mtihc.minecraft.myhelppages.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents a .yml file.
 * It uses the <code>YamlConfiguration</code> class to load/save/edit the file.
 * 
 * @author Mitch
 *
 */
public class YamlFile {

	protected final JavaPlugin plugin;
	protected final String name;
	protected final File file;
	
	private YamlConfiguration config = null;
	

	/**
	 * Constructor
	 * @param plugin The plugin
	 * @param name the filename without .yml extension
	 */
	public YamlFile(JavaPlugin plugin, String name) {
		if (plugin == null) {
			throw new NullPointerException("Parameter plugin must be non-null.");
		}
		if (name == null) {
			throw new NullPointerException("Parameter name must be non-null.");
		}
		this.plugin = plugin;
		this.name = name;
		
		File dataFolder = plugin.getDataFolder();
		file = new File(dataFolder, this.name + ".yml");
	}
	
	/**
	 * Returns the name of the file
	 * @return The name of the file
	 */
	public String getName() { return name; }

	/**
	 * Returns the currently loaded configuration
	 * @return currently loaded config
	 */
	public YamlConfiguration getConfig() {
		return config;
	}

	/**
	 * Loads the yml file
	 */
	public void reload() {
		try {
			config = YamlConfiguration.loadConfiguration(file);
			setDefaults(this.name);
		}
		catch(Exception e) {
			plugin.getLogger().log(Level.WARNING, plugin.getDescription().getFullName() + " could not load file: " + file + " ", e);
		}
	}

	/**
	 * Set default values from the plugin's resources.
	 * Specify a fileName without extension.
	 * @param fileName The name of the file with default values
	 */
	public void setDefaults(String fileName) {
		
		InputStream defConfigStream = plugin.getResource(fileName + ".yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
				.loadConfiguration(defConfigStream);
			config.options().copyDefaults(true);
			config.setDefaults(defConfig);
			save();
		}
	}
	

	/**
	 * Add default values from the plugin's resources.
	 * Specify a fileName without extension.
	 * @param fileName The name of the file with default values
	 */
	public void addDefaults(String fileName) {
		
		InputStream defConfigStream = plugin.getResource(fileName + ".yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
				.loadConfiguration(defConfigStream);
			config.options().copyDefaults(true);
			config.addDefaults(defConfig);
			save();
		}
	}

	/**
	 * Saves the yml file
	 */
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE,
					plugin.getDescription().getFullName() + " could not save to file: " + file + " ", e);
		}
	}

	/**
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}
}
