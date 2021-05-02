package io.github.sunshinewzy.sunstcore.commands

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.utils.toLinkedList
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

/**
 * 自带自动补全的指令
 * 
 * 使用前需要在 plugin.yml 中填写 [name] 指令的信息
 */
open class SCommand(val name: String) : CommandExecutor, TabCompleter {
    private val commands = HashMap<String, SCWrapper>()

    private var helper: (CommandSender, Int) -> Unit = { sender, page ->
        sender.sendSeparator()
        sender.sendMessage("§6§l$name §b命令指南 §d第 $page 页")
        sender.sendSeparator()
    }

    
    init {
        val pluginCommand = Bukkit.getPluginCommand(name)
        
        if(pluginCommand != null) {
            pluginCommand.apply {
                setExecutor(this@SCommand)
                tabCompleter = this@SCommand
            }
        } else SunSTCore.logger.info("指令 /$name 初始化失败！")
        
    }
    
    
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(!cmd.name.equals(name, true)) return false
        
        if(args.isEmpty()) {
            helper(sender, 1)
            return true
        }
        
        val first = args.first().toLowerCase()
        
        if(first.startsWith("?")){
            first.substring(1).toIntOrNull()?.let {
                helper(sender, it)
                return true
            }
        }
        
        if(commands.containsKey(first)){
            val scWrapper = commands[first] ?: return false
            scWrapper(SCommandWrapper(sender, cmd, label, args.toLinkedList().also { it.removeFirst() }, first))
            return true
        }
        
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        if(!cmd.name.equals(name, true)) return null
        if(args.isEmpty()) return null
        
        val first = args.first().toLowerCase()
        val list = ArrayList<String>()

        if(args.size == 1) {
            commands.keys.forEach { 
                if(it.indexOf(first, ignoreCase = true) == 0) {
                    list += it
                }
            }
            
            return list
        }
        
        if(commands.containsKey(first)){
            val scWrapper = commands[first] ?: return list
            scWrapper(SCommandWrapper(sender, cmd, label, args.toLinkedList().also { it.removeFirst() }, first, true, list))
        }
        
        return list
    }

    
    fun addCommand(cmd: String, wrapper: SCWrapper): SCommand {
        commands[cmd.toLowerCase()] = wrapper
        return this
    }

    /**
     * 帮助信息
     * 输入的命令无参数时执行
     * 或参数为 ?1 ?2 ?3 ... ?n 时执行
     *
     * Int 表示 page  -  ?后所跟的数字 (无参数时为 1)
     */
    fun setHelper(helper: (CommandSender, Int) -> Unit): SCommand {
        this.helper = helper
        return this
    }
    
    
    
    companion object {
        fun CommandSender.sendRepeatMsg(str: String, times: Int = 40) {
            val builder = StringBuilder()
            for (i in 1..times) builder.append(str)
            sendMessage(builder.toString())
        }

        fun CommandSender.sendSeparator() {
            sendRepeatMsg("=")
        }
    }
    
}