package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.modules.data.sunst.SLocationData
import io.github.sunshinewzy.sunstcore.modules.energy.SEnergyCell
import io.github.sunshinewzy.sunstcore.modules.energy.SEnergyCell.Companion.getEnergyCell
import io.github.sunshinewzy.sunstcore.modules.energy.SEnergyCell.Companion.isEnergyBlock
import io.github.sunshinewzy.sunstcore.modules.energy.SEnergyCell.Companion.removeEnergyBlock
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SBlock.Companion.toSBlock
import io.github.sunshinewzy.sunstcore.objects.SLocation.Companion.getSLocation
import io.github.sunshinewzy.sunstcore.utils.BlockOperator.Companion.operate
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

object BlockListener : Listener {
    val tryToPlaceBlockLocations = HashMap<Location, BlockPlaceEvent.() -> Boolean>()
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlace(e: BlockPlaceEvent) {
        val block = e.blockPlaced
        val loc = block.location
        val item = e.itemInHand
        
        if(tryToPlaceBlockLocations.isNotEmpty()){
            
            if(tryToPlaceBlockLocations.containsKey(loc)){
                if(!e.isCancelled && block.type == Material.AIR && item.type != Material.AIR && item.amount > 0){
                    val theBlock = tryToPlaceBlockLocations[loc] ?: return

                    if(theBlock(e))
                        SBlock(item).setLocation(loc)
                }

                tryToPlaceBlockLocations.remove(loc)
            }
        }

        if(SEnergyCell.hasEnergyBlocks()) {
            val sBlock = SBlock(item)

            if(sBlock.isEnergyBlock()) {
                block.operate { operator ->
                    operator.apply {
                        val flag= surroundings { theBlock ->
                            if(sBlock.isSimilar(theBlock)) {
                                theBlock.getEnergyCell()?.let {
                                    it.addBlock(theBlock.getSLocation())
                                    return@surroundings true
                                }
                            }

                            false
                        }

                        if(!flag) {
//                        block.addEnergyEntity()
                        }
                    }
                }
            }
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(e: BlockBreakEvent) {
        if(!e.isCancelled) {
            val block = e.block

            if(SEnergyCell.hasEnergyBlocks()) {
                val sBlock = block.toSBlock()

                if(sBlock.isEnergyBlock()) {
                    block.removeEnergyBlock()
                }
            }
            
            SLocationData.clearData(block.world.name, block.getSLocation().toString())
        }
    }
    
}