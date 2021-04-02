package io.github.sunshinewzy.sunstcore.commands

import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.utils.giveItem
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SunSTCommand : SCommand("sun") {

    init {
        addCommand("give", SItem.items.keys) {
            if(sender !is Player){
                sender.sendMessage("只有玩家才能使用此命令")
                return@addCommand false
            }
            
            if(args.isEmpty()){
                sender.sendMsg("give 后加SunST物品名称 (按 TAB 可以自动补全~)")
                return@addCommand true
            }

            val first = args[0]

            val items = SItem.items
            if(items.containsKey(first)){
                val item = items[first] ?: return@addCommand false
                sender.giveItem(item)
                sender.sendMsg("&a您已获得 $first")
                return@addCommand true
            }
            
            false
        }
        
        addCommand("reload") {
            DataManager.reloadData()
            sender.sendMessage("§a配置文件重载成功！")
            
            true
        }
    }
    
    override fun helper(sender: CommandSender, page: Int) {
        sendSeparator(sender)
        sender.sendMessage("§6§lSunSTCore §b命令指南 §d第 $page 页")
        
        when(page) {
            1 -> {
                sender.apply { 
                    sendMessage("§e/sun give [SunST物品名称]  §a>> 获得一个SunST物品")
                    sendMessage("§e/sun reload  §a>> 重载配置文件")
                }
            }
            
            else -> sender.sendMessage("§cSunSTCore 命令指南没有此页！")
        }
        
        sendSeparator(sender)
    }
    
}