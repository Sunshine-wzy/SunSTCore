package io.github.sunshinewzy.sunstcore.modules.machine

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * 自动机器
 */
abstract class MachineTimer(
    name: String,
    wrench: SMachineWrench,
    structure: SMachineStructure,
    val plugin: JavaPlugin,
    private val period: Long,
) : SMachine(name, wrench, structure) {
    
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            
        }, period, period)
        
    }
    
    final override fun runMachine(event: SMachineRunEvent) {
        if(event is SMachineRunEvent.Timer)
            timerRun(event)
    }
    
    abstract fun timerRun(event: SMachineRunEvent.Timer)
}