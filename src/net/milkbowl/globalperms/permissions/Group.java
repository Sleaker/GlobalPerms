package net.milkbowl.globalperms.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionRemovedExecutor;
import org.bukkit.plugin.Plugin;

/**
 * Represents a Group of Permissions that can be attached, and have attachments. Along with being given ops.
 */
public class Group extends Permission implements Permissible  {
	
	private boolean op;
    private final List<PermissionAttachment> attachments = new LinkedList<PermissionAttachment>();
    private final Map<String, PermissionAttachmentInfo> permissions = new HashMap<String, PermissionAttachmentInfo>();
	
	public Group (boolean op, String name) {
		super(name, null, null, null);
		this.op = op;
	}
	
	public Group (boolean op, String name, String description) {
		super(name, description, null, null);
		this.op = op;
	}

	public Group (boolean op, String name, Map<String, Boolean> children) {
        super(name, null, null, children);
        this.op = op;
    }
	
	public Group (boolean op, String name, String description, Map<String, Boolean> children) {
        super(name, description, null, children);
        this.op = op;
    }
	
	@Override
	public boolean isOp() {
		return op;
	}

	@Override
	public void setOp(boolean op) {
		this.op = op;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }

        PermissionAttachment result = new PermissionAttachment(plugin, this);

        attachments.add(result);
        recalculatePermissions();

        return result;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }

        PermissionAttachment result = addAttachment(plugin);

        if (Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new RemoveAttachmentRunnable(result), ticks) == -1) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add PermissionAttachment to " + this + " for plugin " + plugin.getDescription().getFullName() + ": Scheduler returned -1");
            result.remove();
            return null;
        } else {
            return result;
        }
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        } else if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }

        PermissionAttachment result = addAttachment(plugin);
        result.setPermission(name, value);

        recalculatePermissions();

        return result;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        } else if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }

        PermissionAttachment result = addAttachment(plugin, ticks);

        if (result != null) {
            result.setPermission(name, value);
        }

        return result;
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return new HashSet<PermissionAttachmentInfo>(permissions.values());
	}

	@Override
	public boolean hasPermission(String inName) {
        if (inName == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }

        String name = inName.toLowerCase();
        if (name.equalsIgnoreCase(getName()))
        	return true;
        else if (isPermissionSet(name)) {
            return permissions.get(name).getValue();
        } else {
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);

            if (perm != null) {
                return perm.getDefault().getValue(isOp());
            } else {
                return Permission.DEFAULT_PERMISSION.getValue(isOp());
            }
        }
	}

	@Override
	public boolean hasPermission(Permission perm) {
        if (perm == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        String name = perm.getName().toLowerCase();
        if (name.equalsIgnoreCase(getName()))
        	return true;
        else if (isPermissionSet(name)) {
            return permissions.get(name).getValue();
        } else
        	return perm.getDefault().getValue(isOp());
	}

	@Override
	public boolean isPermissionSet(String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }

        return permissions.containsKey(permission.toLowerCase());
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
        if (perm == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        return isPermissionSet(perm.getName());
	}

	@Override
	public void recalculatePermissions() {
        clearPermissions();
        Set<Permission> defaults = Bukkit.getServer().getPluginManager().getDefaultPermissions(isOp());
        Bukkit.getServer().getPluginManager().subscribeToDefaultPerms(isOp(), this);

        calculateChildPermissions(getChildren(), false, null);
        
        for (Permission perm : defaults) {
            String name = perm.getName().toLowerCase();
            permissions.put(name, new PermissionAttachmentInfo(this, name, null, true));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this);
            calculateChildPermissions(perm.getChildren(), false, null);
        }

        for (PermissionAttachment attachment : attachments) {
            calculateChildPermissions(attachment.getPermissions(), false, attachment);
        }
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("Attachment cannot be null");
        }

        if (attachments.contains(attachment)) {
            attachments.remove(attachment);
            PermissionRemovedExecutor ex = attachment.getRemovalCallback();

            if (ex != null) {
                ex.attachmentRemoved(attachment);
            }

            recalculatePermissions();
        } else {
            throw new IllegalArgumentException("Given attachment is not part of Permissible object " + this);
        }
	}
	
    private synchronized void clearPermissions() {
        Set<String> perms = permissions.keySet();

        for (String name : perms) {
            Bukkit.getServer().getPluginManager().unsubscribeFromPermission(name, this);
        }

        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, this);
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, this);

        permissions.clear();
    }
    
    private void calculateChildPermissions(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
        Set<String> keys = children.keySet();

        for (String name : keys) {
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
            boolean value = children.get(name) ^ invert;
            String lname = name.toLowerCase();

            permissions.put(lname, new PermissionAttachmentInfo(this, lname, attachment, value));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this);

            if (perm != null) {
                calculateChildPermissions(perm.getChildren(), !value, attachment);
            }
        }
    }
    
    private class RemoveAttachmentRunnable implements Runnable {
        private PermissionAttachment attachment;

        public RemoveAttachmentRunnable(PermissionAttachment attachment) {
            this.attachment = attachment;
        }

        public void run() {
            attachment.remove();
        }
    }
}
