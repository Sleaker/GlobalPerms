package net.milkbowl.globalperms.permissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public class UserPermissibleBase extends PermissibleBase implements ExtendedPermissibleBase {

	public UserPermissibleBase(Player player) {
		super(player);
	}

	public static void inject(Player player) {
		Permissible permissible = new UserPermissibleBase(player);

		try {
			if (player.getClass().getName().contains("Spout")) {
				injectSpout(player, permissible);
			} else {
				injectCraftBukkit(player, permissible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void injectCraftBukkit(Player player, Permissible permissible) throws Exception {
		Class<?> humanEntity = Class.forName("org.bukkit.craftbukkit.entity.CraftHumanEntity");

		Field permField = humanEntity.getDeclaredField("perm");
		// Make it public for reflection
		permField.setAccessible(true);

		PermissibleBase oldBase = (PermissibleBase) permField.get(player);

		// Copy permissions and attachments from old Permissible

		// Attachments
		Field attachmentField = PermissibleBase.class.getDeclaredField("attachments");
		attachmentField.setAccessible(true);
		attachmentField.set(permissible, attachmentField.get(oldBase));

		// Permissions
		Field permissionsField = PermissibleBase.class.getDeclaredField("permissions");
		permissionsField.setAccessible(true);
		permissionsField.set(permissible, permissionsField.get(oldBase));

		// Inject permissible
		permField.set(player, permissible);
	}

	protected static void injectSpout(Player player, Permissible permissible) throws Exception {
		Class<?> humanEntity = Class.forName("org.getspout.spout.player.SpoutCraftPlayer");
		Field permField = humanEntity.getDeclaredField("perm");
		permField.setAccessible(true);

		permField.set(player, permissible);
	}
	
	@Override
	public boolean inGroup(String group) {
		if (group == null)
			throw new IllegalArgumentException("Group name cannot be null");

		Permission perm = Bukkit.getServer().getPluginManager().getPermission(group);
		if (!(perm instanceof Group))
			return false;

		return hasPermission(group);
	}

	@Override
	public boolean inGroup(Group group) {
		return inGroup(group.getName());
	}

	@Override
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
