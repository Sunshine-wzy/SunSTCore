package io.github.sunshinewzy.sunstcore.modules.data

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.utils.getDataPath
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

/**
 * @param plugin 插件实例
 * @param name 保存的文件名
 * @param path 保存的路径(保存到 plugins/插件名/路径 下)
 * @param saveTime 自动保存时间间隔
 */
abstract class SAutoSaveData(
    private val plugin: JavaPlugin,
    val name: String,
    val path: String = "",
    val saveTime: Long = 12_000
) : Initable {
    protected val file = File(
        plugin.dataFolder,
        if (path == "") "data/$name.yml"
        else "${path.replace("\\", "/")}/$name.yml"
    )
    
    
    constructor(plugin: JavaPlugin, name: String, file: File): this(
        plugin,
        name,
        file.getDataPath(plugin)
    )
    
    init {
        DataManager.allAutoSaveData.add(this)
        
        if(file.exists()){
            Bukkit.getScheduler().runTaskLater(SunSTCore.plugin, Runnable {
                load()
            }, 1)
        } else create()
        
        Bukkit.getScheduler().runTaskTimer(SunSTCore.plugin, Runnable {
            save()
        }, saveTime, saveTime)
    }


    /**
     * 创建文件时调用
     */
    abstract fun YamlConfiguration.createConfig()
    
    /**
     * 保存文件前调用
     */
    abstract fun YamlConfiguration.modifyConfig()

    /**
     * 加载文件后调用
     */
    abstract fun YamlConfiguration.loadConfig()


    /**
     * 创建配置文件
     */
    private fun create() {
        val config = YamlConfiguration()
        config.createConfig()

        try {
            config.save(file)
        } catch (ex: IOException){
            ex.printStackTrace()
        }
    }

    /**
     * 保存配置文件
     */
    open fun save() {
        val config = getConfig()
        config.modifyConfig()
        
        try {
            config.save(file)
        } catch (ex: IOException){
            ex.printStackTrace()
        }
    }

    /**
     * 加载配置文件
     */
    open fun load() {
        getConfig().loadConfig()
    }


    override fun init() {
        
    }

    fun getConfig(): YamlConfiguration = YamlConfiguration.loadConfiguration(file)
    
}