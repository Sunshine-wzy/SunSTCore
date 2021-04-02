package io.github.sunshinewzy.sunstcore.events

import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import org.bukkit.Location
import org.bukkit.event.HandlerList

class SMachineRemoveEvent(sMachine: SMachine, val loc: Location) :SMachineEvent(sMachine) {
    
    override fun getHandlers(): HandlerList = handlerList
    
    
    
    companion object {
        private val handlerList = HandlerList()
        
        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
}