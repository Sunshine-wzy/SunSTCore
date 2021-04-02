package io.github.sunshinewzy.sunstcore.modules.task

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

interface TaskInventory {
    fun getTaskInv(p: Player): Inventory
    
    fun openTaskInv(p: Player, inv: Inventory = getTaskInv(p))
}