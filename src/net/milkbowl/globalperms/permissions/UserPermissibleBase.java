package net.milkbowl.globalperms.permissions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;

public class UserPermissibleBase extends PermissibleBase {

	public UserPermissibleBase(ServerOperator opable) {
		super(opable);
	}

	public boolean inGroup(String group) {
		if (group == null)
			throw new IllegalArgumentException("Group name cannot be null");
		
		Permission perm = Bukkit.getServer().getPluginManager().getPermission(group);
		if (!(perm instanceof Group))
			return false;
		
		return hasPermission(group);
	}
	
	public boolean inGroup(Group group) {
		return inGroup(group.getName());
	}
	
	public List<Group> getGroups() {
		List<Group> groups = new ArrayList<Group>();
		/*	Loop through all Groups and check if inGroup - add to list if true
		 * 
		 * for (Group g : GroupManager.getGroups()) {
		 * 		if (inGroup(group))
		 * 			groups.add(g);
		 * }
		 */
		return groups;
	}
}
