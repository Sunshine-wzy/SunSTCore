package io.github.sunshinewzy.sunstcore.modules.energy

import io.github.sunshinewzy.sunstcore.exceptions.NoEnergyUnitException
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.objects.SLocation.Companion.getSLocation
import io.github.sunshinewzy.sunstcore.objects.SLocation.Companion.toSLocation
import org.bukkit.Location
import org.bukkit.block.Block


/**
 * 能量实体
 * 
 * 表示一个由能量方块互相连接形成的整体
 */
class SEnergyEntity(val storage: SEnergyUnit) {
    
    private val locations = HashSet<SLocation>()
    
    
    fun addBlock(loc: SLocation) {
        locations += loc
        entities[loc] = this
    }
    
    fun removeBlock(loc: SLocation) {
        if(locations.contains(loc))
            locations -= loc
        
        if(entities.containsKey(loc))
            entities -= loc
    }
    
    
    companion object {
        private val entities = HashMap<SLocation, SEnergyEntity>()
        private val energyBlocks = ArrayList<SBlock>()
        
        
        fun Location.hasEnergyEntity(): Boolean =
            entities.containsKey(toSLocation())
        
        fun Block.hasEnergyEntity(): Boolean =
            location.hasEnergyEntity()
        
        fun Location.getEnergyEntity(): SEnergyEntity? {
            val sLoc = toSLocation()
            if(entities.containsKey(sLoc))
                return entities[sLoc]
            return null
        }
        
        fun Block.getEnergyEntity(): SEnergyEntity? =
            location.getEnergyEntity()

        fun Location.getEnergyEntityOrFail(): SEnergyEntity =
            getEnergyEntity() ?: throw NoEnergyUnitException(this)
        
        fun Block.getEnergyEntityOrFail(): SEnergyEntity =
            location.getEnergyEntityOrFail()
        
        
        fun hasEnergyBlocks(): Boolean =
            energyBlocks.isNotEmpty()
        
        fun SBlock.isEnergyBlock(): Boolean =
            energyBlocks.contains(this)
        
        fun Block.removeEnergyBlock() {
            getEnergyEntity()?.removeBlock(getSLocation())
        }
        
        fun Location.removeEnergyBlock() {
            getEnergyEntity()?.removeBlock(toSLocation())
        }
    }
    
}