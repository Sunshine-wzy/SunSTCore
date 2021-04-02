package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object SConfig {
    
    inline fun <reified T> File.loadYamlConfig(target: MutableList<T>, keys: List<String>) {
        val fileConfig = YamlConfiguration.loadConfiguration(this)
        
        keys.forEach {
            if(fileConfig.contains(it)){
                val list = SManager.castList(fileConfig.get(it), T::class.java) ?: return@forEach
                target.addAll(list)
            }
        }
    }
    
    inline fun <reified T> File.loadYamlConfig(target: MutableMap<String, MutableList<T>>) {
        val fileConfig = YamlConfiguration.loadConfiguration(this)
        
        target.forEach { (key, value) -> 
            if(fileConfig.contains(key)){
                val list = SManager.castList(key, T::class.java) ?: return@forEach
                value.addAll(list)
            }
        }
        
    }
    
    
    
}