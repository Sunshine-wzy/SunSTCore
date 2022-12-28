package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.commands.SunSTCommand
import io.github.sunshinewzy.sunstcore.interfaces.SPlugin
import io.github.sunshinewzy.sunstcore.listeners.BlockListener
import io.github.sunshinewzy.sunstcore.listeners.SunSTSubscriber
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SLocationData
import io.github.sunshinewzy.sunstcore.modules.machine.*
import io.github.sunshinewzy.sunstcore.modules.machine.custom.SMachineRecipe
import io.github.sunshinewzy.sunstcore.modules.machine.custom.SMachineRecipes
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.item.SunSTItem
import io.github.sunshinewzy.sunstcore.objects.machine.SunSTMachineManager
import io.github.sunshinewzy.sunstcore.utils.SReflect
import io.github.sunshinewzy.sunstcore.utils.SunSTTestApi
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger


class SunSTCore : JavaPlugin(), SPlugin {
    companion object {
        lateinit var plugin: JavaPlugin
        val pluginManager: PluginManager by lazy { Bukkit.getPluginManager() }
        val logger: Logger by lazy { plugin.logger }

        const val name = "SunSTCore"
        const val colorName = "§eSunSTCore"
    }
    

    override fun onEnable() {
        plugin = this
        
        registerSerialization()
        registerListeners()
        init()
        
        val metrics = Metrics(this, 10212)
        
        logger.info("SunSTCore 加载成功！")
        
        if(System.getProperty("SunSTDebug") == "true") 
            test()
    }

    override fun onDisable() {
        DataManager.saveData()
    }


    private fun init() {
        try {
            SReflect.init()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        
        SItem.initAction()
        DataManager.init()
        SunSTItem.init()
        SMachineWrench.init()
        SunSTCommand.init()
        SLocationData.init()
        SSingleMachine.init()
        SFlatMachine.init()
        SunSTMachineManager.register()
        
    }
    
    private fun registerListeners() {
        pluginManager.apply {
            registerEvents(BlockListener, plugin)
            
        }
        
        SunSTSubscriber.init()
    }
    
    private fun registerSerialization() {
        arrayOf(
            SBlock::class.java,
            TaskProgress::class.java,
            SMachineInformation::class.java, SSingleMachineInformation::class.java, SFlatMachineInformation::class.java,
            SMachineRecipe::class.java, SMachineRecipes::class.java
        ).forEach { 
            ConfigurationSerialization.registerClass(it)
        }
    }
    
    
    @SunSTTestApi
    private fun test() {
        
    }
    
}