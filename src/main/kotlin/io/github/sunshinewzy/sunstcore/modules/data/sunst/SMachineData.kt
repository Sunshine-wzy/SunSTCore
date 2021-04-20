package io.github.sunshinewzy.sunstcore.modules.data.sunst

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.data.SAutoSaveData
import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import io.github.sunshinewzy.sunstcore.objects.SLocation
import org.bukkit.configuration.file.YamlConfiguration

class SMachineData(val sMachine: SMachine) : SAutoSaveData(sMachine.wrench.plugin, sMachine.name, "SMachine") {

    override fun YamlConfiguration.createConfig() {
        
    }

    override fun YamlConfiguration.modifyConfig() {
        set(keyPath, sMachine.machineSLocations.toList())
    }

    override fun YamlConfiguration.loadConfig() {
        if(contains(keyPath)){
            val list = getStringList(keyPath)
            sMachine.machineSLocations.addAll(list)
            
            list.forEach { 
                sMachine.addMachine(SLocation(it))
            }
        } else SunSTCore.logger.warning("$file 中不含键 $keyPath, 加载失败")
    }
    
    
    companion object {
        const val keyPath = "SLocation"
    }
}