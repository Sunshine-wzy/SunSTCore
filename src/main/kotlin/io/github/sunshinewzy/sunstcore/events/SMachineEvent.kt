package io.github.sunshinewzy.sunstcore.events

import io.github.sunshinewzy.sunstcore.modules.machine.SMachine
import org.bukkit.event.Event

abstract class SMachineEvent(val sMachine: SMachine) : Event()