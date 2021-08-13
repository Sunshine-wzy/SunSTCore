package io.github.sunshinewzy.sunstcore.test;

import io.github.sunshinewzy.sunstcore.SunSTCore;
import io.github.sunshinewzy.sunstcore.modules.machine.SFlatMachine;
import io.github.sunshinewzy.sunstcore.objects.SBlock;
import io.github.sunshinewzy.sunstcore.objects.SBlockMap;
import io.github.sunshinewzy.sunstcore.objects.SItem;
import io.github.sunshinewzy.sunstcore.objects.SLocation;
import io.github.sunshinewzy.sunstcore.objects.item.TaskGuideItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestFlatMachine extends SFlatMachine {
	
	public TestFlatMachine() {
		super(SunSTCore.INSTANCE.getPlugin(),
				"TestFlatMachine",
				" x \n b \ncac",
				new SBlockMap()
						.set('b', new SBlock(Material.OBSIDIAN))
						.set('a', new SBlock(Material.CRYING_OBSIDIAN))
						.set('c', new SBlock(Material.ENCHANTING_TABLE))
						.set('x', new SBlock(Material.STONE_SLAB))
		);
	}

	@Override
	public void onClick(@NotNull SLocation sLocation, @NotNull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if(SItem.isItemSimilar(item, TaskGuideItem.SUBMIT.getItem())) {
			player.sendMessage("YES!");
		}
	}
}
