/**
 * 
 */
package com.sleaker.globalperms;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * @author sleaker
 *
 */
public class PermHandler {

	private static PermissionsHandler handler;
	private static Logger log = Logger.getLogger("Minecraft");
	
	private static Plugin permissionPlugin;
	public static Plugin plugin;

	private enum PermissionsHandler {
		PERMISSIONSEX, PERMISSIONS, NONE
	}


	public static void initialize(Server server) {
		plugin = server.getPluginManager().getPlugin("Administrate");
		Plugin permissionsEx = server.getPluginManager().getPlugin("PermissionsEx");
		Plugin permissions = server.getPluginManager().getPlugin("Permissions");

		if (permissionsEx != null) {
			permissionPlugin = permissionsEx;
			handler = PermissionsHandler.PERMISSIONSEX;
			String version = permissionsEx.getDescription().getVersion();
			log.info(GlobalPerms.plugName + " - Permissions hooked using: PermissionsEx v" + version);
		} else if (permissions != null) {
			permissionPlugin = permissions;
			String version = permissions.getDescription().getVersion();
			handler = PermissionsHandler.PERMISSIONS;
			log.info(GlobalPerms.plugName + " - Permissions hooked using: Permissions v" + version);
		} else {
			handler = PermissionsHandler.NONE;
			log.warning(GlobalPerms.plugName + " - A permission plugin was not detected.");
		}
	}

	public static boolean has(Player player, Perms perm) {
		switch (handler) {
		case PERMISSIONSEX:
			return PermissionsEx.getPermissionManager().has(player, perm.getPerm());
		case PERMISSIONS:
			return ((Permissions) permissionPlugin).getHandler().has(player, perm.getPerm());
		case NONE:
		default:
			return player.isOp();
		}
	}

	public static boolean isInvalidHandler() {
		if (handler == PermissionsHandler.NONE || handler == null)
			return true;
		else
			return false;
	}

	/**
	 * Warns the player they did not have permission for the command.
	 * 
	 * @param player
	 */
	 public static void noPermsMessage(Player player) {
		 player.sendMessage("You do not have permission to do that!");
	 }

}
