package io.github.sunshinewzy.sunstcore.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class SMenuClickEvent(
    id: String,
    player: Player,
    val slot: Int,
    val buttonName: String,
    val button: ItemStack,
) : SMenuEvent(id, player) {

    override fun getHandlers(): HandlerList = handlerList


    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
    
}