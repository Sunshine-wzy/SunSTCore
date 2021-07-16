package io.github.sunshinewzy.sunstcore.modules.machine.custom

import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SCoordinate
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 机器配方
 * 
 * 机器功能的描述
 * @param coord 操作位置相对于机器中心方块的坐标
 */
sealed class SMachineRecipe(val coord: SCoordinate) : ConfigurationSerializable {
    /**
     * 消费配方
     * 由外部调用
     */
    fun consume(loc: Location) {
        execute(loc.addClone(coord))
    }

    override fun serialize(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        map["coord"] = coord.toString()
        return map
    }

    /**
     * 配方的具体实现执行
     */
    protected open fun execute(loc: Location) {
        
    }
    
    
    class BlockPlace(coord: SCoordinate, val sBlock: SBlock) : SMachineRecipe(coord) {
        override fun execute(loc: Location) {
            sBlock.setLocation(loc)
        }

        override fun serialize(): MutableMap<String, Any> {
            val map = super.serialize()
            
            return map
        }
    }
    
    class BlockBreak(coord: SCoordinate, val sBlock: SBlock) : SMachineRecipe(coord) {
        override fun execute(loc: Location) {
            val block = loc.block
            if(sBlock.isSimilar(block)) {
                block.type = Material.AIR
            }
        }
    }
    
    class ItemAddPlayer(coord: SCoordinate, val player: Player, val items: List<ItemStack>) : SMachineRecipe(coord) {
        
        constructor(coord: SCoordinate, player: Player, vararg item: ItemStack) : this(coord, player, item.toList())
        
        override fun execute(loc: Location) {
            player.giveItem(items)
        }
    }
    
    class ItemRemovePlayer(coord: SCoordinate, val player: Player, val type: Type, val items: List<ItemStack>) : SMachineRecipe(coord) {

        constructor(coord: SCoordinate, player: Player, type: Type, vararg item: ItemStack) : this(coord, player, type, item.toList())

        override fun execute(loc: Location) {
            val inv = player.inventory
            when(type) {
                Type.HAND -> {
                    items.forEach { 
                        inv.removeHandItem(it)
                    }
                }
                
                Type.OFF_HAND -> {
                    items.forEach { 
                        inv.removeOffHandItem(it)
                    }
                }
                
                Type.INVENTORY -> inv.removeSItem(items)
            }
        }

        enum class Type {
            HAND,
            OFF_HAND,
            INVENTORY
        }
    }
    
    class ItemAddGround(coord: SCoordinate, val items: List<ItemStack>) : SMachineRecipe(coord) {
        
        constructor(coord: SCoordinate, vararg item: ItemStack) : this(coord, item.toList())
        
        override fun execute(loc: Location) {
            loc.world?.let { world ->
                items.forEach { 
                    world.dropItemNaturally(loc, it)
                }
            }
        }
    }
    
    class ItemRemoveGround(coord: SCoordinate, val x: Double, val y: Double , val z: Double, val items: List<ItemStack>) : SMachineRecipe(coord) {

        constructor(coord: SCoordinate, x: Double, y: Double, z: Double, vararg item: ItemStack) : this(coord, x, y, z, item.toList())

        override fun execute(loc: Location) {
            val world = loc.world ?: return
            world.getNearbyEntities(loc, x, y, z).forEach { 
                if(it is Item) {
                    
                }
            }
        }
    }
    
    class Other(coord: SCoordinate) : SMachineRecipe(coord)
    
    object Empty : SMachineRecipe(SCoordinate(0, 0, 0))

}

data class SMachineRecipes(
    var input: SMachineRecipe = SMachineRecipe.Empty,
    var output: SMachineRecipe = SMachineRecipe.Empty,
    var percent: Int = 0
) : ConfigurationSerializable {
    
    init {
        require(percent in 0..100) {
            "The percent should be between 0 and 100."
        }
    }


    override fun serialize(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        
        map["input"] = input
        map["output"] = output
        map["percent"] = percent
        
        return map
    }


    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SMachineRecipes {
            val information = SMachineRecipes()

            map["input"]?.let {
                if(it is SMachineRecipe)
                    information.input = it
            }

            map["output"]?.let {
                if(it is SMachineRecipe)
                    information.output = it
            }

            map["percent"]?.let { 
                if(it is Int)
                    information.percent = it
            }


            return information
        }
    }
    
    
}