package io.github.sunshinewzy.sunstcore.commands

import io.github.sunshinewzy.sunstcore.commands.SCommandWrapper.Type.EMPTY
import io.github.sunshinewzy.sunstcore.commands.SCommandWrapper.Type.NORMAL
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

typealias SCWrapper = SCommandWrapper.() -> Unit

class SCommandWrapper(
    val sender: CommandSender,
    val cmd: Command,
    val label: String,
    val args: List<String>,
    val isTabCompleter: Boolean = false,
    val complements: ArrayList<String> = ArrayList(),
    val preArg: String = ""
) {
    
    private fun wrap(name: String, wrapper: SCWrapper, type: Type = NORMAL): Boolean {
        if(isTabCompleter) {
            if(args.size == 1) {
                if(name.indexOf(args.first(), ignoreCase = true) == 0) {
                    complements += name
                }
            }
            
            return false
        }
        
        
        when(type) {
            NORMAL -> {
                if(args.isEmpty() || args.first().equals(name, true))
                    return false
            }

            EMPTY -> {
                if(args.isNotEmpty() && args.first() != "")
                    return false
            }

        }
        
        val list = LinkedList<String>().also { it.addAll(args) }
        if(type != EMPTY) list.removeFirst()
        
        val scWrapper = SCommandWrapper(sender, cmd, label, list, isTabCompleter, complements, name)
        wrapper(scWrapper)
        return true
    }
    
    
    operator fun String.invoke(wrapper: SCWrapper) {
        wrap(this, wrapper)
    }
    
    operator fun Collection<String>.invoke(wrapper: SCWrapper) {
        forEach { 
            if(wrap(it, wrapper))
                return
        }
    }
    
    fun empty(wrapper: SCWrapper) {
        wrap("", wrapper, EMPTY)
    }


    enum class Type {
        NORMAL,
        EMPTY
    }
    
}