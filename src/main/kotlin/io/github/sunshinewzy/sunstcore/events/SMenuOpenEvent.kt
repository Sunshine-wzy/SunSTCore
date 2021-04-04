package io.github.sunshinewzy.sunstcore.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

class SMenuOpenEvent(
    id: String,
    player: Player
) : SMenuEvent(id, player) {

    override fun getHandlers(): HandlerList = handlerList


    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
    
}