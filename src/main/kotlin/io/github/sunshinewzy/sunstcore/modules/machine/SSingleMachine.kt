package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.events.ssinglemachine.SSingleMachineAddEvent
import io.github.sunshinewzy.sunstcore.events.ssinglemachine.SSingleMachineRemoveEvent
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.interfaces.Registrable
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SSingleMachineData
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.objects.SLocation.Companion.toSLocation
import io.github.sunshinewzy.sunstcore.utils.castMap
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

abstract class SSingleMachine(
    val plugin: JavaPlugin,
    val name: String,
    val item: ItemStack
) : Registrable {
    val singleMachines = HashMap<SLocation, SSingleMachineInformation>()
    
    
    init {
        SSingleMachineData(plugin, this)
        
        val typeList = machineItemTypes[item.type]
        if(typeList != null) {
            typeList += this
        } else machineItemTypes[item.type] = arrayListOf(this)
    }
    
    
    abstract fun onClick(sLocation: SLocation, event: PlayerInteractEvent)

    
    fun getOwner(sLocation: SLocation): String = singleMachines[sLocation]?.owner ?: ""
    
    
    fun addData(sLocation: SLocation, key: String, value: String): Boolean {
        singleMachines[sLocation]?.data?.let { information ->
            information[key] = value
            return true
        }
        
        return false
    }

    fun removeData(sLocation: SLocation, key: String) {
        singleMachines[sLocation]?.data?.remove(key)
    }

    fun clearData(sLocation: SLocation) {
        singleMachines[sLocation]?.data?.clear()
    }

    fun getData(sLocation: SLocation, key: String): String? {
        singleMachines[sLocation]?.data?.let { data ->
            if(data.containsKey(key)) {
                return data[key]
            }
        }
        return null
    }

    fun getDataOrFail(sLocation: SLocation, key: String): String =
        getData(sLocation, key) ?: throw IllegalArgumentException("The SLocation '${toString()}' doesn't have SSingleMachine($name) data of $key.")

    
    /**
     * 必须调用该方法以注册机器
     */
    override fun register() {
        
    }
    
    

    companion object : Initable {
        /**
         * 所有机器的位置
         */
        private val machines = HashMap<SLocation, SSingleMachine>()
        private val machineItemTypes = HashMap<Material, ArrayList<SSingleMachine>>()


        override fun init() {
            subscribeEvent<PlayerInteractEvent> { 
                val clickedBlock = clickedBlock ?: return@subscribeEvent
                
                if(action == Action.RIGHT_CLICK_BLOCK && hand == EquipmentSlot.HAND && clickedBlock.type != Material.AIR && !player.isSneaking) {
                    val loc = clickedBlock.location
                    loc.getSSingleMachine()?.let {
                        it.onClick(loc.toSLocation(), this)
                        isCancelled = true
                    }
                }
            }
            
            subscribeEvent<BlockPlaceEvent> { 
                val handItem = itemInHand
                machineItemTypes[handItem.type]?.let { list ->
                    for(singleMachine in list) {
                        if(handItem.isItemSimilar(singleMachine.item)) {
                            addMachine(blockPlaced.location, singleMachine, player)
                            break
                        }
                    }
                }
            }
            
            subscribeEvent<BlockBreakEvent> { 
                val block = block
                val loc = block.location
                val sLoc = loc.toSLocation()

                machines[sLoc]?.let {
                    if(removeMachine(loc)) {
                        isDropItems = false
                        loc.world?.dropItemNaturally(loc, it.item)
                    }
                }
            }
        }
        
        
        fun addMachine(sLocation: SLocation, sSingleMachine: SSingleMachine, information: SSingleMachineInformation = SSingleMachineInformation()) {
            machines[sLocation] = sSingleMachine
            sSingleMachine.singleMachines[sLocation] = information
        }
        
        fun addMachine(location: Location, sSingleMachine: SSingleMachine, player: Player) {
            addMachine(location.toSLocation(), sSingleMachine, SSingleMachineInformation(player.uniqueId.toString()))
            SunSTCore.pluginManager.callEvent(SSingleMachineAddEvent(sSingleMachine, location, player))
        }

        fun removeMachine(sLocation: SLocation): SSingleMachine? =
            machines.remove(sLocation)?.also { 
                it.singleMachines.remove(sLocation)
            }
        
        fun removeMachine(location: Location): Boolean {
            removeMachine(location.toSLocation())?.let { sSingleMachine ->
                SunSTCore.pluginManager.callEvent(SSingleMachineRemoveEvent(sSingleMachine, location))
                return true
            }
            return false
        }
        
        
        fun Location.getSSingleMachine(): SSingleMachine? {
            val sLoc = toSLocation()

            return machines[sLoc]
        }
        
        fun Location.hasSSingleMachine(): Boolean =
            machines.containsKey(toSLocation())
    }
    
}


data class SSingleMachineInformation(
    var owner: String = "",
    val data: HashMap<String, String> = HashMap()
) : ConfigurationSerializable {
    
    override fun serialize(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["owner"] = owner
        map["data"] = data
        return map
    }
    
    
    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SSingleMachineInformation {
            val information = SSingleMachineInformation()
            
            map["owner"]?.let { 
                if(it is String)
                    information.owner = it
            }

            map["data"]?.castMap(information.data)
            
            return information
        }
    }
}