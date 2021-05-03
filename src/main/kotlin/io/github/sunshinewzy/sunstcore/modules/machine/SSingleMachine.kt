package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.objects.SLocation.Companion.toSLocation
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

abstract class SSingleMachine(val name: String) : Initable {
    val machineSLocations = HashSet<String>()
    
    abstract fun onClick(event: PlayerInteractEvent)

    /**
     * 必须调用该方法以初始化机器
     */
    override fun init() {
        
    }

    companion object : Initable {
        /**
         * 所有机器的位置
         */
        private val machines = HashMap<SLocation, SSingleMachine>()


        override fun init() {
            subscribeEvent<PlayerInteractEvent> { 
                val clickedBlock = clickedBlock ?: return@subscribeEvent
                
                if(action == Action.RIGHT_CLICK_BLOCK && hand == EquipmentSlot.HAND && clickedBlock.type != Material.AIR) {
                    val loc = clickedBlock.location
                    loc.getSSingleMachine()?.onClick(this)
                }
            }
        }
        
        fun Location.getSSingleMachine(): SSingleMachine? {
            val sLoc = toSLocation()
            
            if(machines.containsKey(sLoc)) {
                return machines[sLoc]
            }
            
            return null
        }
        
        fun Location.hasSSingleMachine(): Boolean =
            machines.containsKey(toSLocation())
    }
    
}