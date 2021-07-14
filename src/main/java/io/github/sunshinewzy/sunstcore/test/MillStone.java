package io.github.sunshinewzy.sunstcore.test;

import io.github.sunshinewzy.sunstcore.SunSTCore;
import io.github.sunshinewzy.sunstcore.modules.machine.SFlatMachine;
import io.github.sunshinewzy.sunstcore.objects.SBlock;
import io.github.sunshinewzy.sunstcore.objects.SItem;
import io.github.sunshinewzy.sunstcore.objects.SLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MillStone extends SFlatMachine {
	public static Map<Character, SBlock> map = new HashMap<>();
	
	static {
		map.put('a', new SBlock(Material.SMOOTH_STONE_SLAB));
		map.put('b', new SBlock(Material.COBBLESTONE_WALL));
		map.put('c', new SBlock(Material.STONE_BRICKS));
		map.put('x', new SBlock(Material.COBBLESTONE_WALL));
		
	}
	
	public MillStone() {
		super(SunSTCore.INSTANCE.getPlugin(),
				"MillStone",
				"  a\nxbb\n  ba\ncbbb",
				map
		);
	}

	@Override
	public void onClick(@NotNull SLocation sLocation, @NotNull PlayerInteractEvent event) {
		Object data = getData(sLocation, "cnt");
		if(data instanceof Integer) {
			int cnt = (int) data;

			Location loc = sLocation.toLocation();
			loc.setY(loc.getBlockY() - 1);
			Block block = loc.getBlock();
			if(block.getType() == Material.COBBLESTONE) {
				if(cnt >= 4) {
					block.setType(Material.AIR);
					Objects.requireNonNull(loc.getWorld()).dropItemNaturally(loc, new SItem(Material.GRAVEL));
					
					cnt = 0;
				} else cnt++;
			}
			
			setData(sLocation, "cnt", cnt);
		}
	}

	
}
