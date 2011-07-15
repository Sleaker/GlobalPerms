package com.sleaker.globalperms;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalPerms extends JavaPlugin {

	public static String plugName;
	private Logger log = Logger.getLogger("Minecraft");
	
	private GPBlockListener blockListener;
	private GPPlayerListener playerListener;
	private GPEntityListener entityListener;
	
	@Override
	public void onDisable() {
		log.info(plugName + " - disabled!");
		
	}

	@Override
	public void onEnable() {
		GlobalPerms.plugName = "[" + this.getDescription().getName() + "]";
		
		PermHandler.initialize(getServer());
		if (PermHandler.isInvalidHandler()) {
			log.warning(plugName + " - Could not detect a permissions plugin, disabling.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		PluginManager pm = this.getServer().getPluginManager();
		//Register Event and Listener object!
		blockListener = new GPBlockListener();
		playerListener = new GPPlayerListener();
		entityListener = new GPEntityListener();
		
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_BED_ENTER, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.High, this);
		
		
		
		log.info(plugName + " v" + this.getDescription().getVersion() + " by Sleaker enabled!");
	}

}
