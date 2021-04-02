package io.github.sunshinewzy.sunstcore.commands

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

/**
 * 自带自动补全的指令
 * (目前仅支持一个参数)
 * 
 * 使用前需要在 plugin.yml 中填写 [name] 指令的信息
 */
abstract class SCommand(val name: String) : CommandExecutor, TabCompleter, Initable {
    private val commands = HashMap<String, Pair<Collection<String>, SCommandWrapper.() -> Boolean>>()
    
    
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
        
        val first = args[0].toLowerCase()
        
        if(first.startsWith("?")){
            val num = first.substring(1).toIntOrNull()
            if(num != null){
                helper(sender, num)
                return true
            }
        }
        
        if(commands.containsKey(first)){
            val block = commands[first]?.second ?: return false
            val newArgs =
                if(args.size >= 2) ArrayList(args.toMutableList().apply { removeAt(0) })
                else ArrayList<String>()
            
            return block(SCommandWrapper(sender, cmd, label, newArgs))
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
        
        val first = args[0].toLowerCase()
        val list = ArrayList<String>()

        if(args.size >= 2){
            if(commands.containsKey(first)){
                val second = args[1].toLowerCase()
                val complement = commands[first]?.first
                if(complement != null){
                    list.addTabCommand(second, complement)
                    return list
                }
            }
        }
        
        
        list.addTabCommand(first, commands.keys)
        return list
    }

    override fun init() {
        val pluginCommand = Bukkit.getPluginCommand(name) ?: kotlin.run { 
            SunSTCore.logger.info("指令 /$name 初始化失败！")
            return
        }
        
        pluginCommand.apply {
            setExecutor(this@SCommand)
            tabCompleter = this@SCommand
        }
    }

    /**
     * 帮助信息
     * 输入的命令无参数时执行
     * 或参数为 ?1 ?2 ?3 ... ?n 时执行
     * 
     * @param page ?后所跟的数字 (无参数时为 1)
     */
    abstract fun helper(sender: CommandSender, page: Int)
    
    
    protected fun addCommand(cmd: String, complement: Collection<String> = emptyList(), block: SCommandWrapper.() -> Boolean) {
        commands[cmd.toLowerCase()] = complement to block
    }
    

    private fun MutableList<String>.addTabCommand(firstStr: String, complement: Collection<String>) {
        val arg = firstStr.toLowerCase()
        nextCommand@ for(str in complement) {
            if(arg.startsWith(str.substring(0, 1))) {
                
                if(str.length < arg.length) continue
                
                for(i in arg.indices) {
                    if (arg[i] != str[i]) continue@nextCommand
                }
                
                add(str)
            }
        }
        
        if(isEmpty()) addAll(complement)
    }
    
    
    companion object {
        fun sendRepeatMsg(sender: CommandSender, str: String, times: Int = 40) {
            val builder = StringBuilder()
            for (i in 1..times) builder.append(str)
            sender.sendMessage(builder.toString())
        }

        fun sendSeparator(sender: CommandSender) {
            sendRepeatMsg(sender, "=")
        }
    }
    
}