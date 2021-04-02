package io.github.sunshinewzy.sunstcore.modules.energy

import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SLocation

class SEnergyEntity {
    
    private val locations = HashSet<SLocation>()
    
    
    fun addBlock(loc: SLocation) {
        locations += loc
        entities[loc] = this
    }
    
    fun removeBlock(loc: SLocation) {
        locations -= loc
        entities -= loc
    }
    
    
    companion object {
        
        val entities = HashMap<SLocation, SEnergyEntity>()
        val pipelines = ArrayList<SBlock>()
        
    }
    
}