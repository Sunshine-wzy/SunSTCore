package io.github.sunshinewzy.sunstcore.modules.data.sunst

import io.github.sunshinewzy.sunstcore.modules.data.SAutoSaveData
import io.github.sunshinewzy.sunstcore.modules.machine.SSingleMachine
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

class SSingleMachineData(
    val plugin: JavaPlugin,
    val sSingleMachine: SSingleMachine
) : SAutoSaveData(plugin, sSingleMachine.name, "SSingleMachine") {

    override fun YamlConfiguration.createConfig() {
        
    }

    override fun YamlConfiguration.modifyConfig() {
        
    }

    override fun YamlConfiguration.loadConfig() {
        
    }
}