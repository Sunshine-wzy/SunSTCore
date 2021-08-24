package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SPartProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

object SunSTSubscriber : Initable {
    override fun init() {
        // 保护 holder 为 SProtectInventoryHolder 的物品栏
        subscribeEvent<InventoryClickEvent>(EventPriority.HIGHEST) {
            val holder = inventory.holder ?: return@subscribeEvent
            
            when(holder) {
                is SProtectInventoryHolder<*> -> {
                    isCancelled = true
                }
                
                is SPartProtectInventoryHolder<*> -> {
                    isCancelled = true
                    
                    when(click) {
                        ClickType.LEFT, ClickType.RIGHT, ClickType.CREATIVE, ClickType.SHIFT_LEFT -> {}
                        else -> return@subscribeEvent
                    }
                    
                    clickedInventory?.holder?.let { topHolder ->
                        if(topHolder is SPartProtectInventoryHolder<*>) {
                            if(topHolder.allowClickSlots.contains(slot))
                                isCancelled = false
                            
                            return@subscribeEvent
                        }
                    }
                    
                    if(click != ClickType.SHIFT_LEFT) {
                        isCancelled = false
                        return@subscribeEvent
                    }
                }
            }
        }
        
        subscribeEvent<InventoryDragEvent>(EventPriority.HIGHEST) {
            val holder = inventory.holder ?: return@subscribeEvent

            when(holder) {
                is SProtectInventoryHolder<*> -> {
                    isCancelled = true
                }

                is SPartProtectInventoryHolder<*> -> {
                    rawSlots.forEach {
                        if(!holder.allowClickSlots.contains(it))
                            isCancelled = true
                    }

                    return@subscribeEvent
                }
            }
        }
        
    }
}