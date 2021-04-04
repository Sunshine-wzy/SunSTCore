package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack

class SBlock(val material: Material) {
    var name = ""
    var data: BlockData? = null
    
    private var displayItem: ItemStack = SItem(material, 1)
    
    
    constructor(material: Material, name: String) : this(material) {
        this.name = name
    }
    
    constructor(loc: Location) : this(loc.block.type)
    
    constructor(block: Block) : this(block.type)
    
    constructor(blockState: BlockState) : this(blockState.type)
    
    constructor(item: ItemStack) : this(item.type)
    
    
    fun setBlockData(data: BlockData): SBlock {
        this.data = data
        return this
    }
    
    fun setLocation(loc: Location) {
        val block = loc.block
        block.type = material
        block.blockData
    }
    
    fun toItem(): ItemStack = SItem(material, 1)
    
    fun isSimilar(block: Block): Boolean = this == block.toSBlock()

    fun isSimilar(block: BlockState): Boolean = isSimilar(block.block)
    
    fun isSimilar(loc: Location): Boolean = isSimilar(loc.block)
    
    
    fun hasName(): Boolean = name != ""
    
    fun isNameEqual(sBlock: SBlock): Boolean = name == sBlock.name
    
    
    fun setDisplayItem(item: ItemStack): SBlock {
        displayItem = item
        return this
    }
    
    fun getDisplayItem(): ItemStack = displayItem


    
    override fun equals(other: Any?): Boolean =
        when {
            other == null -> false
            this === other -> true
            other !is SBlock -> false
            
            else -> material == other.material && name == other.name
        }

    override fun hashCode(): Int {
        var hash = 1
        hash = hash * 31 + material.hashCode()
        hash = hash * 31 + if(hasName()) name.hashCode() else 0
        return hash
    }

    override fun toString(): String = "SBlock{material=$material,name=$name}"
    
    
    companion object {
        fun Location.getSBlock(): SBlock = SBlock(this)
        
        fun Block.toSBlock(): SBlock = SBlock(this)
    }
    
}