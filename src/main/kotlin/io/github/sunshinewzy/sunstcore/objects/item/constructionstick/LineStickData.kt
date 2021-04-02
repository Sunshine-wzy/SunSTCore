package io.github.sunshinewzy.sunstcore.objects.item.constructionstick

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.SAutoSaveData
import io.github.sunshinewzy.sunstcore.objects.SItem
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

object LineStickData : SAutoSaveData(
    SunSTCore.getPlugin(),
    "LineStick",
    "ConstructionStick"
) {

    init {
        DataManager.addReloadData(this)
    }
    
    override fun YamlConfiguration.createConfig() {
        val ironStick = LineStick(
            SItem(
                Material.IRON_SHOVEL,
                "§f铁制建筑手杖",
                "§e将我拿在副手",
                "§e用主手放方块",
                "§a[建造长度: 3]"
            ), 3
        )
        
        set(
            "CONSTRUCTIONSTICK_IRON",
            ironStick
        )
        
        SItem.items["CONSTRUCTIONSTICK_IRON"] = ironStick
    }

    override fun YamlConfiguration.modifyConfig() {
        
    }

    override fun YamlConfiguration.loadConfig() {
        val roots = getKeys(false)
        roots.forEach { 
            val lineStick = get(it)
            if(lineStick is LineStick)
                SItem.items[it] = lineStick
        }
    }
}