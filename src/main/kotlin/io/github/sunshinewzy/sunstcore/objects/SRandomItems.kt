package io.github.sunshinewzy.sunstcore.objects

import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class SRandomItems(val items: List<SRandomItem>) {
    
    constructor(vararg items: SRandomItem) : this(items.toList())
    
    
    
    fun takeAll(): List<ItemStack> {
        val list = ArrayList<ItemStack>()
        
        items.forEach {
            val randInt = Random.nextInt(100) + 1
            
            if(randInt in 1..it.percent) {
                list += it.items
            }
        }
        
        return list
    }
    
    fun takeOne(): List<ItemStack> {
        val randInt = Random.nextInt(100) + 1
        
        var cnt = 0
        items.forEach { 
            if(randInt in cnt..(cnt + it.percent)) {
                return it.items
            }
            cnt += it.percent
        }
        
        return emptyList()
    }
    
    
    fun takeAllFirst(): List<ItemStack> {
        val list = ArrayList<ItemStack>()

        items.forEach {
            val randInt = Random.nextInt(100) + 1

            if(randInt in 1..it.percent) {
                list += it.items.first()
            }
        }

        return list
    }
    
    fun takeOneFirst(): ItemStack {
        val randInt = Random.nextInt(100) + 1

        var cnt = 0
        items.forEach {
            if(randInt in cnt..(cnt + it.percent)) {
                return it.items.first()
            }
            cnt += it.percent
        }

        return SItem(Material.AIR)
    }
    
    
    
    companion object {

        fun Array<ItemStack>.randItem(): ItemStack = random()
        
    }
    
}

/**
 * @param percent 表示概率(0% - 100%)
 * @param items 表示有 [percent] 的概率抽取的物品(组)
 */
class SRandomItem(val percent: Int, val items: List<ItemStack>) {
    
    constructor(percent: Int, item: ItemStack) : this(percent, listOf(item))
    
    constructor(percent: Int, item: Itemable) : this(percent, item.getSItem())
    
    init {
        require(percent in 0..100) {
            "The percent should be between 0 and 100."
        }
        
        require(items.isNotEmpty()) {
            "Items must have one item in it."
        }
    }
    
}