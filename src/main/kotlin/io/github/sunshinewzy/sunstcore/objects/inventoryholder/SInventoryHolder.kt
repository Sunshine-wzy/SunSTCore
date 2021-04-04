package io.github.sunshinewzy.sunstcore.objects.inventoryholder

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

open class SInventoryHolder<T>(var data: T) : InventoryHolder {
    private val inventory: Inventory = Bukkit.createInventory(this, 9)
    
    
    override fun getInventory(): Inventory {
        return inventory
    }

    inline fun <reified T> isData(target: T): Boolean {
        if(data is T){
            if(data == target){
                return true
            }
        }

        return false
    }
    
    
    companion object {
        fun Inventory.getSHolder(): SInventoryHolder<*>? {
            if (holder !is SInventoryHolder<*>)
                return null
            
            return holder as SInventoryHolder<*>
        }
    }
    

    override fun hashCode(): Int {
        return data?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if (this === other) return true
        if (other !is SInventoryHolder<*>) return false

        if (data != other.data) return false

        return true
    }

}