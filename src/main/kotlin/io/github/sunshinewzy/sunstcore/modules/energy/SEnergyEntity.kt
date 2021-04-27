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
class SEnergyEntity<EU: SEnergyUnit>(val storage: EU) {
    
    private val locations = HashSet<SLocation>()
    
    
    fun addEnergy(unit: EU) {
        storage += unit
    }
    
    fun removeEnergy(unit: EU) {
        storage -= unit
    }
    
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
        private val entities = HashMap<SLocation, SEnergyEntity<out SEnergyUnit>>()
        private val energyBlocks = ArrayList<SBlock>()
        
        
        fun Location.hasEnergyEntity(): Boolean =
            entities.containsKey(toSLocation())
        
        fun Block.hasEnergyEntity(): Boolean =
            location.hasEnergyEntity()
        
        fun Location.getEnergyEntity(): SEnergyEntity<out SEnergyUnit>? {
            val sLoc = toSLocation()
            if(entities.containsKey(sLoc))
                return entities[sLoc]
            return null
        }
        
        fun Block.getEnergyEntity(): SEnergyEntity<out SEnergyUnit>? =
            location.getEnergyEntity()

        fun Location.getEnergyEntityOrFail(): SEnergyEntity<out SEnergyUnit> =
            getEnergyEntity() ?: throw NoEnergyUnitException(this)
        
        fun Block.getEnergyEntityOrFail(): SEnergyEntity<out SEnergyUnit> =
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