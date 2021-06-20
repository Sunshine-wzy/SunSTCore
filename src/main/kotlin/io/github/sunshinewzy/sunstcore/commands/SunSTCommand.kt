package io.github.sunshinewzy.sunstcore.commands

import io.github.sunshinewzy.sunstcore.SunSTCore.colorName
import io.github.sunshinewzy.sunstcore.commands.SCommand.Companion.sendSeparator
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.utils.giveItem
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.entity.Player

object SunSTCommand : Initable {
    
    override fun init() {
        SCommand("SunST")
            .addCommand("give", true) {
                SItem.items.keys {
                    empty {
                        if(sender !is Player){
                            sender.sendMessage("只有玩家才能使用此命令")
                            return@empty
                        }

                        val items = SItem.items
                        if(items.containsKey(preArg)){
                            val item = items[preArg] ?: return@empty
                            sender.giveItem(item)
                            sender.sendMsg(colorName, "&a您已获得 $preArg")
                            return@empty
                        }
                    }
                }
                
                empty {
                    sender.sendMsg(colorName,"&agive 后加SunST物品名称 (按 TAB 可以自动补全~)")
                }
            }
                
            .addCommand("reload", true) {
                empty {
                    DataManager.reloadData()
                    sender.sendMsg(colorName, "&a配置文件重载成功！")
                }
            }
                
            .setHelper { sender, page ->
                sender.sendSeparator()
                sender.sendMessage("§6§lSunSTCore §b命令指南 §d第 $page 页")

                when(page) {
                    1 -> {
                        sender.apply {
                            if(sender.isOp) {
                                sendMessage("§e/sun give [SunST物品名称]  §a>> 获得一个SunST物品")
                                sendMessage("§e/sun reload  §a>> 重载配置文件")
                            }
                        }
                    }

                    else -> sender.sendMessage("§cSunSTCore 命令指南没有此页！")
                }

                sender.sendSeparator()
            }
    }
    
}