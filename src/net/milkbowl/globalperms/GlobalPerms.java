package net.milkbowl.globalperms;

import java.util.Collection;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalPerms extends JavaPlugin {

	public static String plugName;
	private Logger log = Logger.getLogger("Minecraft");

	private GPBlockListener blockListener;
	private GPPlayerListener playerListener;
	private GPEntityListener entityListener;
	private Permission perms;

	@Override
	public void onDisable() {
		log.info(plugName + " - disabled!");

	}

	@Override
	public void onEnable() {
		GlobalPerms.plugName = "[" + this.getDescription().getName() + "]";

		if (!setupDependencies()) {
			log.warning(plugName + " - Could not detect Vault, disabling.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		PluginManager pm = this.getServer().getPluginManager();
		//Register Event and Listener object!
		blockListener = new GPBlockListener(this);
		playerListener = new GPPlayerListener(this);
		entityListener = new GPEntityListener(this);

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

	private boolean setupDependencies() {
		try {
		Collection<RegisteredServiceProvider<Permission>> perms = this.getServer().getServicesManager().getRegistrations(net.milkbowl.vault.permission.Permission.class);
		for(RegisteredServiceProvider<Permission> perm : perms) {
			Permission p = perm.getProvider();
			log.info(String.format("[%s] Found Service (Permission) %s", getDescription().getName(), p.getName()));
		}
		
		this.perms = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class).getProvider();
		log.info(String.format("[%s] Using Permission Provider %s", getDescription().getName(), this.perms.getName()));
		} catch (Exception e) {
			return false;
		}
		return (this.perms != null);
	}
	
	/**
	 * Warns the player they did not have permission for the command.
	 * 
	 * @param player
	 */
	public static void noPermsMessage(Player player) {
		player.sendMessage("You do not have permission to do that!");
	}

	public boolean has(Player player, Perms perm) {
		return this.perms.has(player, perm.getPerm());
	}
}
