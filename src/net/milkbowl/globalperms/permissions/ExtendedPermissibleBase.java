package net.milkbowl.globalperms.permissions;

import java.util.List;

import org.bukkit.permissions.Permissible;

public interface ExtendedPermissibleBase extends Permissible {

	public abstract boolean inGroup(String group);

	public abstract boolean inGroup(Group group);

	public abstract List<Group> getGroups();
}
