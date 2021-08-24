package io.github.sunshinewzy.sunstcore.objects.machine

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.machine.SSingleMachine
import io.github.sunshinewzy.sunstcore.objects.*
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.addShapelessRecipe
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SPartProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.setItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import java.util.function.Consumer

object CraftingStation : SSingleMachine(
    SunSTCore.plugin,
    "CraftingStation",
    SItem(Material.CRAFTING_TABLE, "&b合成站").addShapelessRecipe(SunSTCore.plugin, "CraftingStation", 1, Material.CRAFTING_TABLE)
) {
    private val menu = SMenu("CraftingStation", "合成站", 5)
    private val WHITE_GLASS_PANE = SItem(Material.WHITE_STAINED_GLASS_PANE)
    private val YELLOW_GLASS_PANE = SItem(Material.YELLOW_STAINED_GLASS_PANE)
    private val LIME_GLASS_PANE = SItem(Material.LIME_STAINED_GLASS_PANE)
    private val scheduler = Bukkit.getScheduler()
    
    init {
        menu.apply { 
            holder = SPartProtectInventoryHolder(
                arrayListOf(
                    2 orderWith 2, 3 orderWith 2, 4 orderWith 2,
                    2 orderWith 3, 3 orderWith 3, 4 orderWith 3,
                    2 orderWith 4, 3 orderWith 4, 4 orderWith 4,
                    8 orderWith 3
                ), id
            )
            
            for(x in 1..9) {
                setItem(x, 1, WHITE_GLASS_PANE)
                setItem(x, 5, WHITE_GLASS_PANE)
            }
            
            for(y in 2..4) {
                setItem(1, y, WHITE_GLASS_PANE)
                setItem(5, y, WHITE_GLASS_PANE)
                setItem(6, y, WHITE_GLASS_PANE)
                
                setItem(7, y, LIME_GLASS_PANE)
                setItem(9, y, LIME_GLASS_PANE)
            }
            
            setItem(8, 2, LIME_GLASS_PANE)
            setItem(8, 4, LIME_GLASS_PANE)
            
            setItem(1, 1, YELLOW_GLASS_PANE)
            setItem(5, 1, YELLOW_GLASS_PANE)
            setItem(1, 5, YELLOW_GLASS_PANE)
            setItem(5, 5, YELLOW_GLASS_PANE)
            
            setClickAction { 
                if(rawSlot == 8 orderWith 3) {
                    val currentItem = currentItem
                    if(currentItem == null || currentItem.type == Material.AIR) {
                        isCancelled = true
                    } else {
                        
                    }
                }
            }
        }
    }
    
    override fun onClick(sLocation: SLocation, event: PlayerInteractEvent) {
        val player = event.player
        menu.openInventory(player)
        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 2f)
        
        scheduler.runTaskTimer(SunSTCore.plugin, Consumer {
            val inv = player.openInventory.topInventory
            val holder = inv.holder
            
            if(holder != null && holder is SInventoryHolder<*> && holder == menu.holder && inv.type == InventoryType.CHEST) {
                val recipe = SCraftRecipe.matchRecipe(inv, 2, 2, 3) ?: kotlin.run { 
                    inv.setItem(8, 3, SItem(Material.AIR))
                    return@Consumer
                }
                
                inv.setItem(8, 3, recipe.output)
                
            } else scheduler.cancelTask(it.taskId)
        }, 1, 10)
    }
    
}