package io.github.sunshinewzy.sunstcore.modules.data

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException

abstract class SAutoCoverSaveData(
    plugin: JavaPlugin,
    name: String,
    path: String = "",
    saveTime: Long = 12_000
) : SAutoSaveData(plugin, name, path, saveTime) {

    override fun save() {
        val config = YamlConfiguration()
        config.modifyConfig()

        try {
            config.save(file)
        } catch (ex: IOException){
            ex.printStackTrace()
        }
    }
    
}