package net.milkbowl.globalperms;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class GPBlockListener extends BlockListener{
	
	GlobalPerms plugin;
	public GPBlockListener(GlobalPerms plugin) {
		this.plugin = plugin;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		
		if (!plugin.has(event.getPlayer(), Perms.BUILD))
			event.setCancelled(true);
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		
		if (!plugin.has(event.getPlayer(), Perms.BUILD))
			event.setCancelled(true);
	}
}
