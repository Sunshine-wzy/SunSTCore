package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.utils.castMap
import org.bukkit.configuration.serialization.ConfigurationSerializable

data class SMachineInformation(
    var owner: String = "",
    var level: Short = 0,
    val data: HashMap<String, Any> = HashMap()
) : ConfigurationSerializable {

    override fun serialize(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["owner"] = owner
        map["level"] = level
        map["data"] = data
        return map
    }


    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SMachineInformation {
            val information = SMachineInformation()

            map["owner"]?.let {
                if(it is String)
                    information.owner = it
            }
            
            map["level"]?.let { 
                if(it is Short)
                    information.level = it
            }

            map["data"]?.castMap(information.data)

            return information
        }
    }
}