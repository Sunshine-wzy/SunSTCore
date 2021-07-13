package io.github.sunshinewzy.sunstcore

import io.github.sunshinewzy.sunstcore.commands.SunSTCommand
import io.github.sunshinewzy.sunstcore.listeners.BlockListener
import io.github.sunshinewzy.sunstcore.listeners.SunSTSubscriber
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SLocationData
import io.github.sunshinewzy.sunstcore.modules.machine.*
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SCoordinate
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.item.SunSTItem
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.LineStick
import io.github.sunshinewzy.sunstcore.objects.item.constructionstick.RangeStick
import io.github.sunshinewzy.sunstcore.utils.*
import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.metrics.BMetrics
import io.izzel.taboolib.module.dependency.Dependencies
import io.izzel.taboolib.module.dependency.Dependency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot


@Dependencies(Dependency(maven = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"))
object SunSTCore : Plugin() {
    val sunstScope by lazy { CoroutineScope(SupervisorJob()) }
    val pluginManager = Bukkit.getServer().pluginManager
    val logger = plugin.logger
    
    const val name = "SunSTCore"
    const val colorName = "§eSunSTCore"
    

    override fun onEnable() {
        registerSerialization()
        registerListeners()
        init()
        
        val metrics = BMetrics(plugin, 10212)
        
        plugin.logger.info("SunSTCore 加载成功！")
        
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
        
    }
    
    private fun registerListeners() {
        pluginManager.apply {
            registerEvents(BlockListener, plugin)
        }
        
        SunSTSubscriber.init()
    }
    
    private fun registerSerialization() {
        ConfigurationSerialization.registerClass(TaskProgress::class.java)
        
        ConfigurationSerialization.registerClass(LineStick::class.java)
        ConfigurationSerialization.registerClass(RangeStick::class.java)
        
        ConfigurationSerialization.registerClass(SFlatMachineInformation::class.java)
        ConfigurationSerialization.registerClass(SMachineInformation::class.java)
        
    }
    
    
    @SunSTTestApi
    private fun test() {
        val wrench = SMachineWrench(plugin, SItem(Material.BONE, "§e扳手", "§a适合安装多方块机器"))
        
        val millStone = object : SMachineManual("MillStone", "磨盘", wrench, SMachineStructure.CentralSymmetry(
            SMachineSize.SIZE3,
            """
                a
                
                b
            """.trimIndent(),
            mapOf('a' to SBlock(Material.STONE_SLAB), 'b' to SBlock(Material.COBBLESTONE_WALL)),
            SCoordinate(0, 1, 0)
        )) {
            override fun manualRun(event: SMachineRunEvent.Manual, level: Short) {
                var cnt = getDataByType<Int>(event.sLoc, "cnt") ?: 0

                if(cnt >= 4) {
                    event.player.giveItem(SItem(Material.GRAVEL))
                    event.player.sendMsg("Yes!")
                    
                    cnt = 0
                } else cnt++

                setData(event.sLoc, "cnt", cnt)
            }
        }
        
        
        subscribeEvent<PlayerInteractEvent> { 
            if(hand == EquipmentSlot.HAND && action == Action.RIGHT_CLICK_BLOCK) {
                val item = item ?: return@subscribeEvent
                if(item.type == Material.ARROW) {
                    player.sendMsg("Ohhhh!")
                }
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