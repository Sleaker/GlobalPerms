package net.milkbowl.globalperms;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class GPEntityListener extends EntityListener {

	GlobalPerms plugin;
	public GPEntityListener(GlobalPerms plugin) {
		this.plugin = plugin;
	}

	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.isCancelled())
			return;
		if (event.getEntity() instanceof Player) {
			Player defender = (Player) event.getEntity();
			Player attacker = null;
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent) event;
				if (!(subEvent.getDamager() instanceof Player)) 
					return;

				else
					attacker = (Player) subEvent.getDamager();

			} else if (event instanceof EntityDamageByProjectileEvent) {
				EntityDamageByProjectileEvent subEvent = (EntityDamageByProjectileEvent) event;
				if (!(subEvent.getDamager() instanceof Player)) 
					return;
				else
					attacker = (Player) subEvent.getDamager();
			}

			if (attacker != null) {
				if ( plugin.has(defender, Perms.PVP_IMMUNE) || plugin.has(attacker, Perms.PVP_IMMUNE) )
					event.setCancelled(true);
			}
		} else if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent) event;
			if (subEvent.getDamager() instanceof Player) {
				if (plugin.has((Player) subEvent.getDamager(), Perms.COMBAT_IMMUNE))
					event.setCancelled(true);
			}
		} else if (event instanceof EntityDamageByProjectileEvent) {
			EntityDamageByProjectileEvent subEvent = (EntityDamageByProjectileEvent) event;
			if (subEvent.getDamager() instanceof Player) {
				if (plugin.has((Player) subEvent.getDamager(), Perms.COMBAT_IMMUNE))
					event.setCancelled(true);
			}
		}
	}
}
