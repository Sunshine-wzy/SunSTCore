package io.github.sunshinewzy.sunstcore.utils.nms

import io.izzel.taboolib.Version
import io.izzel.taboolib.kotlin.Reflex
import io.izzel.taboolib.module.packet.TPacketHandler
import org.bukkit.entity.Player

object SNms {
    
//    @TInject(asm = "io.github.sunshinewzy.sunstcore.utils.nms.SNms")
//    lateinit var nms: NMS
    
    internal val version = Version.getCurrentVersionInt()


    /**
     * 发送数据包方法
     *
     * @param player 玩家
     * @param packet 数据包
     * @param fields vararg指可变参数，fields 为了将数据包中各个值写入包
     */
    fun sendPacket(player: Player, packet: Any, vararg fields: Pair<String, Any>) {
        TPacketHandler.sendPacket(player, setFields(packet, *fields))
    }

    /**
     * 为了将数据包中各个值写入包
     */
    fun setFields(any: Any, vararg fields: Pair<String, Any>): Any {
        fields.forEach { (key, value) ->
            Reflex.from(any.javaClass, any).set(key, value)
        }
        return any
    }
    
    
}