package io.github.sunshinewzy.sunstcore.objects.item.constructionstick

import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.utils.getFaceLocation
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 建筑手杖
 * @param item 作为建筑手杖的物品
 * 
 * 请确保同一物品只构造一遍(构造时会自动注册该物品为建筑手杖)
 * 请确保在插件初始化阶段就构造该物品, 否则可能会出现失效情况
 */
abstract class ConstructionStick(item: ItemStack) : SItem(item) {
    
    init {
        checkFirstRun()
    }

    /**
     * 请确保第一次注册该物品时执行
     */
    abstract fun checkFirstRun()
    
    
    companion object {
        fun Player.spawnStickSelectParticle(listLoc: Collection<Location>, face: BlockFace) {
            listLoc.forEach {
                val loc = it.getFaceLocation(face).add(0.5, 0.5, 0.5)
                spawnParticle(Particle.VILLAGER_HAPPY, loc, 1)
            }
        }
    }
    
    
    
}