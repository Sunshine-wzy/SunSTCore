package io.github.sunshinewzy.sunstcore.modules.data.sunst

import io.github.sunshinewzy.sunstcore.modules.data.SAutoCoverSaveData
import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import io.github.sunshinewzy.sunstcore.modules.machine.SMachineInformation
import io.github.sunshinewzy.sunstcore.objects.SLocation
import org.bukkit.configuration.file.YamlConfiguration

class SMachineData(val sMachine: SMachine) : SAutoCoverSaveData(sMachine.wrench.plugin, sMachine.name, "SMachine") {

    override fun YamlConfiguration.modifyConfig() {
        sMachine.sMachines.forEach { (sLoc, information) ->
            set(sLoc.toString(), information)
        }
    }

    override fun YamlConfiguration.loadConfig() {
        val roots = getKeys(false)
        roots.forEach { sLoc ->
            val information = get(sLoc) as? SMachineInformation ?: SMachineInformation()
            sMachine.addMachine(SLocation(sLoc), information)
        }
    }
    
}