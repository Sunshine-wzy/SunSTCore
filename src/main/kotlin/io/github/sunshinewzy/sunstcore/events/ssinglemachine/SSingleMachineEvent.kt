package io.github.sunshinewzy.sunstcore.events.ssinglemachine

import io.github.sunshinewzy.sunstcore.modules.machine.SSingleMachine
import org.bukkit.event.Event

abstract class SSingleMachineEvent(val sSingleMachine: SSingleMachine) : Event()