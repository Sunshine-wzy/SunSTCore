package io.github.sunshinewzy.sunstcore.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SCommandWrapper(
    val sender: CommandSender,
    val cmd: Command,
    val label: String,
    val args: List<String>
)