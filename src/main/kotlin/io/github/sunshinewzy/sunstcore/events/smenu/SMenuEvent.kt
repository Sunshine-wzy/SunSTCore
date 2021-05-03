package io.github.sunshinewzy.sunstcore.events.smenu

import org.bukkit.entity.Player
import org.bukkit.event.Event

abstract class SMenuEvent(
    val id: String,
    val player: Player
) : Event()