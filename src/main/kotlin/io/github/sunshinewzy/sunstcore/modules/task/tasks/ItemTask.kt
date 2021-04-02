package io.github.sunshinewzy.sunstcore.modules.task.tasks

import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.utils.containsItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ItemTask(
    taskStage: TaskStage,
    taskName: String,
    order: Int,
    predecessor: TaskBase?,
    symbol: ItemStack,
    reward: Array<ItemStack>,
    val requireItems: Array<ItemStack>,
    vararg descriptionLore: String,
) : TaskBase(taskStage, taskName, order, predecessor, symbol, reward, 5, *descriptionLore) {
    
    init {
        setSubmitItemOrder(5, 2)
        setBackItemOrder(5, 4)
        
        for(i in requireItems.indices){
            if(i >= 5) break
            
            setSlotItem(3 + i, 3, requireItems[i])
        }
    }
    
    override fun submit(player: Player) {
        if(player.inventory.containsItem(requireItems))
            completeTask(player)
        else requireNotEnough(player)
    }

    override fun clickInventory(e: InventoryClickEvent) {
        
    }
    
}