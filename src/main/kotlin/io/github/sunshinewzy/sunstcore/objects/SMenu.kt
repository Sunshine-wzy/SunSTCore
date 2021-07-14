package io.github.sunshinewzy.sunstcore.objects

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.events.smenu.SMenuClickEvent
import io.github.sunshinewzy.sunstcore.events.smenu.SMenuOpenEvent
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.getSPlayer
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * 菜单
 * 
 * @param id 菜单ID
 * @param title 标题
 * @param size 行数
 */
class SMenu(
    val id: String,
    var title: String,
    val size: Int
) {
    private val buttons = HashMap<Int, Pair<String, ItemStack>>()       // 点击触发 SMenuClickEvent 的按钮
    private val items = HashMap<Int, ItemStack>()                       // 普通物品，点击后不会触发事件
    private val buttonOnClick = HashMap<Int, InventoryClickEvent.() -> Unit>()
    private var action: Inventory.() -> Unit = { }

    var holder: InventoryHolder = SProtectInventoryHolder(id)
    var openItem: ItemStack? = null
    var openSound: Sound? = null
    var volume = 1f
    var pitch = 1f
    
    
    init {
        subscribeEvent<InventoryClickEvent> { 
            if(inventory.holder == this@SMenu.holder) {
                buttons[slot]?.let {
                    buttonOnClick[slot]?.invoke(this)
                    SunSTCore.pluginManager.callEvent(SMenuClickEvent(this@SMenu, id, title, view.getSPlayer(), slot, it.first, it.second))
                }
            }
        }
        
        subscribeEvent<PlayerInteractEvent> { 
            val openItem = openItem ?: return@subscribeEvent
            
            if(hand == EquipmentSlot.HAND && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
                val item = item ?: return@subscribeEvent
                if(item.isItemSimilar(openItem)) {
                    val openSound = openSound
                    if(openSound != null) openInventoryWithSound(player, openSound, volume, pitch)
                    else openInventory(player)
                }
            }
        }
        
    }
    
    
    fun setButton(slot: Int, item: ItemStack, name: String): SMenu {
        buttons[slot] = name to item
        return this
    }
    
    fun setButton(x: Int, y: Int, item: ItemStack, name: String): SMenu {
        setButton(x orderWith y, item, name)
        return this
    }
    
    fun setButton(slot: Int, item: ItemStack, name: String, onClick: InventoryClickEvent.() -> Unit): SMenu {
        setButton(slot, item, name)
        buttonOnClick[slot] = onClick
        return this
    }

    fun setButton(x: Int, y: Int, item: ItemStack, name: String, onClick: InventoryClickEvent.() -> Unit): SMenu {
        setButton(x orderWith y, item, name, onClick)
        return this
    }
    
    
    fun setItem(slot: Int, item: ItemStack): SMenu {
        items[slot] = item
        return this
    }
    
    fun setItem(x: Int, y: Int, item: ItemStack): SMenu {
        setItem(x orderWith y, item)
        return this
    }
    
    fun setAction(action: Inventory.() -> Unit): SMenu {
        this.action = action
        return this
    }
    
    
    fun getInventory(): Inventory {
        val inv = Bukkit.createInventory(holder, size * 9, title)
        
        buttons.forEach { (slot, pair) ->
            inv.setItem(slot, pair.second)
        }
        
        items.forEach { (slot, item) ->
            inv.setItem(slot, item)
        }
        
        action(inv)
        
        return inv
    }
    
    fun openInventory(player: Player) {
        player.openInventory(getInventory())
        
        SunSTCore.pluginManager.callEvent(SMenuOpenEvent(this, id, title, player))
    }
    
    fun openInventoryWithSound(player: Player, sound: Sound, volume: Float = 1f, pitch: Float = 1f) {
        openInventory(player)
        player.playSound(player.location, sound, volume, pitch)
    }
    
    fun createEdge(edgeItem: ItemStack) {
        val meta = (if (edgeItem.hasItemMeta()) edgeItem.itemMeta else Bukkit.getItemFactory().getItemMeta(edgeItem.type)) ?: return
        meta.setDisplayName(" ")
        edgeItem.itemMeta = meta

        for(i in 0..8) {
            setItem(i, edgeItem)
            setItem(i + 9 * (size - 1), edgeItem)
        }
        for(i in 9..9*(size - 2) step 9) {
            setItem(i, edgeItem)
            setItem(i + 8, edgeItem)
        }
    }
    
}