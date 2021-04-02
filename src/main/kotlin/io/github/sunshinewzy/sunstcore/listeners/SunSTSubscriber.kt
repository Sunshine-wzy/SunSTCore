package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SPartProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.event.inventory.InventoryClickEvent

object SunSTSubscriber : Initable {
    override fun init() {
        // 保护 holder 为 SProtectInventoryHolder 的物品栏
        subscribeEvent<InventoryClickEvent> {
            val holder = inventory.holder
            
            when(holder) {
                is SPartProtectInventoryHolder<*> -> {
                    if(!holder.allowClickSlots.contains(slot))
                        isCancelled = true
                }

                is SProtectInventoryHolder<*> -> {
                    isCancelled = true
                }
            }
            
        }
        
    }
}