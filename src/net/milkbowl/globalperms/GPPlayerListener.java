package net.milkbowl.globalperms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class GPPlayerListener extends PlayerListener {

	GlobalPerms plugin;
	public GPPlayerListener(GlobalPerms plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled())
			return;

		if (!plugin.has(event.getPlayer(), Perms.CHAT))
			event.setCancelled(true);
	}

	
	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (event.isCancelled())
			return;
		
		if(!plugin.has(event.getPlayer(), Perms.BUILD))
			event.setCancelled(false);
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		Material mat = event.getClickedBlock().getType();
		Action action = event.getAction();
		Material hand = player.getItemInHand().getType();
		if (hand.equals(Material.FLINT_AND_STEEL) && (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR))) {
			if (!plugin.has(player, Perms.USE_FIRE)) {
				event.setCancelled(true);
				return;
			}
		} else if (hand.equals(Material.LAVA_BUCKET) && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
			if (!plugin.has(player, Perms.USE_LAVA)) {
				event.setCancelled(true);
				return;
			}
		} else if (hand.equals(Material.WATER_BUCKET) && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
			if (!plugin.has(player, Perms.USE_WATER)) {
				event.setCancelled(true);
				return;
			}
		}
		//Don't do conditionals if this player has the invisible use.all permission. This permission is for tracking players that have use.*
		if (plugin.has(player, Perms.USE_ALL))
			return;
		//Check Permission for individual Blocks
		if (mat.equals(Material.CHEST) && action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!plugin.has(player, Perms.USE_CHEST))
				event.setCancelled(true);
		} else if ((mat.equals(Material.BED) || mat.equals(Material.BED_BLOCK)) && action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!plugin.has(player, Perms.USE_BED))
				event.setCancelled(true);
		} else if ((mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN)) && action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!plugin.has(player, Perms.USE_SIGN))
				event.setCancelled(true);
		} else if (mat.equals(Material.IRON_DOOR) || mat.equals(Material.IRON_DOOR_BLOCK) || mat.equals(Material.WOOD_DOOR) || mat.equals(Material.WOODEN_DOOR)) {
			if (!plugin.has(player, Perms.USE_DOOR))
				event.setCancelled(true);
		} else if (mat.equals(Material.TRAP_DOOR)) {
			if (!plugin.has(player, Perms.USE_DOOR))
				event.setCancelled(true);
		}
	}

	@Override
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		if (event.isCancelled())
			return;
		if (!plugin.has(event.getPlayer(), Perms.USE_BED))
			event.setCancelled(true);
	}

	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if (event.isCancelled())
			return;

		Material mat = event.getBlockClicked().getType();
		if (mat.equals(Material.LAVA)) {
			if (!plugin.has(event.getPlayer(), Perms.FILL_LAVA));
				event.setCancelled(true);
		} else if (mat.equals(Material.WATER)) {
			if (!plugin.has(event.getPlayer(), Perms.FILL_WATER))
				event.setCancelled(true);
		}
	}

	@Override
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.isCancelled())
			return;
		
		Material mat = event.getBucket();
		if (mat.equals(Material.LAVA_BUCKET)) {
			if (!plugin.has(event.getPlayer(), Perms.USE_LAVA))
				event.setCancelled(true);
		} else if (mat.equals(Material.WATER_BUCKET)) {
			if (!plugin.has(event.getPlayer(), Perms.USE_WATER))
				event.setCancelled(true);
		}
	}
	
	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (plugin.has(event.getPlayer(), Perms.DENY_LOGIN))
			event.getPlayer().kickPlayer("You are not authorized on this server!");
	}
}
