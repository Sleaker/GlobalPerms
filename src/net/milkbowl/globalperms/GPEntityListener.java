package net.milkbowl.globalperms;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class GPEntityListener extends EntityListener {

	GlobalPerms plugin;
	public GPEntityListener(GlobalPerms plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled() || !(event instanceof EntityDamageByEntityEvent))
			return;

		EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent) event;
		Player player = null;
		if (event.getEntity() instanceof Player) {
			Player defender = (Player) event.getEntity();
			Player attacker = null;
			if (subEvent.getDamager() instanceof Projectile) {
				if (!(((Projectile) subEvent.getDamager()).getShooter() instanceof Player))
					return;
				else
					attacker = (Player) ((Projectile) subEvent.getDamager()).getShooter();
			} else if (!(subEvent.getDamager() instanceof Player)) 
				return;

			else
				attacker = (Player) subEvent.getDamager();


			if (attacker != null && (plugin.has(defender, Perms.PVP_IMMUNE) || plugin.has(attacker, Perms.PVP_IMMUNE))) {
				event.setCancelled(true);
				return;
			}
		} 
		
		if (subEvent.getDamager() instanceof Player) {
			player = (Player) subEvent.getDamager();
		} else if (subEvent.getEntity() instanceof Player)
			player = (Player) subEvent.getEntity();
		else if (subEvent.getEntity() instanceof Projectile) {
			if (((Projectile) subEvent.getEntity()).getShooter() instanceof Player)
				player = (Player) ((Projectile) subEvent.getEntity()).getShooter();
		}

		if (player != null && plugin.has(player, Perms.COMBAT_IMMUNE))
			event.setCancelled(true);
	}
}
