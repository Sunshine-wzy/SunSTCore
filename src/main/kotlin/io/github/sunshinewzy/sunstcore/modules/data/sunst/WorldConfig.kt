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
    
    lateinit var config: YamlConfiguration
    
    override fun init() {
        config = if(file.exists()) {
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
        
        config.getKeys(false).forEach { world ->
            worldEnabledMap[world] = config.getBoolean(world, true)
        }
    }
    
    fun checkWorld(world: String) {
        if(world !in worldEnabledMap) {
            config.set(world, true)
            worldEnabledMap[world] = true

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