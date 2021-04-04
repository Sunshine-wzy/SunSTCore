package io.github.sunshinewzy.sunstcore.events

import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList

class SMachineAddEvent(
    sMachine: SMachine,
    val loc: Location,
    val player: Player
) : SMachineEvent(sMachine), Cancellable {
    private var cancelledFlag = false
    
    
    override fun getHandlers(): HandlerList = handlerList

    override fun isCancelled(): Boolean = cancelledFlag
    override fun setCancelled(flag: Boolean) {
        cancelledFlag = flag
    }

    companion object {
        private val handlerList = HandlerList()
        
        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
}