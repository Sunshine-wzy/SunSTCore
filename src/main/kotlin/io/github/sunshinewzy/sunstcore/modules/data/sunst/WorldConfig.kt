package io.github.sunshinewzy.sunstcore.modules.data.sunst

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object WorldConfig : Initable {
    private val file: File = File(SunSTCore.plugin.dataFolder, "config/world.yml")
    private val worldEnabledMap: MutableMap<String, Boolean> = hashMapOf()
    
    
    override fun init() {
        val config = if(file.exists()) {
            YamlConfiguration.loadConfiguration(file)
        } else {
            val theConfig = YamlConfiguration()
            Bukkit.getWorlds().forEach { world -> 
                theConfig.set(world.name, true)
            }
            
            try {
                theConfig.save(file)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            theConfig
        }
        
        var flag = false
        Bukkit.getWorlds().forEach { world ->
            val name = world.name
            if(config.contains(name)) {
                worldEnabledMap[name] = config.getBoolean(name, true)
            } else {
                config.set(name, true)
                worldEnabledMap[name] = true
                flag = true
            }
        }
        
        if(flag) {
            try {
                config.save(file)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    
    fun isWorldEnabled(world: String): Boolean =
        worldEnabledMap[world] ?: true
    
    fun isWorldEnabled(world: World): Boolean =
        isWorldEnabled(world.name)
    
}