package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SPartProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent

object SunSTSubscriber : Initable {
    override fun init() {
        // 保护 holder 为 SProtectInventoryHolder 的物品栏
        subscribeEvent<InventoryClickEvent>(EventPriority.HIGHEST) {
            val holder = inventory.holder ?: return@subscribeEvent
            
            when(holder) {
                is SProtectInventoryHolder<*> -> {
                    isCancelled = true
                }
                
                else -> {
                    view.topInventory.holder?.let { topHolder ->
                        if(topHolder is SPartProtectInventoryHolder<*>) {
                            if(!topHolder.allowClickSlots.contains(slot))
                                isCancelled = true
                        }
                    }
                }
            }
            
        }
        
    }
}