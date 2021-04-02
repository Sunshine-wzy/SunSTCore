package io.github.sunshinewzy.sunstcore.objects.item.constructionstick

import io.github.sunshinewzy.sunstcore.utils.damageItemInOffHand
import io.github.sunshinewzy.sunstcore.utils.getFaceLocation
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import io.github.sunshinewzy.sunstcore.utils.tryToPlaceBlock
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.NumberConversions

/**
 * 直线型建筑手杖
 * @param length 建筑手杖的作用长度
 */
class LineStick(item: ItemStack, val length: Int) : ConstructionStick(item) {

    init {
        sticks.add(this)
    }
    
    constructor(map: Map<String, Any>) : this(deserialize(map), NumberConversions.toInt(map["length"]))
    
    
    override fun checkFirstRun() {
        if(sticks.isEmpty()){
            subscribeEvent<PlayerInteractEvent> {
                if(action == Action.RIGHT_CLICK_BLOCK){
                    if(clickedBlock == null) return@subscribeEvent

                    if(hand == EquipmentSlot.HAND){
                        val item = item ?: return@subscribeEvent
                        
                        if(item.type.isBlock){
                            val offHandItem = player.inventory.itemInOffHand
                            
                            sticks.forEach {
                                if(offHandItem.isItemSimilar(it)){
                                    val clickedBlock = clickedBlock ?: return@subscribeEvent
                                    var loc = clickedBlock.location

                                    for(i in 1..it.length){
                                        loc = loc.getFaceLocation(blockFace)
                                        if(loc.block.type != Material.AIR) return@forEach

                                        player.tryToPlaceBlock(loc, clickedBlock, item) {
                                            val p = player
                                            if(p.gameMode == GameMode.SURVIVAL || p.gameMode == GameMode.ADVENTURE){
                                                item.amount = item.amount - 1
                                                p.damageItemInOffHand()
                                            }
                                            true
                                        }
                                    }

                                    isCancelled = true
                                }
                            }
                            
                        }
                    }
                }
            }
        }
    }

    override fun serialize(): MutableMap<String, Any> {
        val map = super.serialize()
        map["length"] = length
        return map
    }

    companion object {
        private val sticks = ArrayList<LineStick>()
        
    }
    
}