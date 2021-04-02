package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.objects.SBlock
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

object BlockListener : Listener {
    val tryToPlaceBlockLocations = HashMap<Location, BlockPlaceEvent.() -> Boolean>()
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlace(e: BlockPlaceEvent) {
        if(tryToPlaceBlockLocations.isNotEmpty()){
            val block = e.blockPlaced
            val loc = block.location
            val item = e.itemInHand
            
            if(tryToPlaceBlockLocations.containsKey(loc)){
                if(!e.isCancelled && block.type == Material.AIR && item != null && item.type != Material.AIR && item.amount > 0){
                    val theBlock = tryToPlaceBlockLocations[loc] ?: return
                    if(theBlock(e))
                        SBlock(item).setLocation(loc)
                }

                tryToPlaceBlockLocations.remove(loc)
            }
        }
        
        
        
    }
    
}