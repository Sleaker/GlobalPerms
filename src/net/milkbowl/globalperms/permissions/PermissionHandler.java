package net.milkbowl.globalperms.permissions;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.milkbowl.globalperms.GlobalPerms;

public class PermissionHandler {
	
	private final File permsFile;
	private final GlobalPerms plugin;
	
	private Set<Group> groups = new HashSet<Group>();
	
	public PermissionHandler(GlobalPerms plugin) {
		this.plugin = plugin;
		permsFile = new File(plugin.getDataFolder() + File.separator + "permissions.yml");
	}
	
}
