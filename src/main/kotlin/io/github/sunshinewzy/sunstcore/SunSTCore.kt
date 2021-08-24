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
import io.github.sunshinewzy.sunstcore.objects.SCraftRecipe
import io.github.sunshinewzy.sunstcore.objects.SHashMap
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.item.SunSTItem
import io.github.sunshinewzy.sunstcore.objects.machine.SunSTMachineManager
import io.github.sunshinewzy.sunstcore.utils.SReflect
import io.github.sunshinewzy.sunstcore.utils.SunSTTestApi
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
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
        ConfigurationSerialization.registerClass(SBlock::class.java)
        
        ConfigurationSerialization.registerClass(TaskProgress::class.java)
        
        ConfigurationSerialization.registerClass(SMachineInformation::class.java)
        ConfigurationSerialization.registerClass(SSingleMachineInformation::class.java)
        ConfigurationSerialization.registerClass(SFlatMachineInformation::class.java)
        
        ConfigurationSerialization.registerClass(SMachineRecipe::class.java)
        ConfigurationSerialization.registerClass(SMachineRecipes::class.java)
    }
    
    
    @SunSTTestApi
    private fun test() {
        
        SCraftRecipe.addRecipe(SCraftRecipe.Size3(
            SItem(Material.DIAMOND, "&d金刚石", "&7人工钻石"),
            """
                 c 
                cxc
                 c
            """.trimIndent(),
            mapOf('c' to SItem(Material.COAL_BLOCK), 'x' to SItem(Material.NETHER_STAR))
        ))
        
        SCraftRecipe.addRecipe(SCraftRecipe.Size3(
            SItem(Material.WOODEN_SWORD, "&6桃木剑"),
            " w ",
            " w ",
            "wxw",
            SHashMap<Char, ItemStack>()
                .set('w', SItem(Material.OAK_LOG))
                .set('x', SItem(Material.NETHER_STAR))
        ))
        
        val iron = SItem(Material.IRON_INGOT, "&e陨铁")
        
        SCraftRecipe.addRecipe(SCraftRecipe.Size3(
            iron,
            """
                iii
                ixi
                iii
            """.trimIndent(),
            mapOf('i' to SItem(Material.IRON_INGOT), 'x' to SItem(Material.NETHER_STAR))
        ))
        
        SCraftRecipe.addRecipe(SCraftRecipe.Size3(
            SItem(Material.IRON_SWORD, "&b陨铁剑"),
            arrayOf(
                SItem(Material.AIR), iron, SItem(Material.AIR),
                SItem(Material.AIR), iron, SItem(Material.AIR),
                iron, SItem(Material.NETHER_STAR), iron
            )
        ))
        
        subscribeEvent<PlayerInteractEvent> { 
            if(hand == EquipmentSlot.HAND && action == Action.RIGHT_CLICK_BLOCK) {
                
            }
        }
        
        subscribeEvent<PlayerJoinEvent> { 
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                if(player.isOp) {
                    player.gameMode = GameMode.CREATIVE
                }
            }, 20)
        }
        
    }
    
}