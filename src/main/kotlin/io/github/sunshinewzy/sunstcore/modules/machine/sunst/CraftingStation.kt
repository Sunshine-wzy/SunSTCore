package io.github.sunshinewzy.sunstcore.modules.machine.sunst

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.machine.SSingleMachine
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.objects.SMenu
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SPartProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.orderWith
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object CraftingStation : SSingleMachine(
    SunSTCore.plugin,
    "CraftingStation",
    SItem(Material.CRAFTING_TABLE, "&b合成站")
) {
    private val menu = SMenu("CraftingStation", "合成站", 5)
    
    init {
        menu.apply { 
            holder = SPartProtectInventoryHolder(
                arrayListOf(
                    2 orderWith 2, 3 orderWith 2, 4 orderWith 2,
                    2 orderWith 3, 3 orderWith 3, 4 orderWith 3,
                    2 orderWith 4, 3 orderWith 4, 4 orderWith 4,
                    8 orderWith 3
                ), 0
            )
            
        }
    }
    
    override fun onClick(sLocation: SLocation, event: PlayerInteractEvent) {
        menu.openInventory(event.player)
    }
    
}