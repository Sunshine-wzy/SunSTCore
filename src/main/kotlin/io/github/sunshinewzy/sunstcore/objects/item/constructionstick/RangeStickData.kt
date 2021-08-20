package io.github.sunshinewzy.sunstcore.objects.item.constructionstick

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.data.DataManager
import io.github.sunshinewzy.sunstcore.modules.data.SAutoSaveData
import io.github.sunshinewzy.sunstcore.objects.SItem
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

object RangeStickData : SAutoSaveData(
    SunSTCore.plugin,
    "RangeStick",
    "ConstructionStick"
) {
    
    init {
        DataManager.addReloadData(this)
    }
    
    
    override fun YamlConfiguration.createConfig() {
        val diamondStick = RangeStick(
            SItem(
                Material.DIAMOND_SHOVEL,
                "§b钻石建筑手杖",
                "§e将我拿在主手并对准方块",
                "§e(请确保背包中有该方块)",
                "§a[最大建造半径: 5]"
            ), 5
        )
        
        set(
            "CONSTRUCTIONSTICK_DIAMOND",
            diamondStick
        )
        
        SItem.items["CONSTRUCTIONSTICK_DIAMOND"] = diamondStick
    }

    override fun YamlConfiguration.modifyConfig() {
        
    }

    override fun YamlConfiguration.loadConfig() {
        val roots = getKeys(false)
        roots.forEach {
            val rangeStick = get(it)
            if(rangeStick is RangeStick)
                SItem.items[it] = rangeStick
        }
    }
}