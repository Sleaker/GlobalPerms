package com.sleaker.globalperms;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class GPBlockListener extends BlockListener{
	
	public GPBlockListener() {
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		
		if (!PermHandler.has(event.getPlayer(), Perms.BUILD))
			event.setCancelled(true);
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		
		if (!PermHandler.has(event.getPlayer(), Perms.BUILD))
			event.setCancelled(true);
	}
}
