package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.sunst.STaskData
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SunSTPlayerData
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.createEdge
import io.github.sunshinewzy.sunstcore.utils.getSPlayer
import io.github.sunshinewzy.sunstcore.utils.hasCompleteStage
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class TaskProject(
    val projectName: String,
    val openItem: ItemStack = SItem(Material.ENCHANTED_BOOK, "§e$projectName §a向导"),
    val isFirstJoinGive: Boolean = true,
    val title: String,
    val edgeItem: ItemStack = SItem(Material.WHITE_STAINED_GLASS_PANE),
    val openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    val volume: Float = 1f,
    val pitch: Float = 1.2f,
    val invSize: Int = 5
) : TaskInventory {
    private val holder = SProtectInventoryHolder(projectName)
    
    val stageMap = HashMap<String, TaskStage>()
    val lastTaskInv = HashMap<UUID, TaskInventory>()
    
    init {
        DataManager.sTaskData[projectName] = STaskData(this)
        
        if(isFirstJoinGive)
            DataManager.firstJoinGiveOpenItems[projectName] = openItem

        subscribeEvent<PlayerInteractEvent> {
            val item = item ?: return@subscribeEvent

            if(hand == EquipmentSlot.HAND){
                when(action) {
                    Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                        if(player.isSneaking){
                            openTaskInv(player)
                        }
                    }
                    
                    Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                        if(item.isItemSimilar(openItem)){
                            isCancelled = true

                            val uuid = player.uniqueId
                            if(lastTaskInv.containsKey(uuid)){
                                val lastInv = lastTaskInv[uuid]
                                if(lastInv != null){
                                    lastInv.openTaskInv(player)
                                    val holder = player.openInventory.topInventory.holder
                                    if(holder is TaskInventoryHolder && holder.value > 1)
                                        holder.value = 1
                                    return@subscribeEvent
                                }
                            }

                            openTaskInv(player)
                        }
                    }
                    
                    else -> {}
                }
            }
        }
        
        subscribeEvent<InventoryClickEvent> { 
            if(inventory.holder == this@TaskProject.holder){
                stageMap.values.forEach { 
                    val player = view.getSPlayer()
                    if(slot == it.order && player.hasCompleteStage(it.predecessor)){
                        it.openTaskInv(player)
                    }
                }
            }
        }
    }


    override fun openTaskInv(p: Player, inv: Inventory) {
        lastTaskProject[p.uniqueId] = this
        lastTaskInv[p.uniqueId] = this
        
        p.playSound(p.location, openSound, volume, pitch)
        p.openInventory(inv)
    }
    
    override fun getTaskInv(p: Player): Inventory {
        val inv = Bukkit.createInventory(holder, invSize * 9, title)
        inv.createEdge(invSize, edgeItem)
        stageMap.values.forEach {
            val pre = it.predecessor
            if(pre == null || p.hasCompleteStage(pre)){
                inv.setItem(it.order, it.symbol)
            }
        }

        return inv
    }
    
    fun getProgress(p: Player): TaskProgress {
        val uid = p.uniqueId.toString()
        
        if(DataManager.sPlayerData.containsKey(uid)){
            val taskProgresses = DataManager.sPlayerData[uid]!!.taskProgress
            
            return if(taskProgresses.containsKey(projectName))
                taskProgresses[projectName]!!
            else{
                val progress = TaskProgress()
                taskProgresses[projectName] = progress
                progress
            }
        }
        else{
            val sPlayerData = SunSTPlayerData(SunSTCore.getPlugin(), uid)
            DataManager.sPlayerData[uid] = sPlayerData

            val progress = TaskProgress()
            sPlayerData.taskProgress[projectName] = progress
            return progress
        }
    }
    
    
    companion object {
        val lastTaskProject = HashMap<UUID, TaskProject>()
    }
    
}