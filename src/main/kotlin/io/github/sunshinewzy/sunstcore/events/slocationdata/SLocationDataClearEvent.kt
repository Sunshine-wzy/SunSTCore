package io.github.sunshinewzy.sunstcore.events.slocationdata

import io.github.sunshinewzy.sunstcore.objects.SLocation
import org.bukkit.event.HandlerList

class SLocationDataClearEvent(sLocation: SLocation) : SLocationDataEvent(sLocation) {

    override fun getHandlers(): HandlerList = handlerList


    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
    
}