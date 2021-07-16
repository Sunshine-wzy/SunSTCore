package io.github.sunshinewzy.sunstcore.objects

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.events.smenu.SMenuClickEvent
import io.github.sunshinewzy.sunstcore.events.smenu.SMenuOpenEvent
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.objects.STurnPageType.NEXT_PAGE
import io.github.sunshinewzy.sunstcore.objects.STurnPageType.PRE_PAGE
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SInventoryHolder
import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder
import io.github.sunshinewzy.sunstcore.utils.asPlayer
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
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
    private var action: Inventory.() -> Unit = {}
    
    private val pages = HashMap<Int, SPage>()
    private val allPageButtons = HashMap<Int, Pair<STurnPageType, ItemStack>>()
    
    var holder: SInventoryHolder<*> = SProtectInventoryHolder(id)
    var openItem: ItemStack? = null
    var openSound: Sound? = null
    var volume = 1f
    var pitch = 1f
    var maxPage = 0
        set(value) {
            field = value
            holder.maxPage = value
        }
    
    
    init {
        subscribeEvent<InventoryClickEvent> { 
            val holder = inventory.holder ?: return@subscribeEvent
            if(holder is SInventoryHolder<*> && holder == this@SMenu.holder) {
                if(holder.page != 0) {
                    pages[holder.page]?.let { sPage ->
                        sPage.turnPageButtons[slot]?.let {
                            val player = view.asPlayer()

                            when(it.first) {
                                NEXT_PAGE -> if(holder.page < maxPage) openInventoryByPage(player, holder.nextPage())
                                PRE_PAGE -> if(holder.page > 1) openInventoryByPage(player, holder.prePage())
                            }

                            return@subscribeEvent
                        }
                        
                        sPage.buttons[slot]?.let { 
                            it.third(this)
                            SunSTCore.pluginManager.callEvent(SMenuClickEvent(this@SMenu, id, title, view.asPlayer(), slot, it.first, it.second, sPage.page))
                            
                            return@subscribeEvent
                        }
                    }

                    allPageButtons[slot]?.let {
                        val player = view.asPlayer()

                        when(it.first) {
                            NEXT_PAGE -> if(holder.page < maxPage) openInventoryByPage(player, holder.nextPage())
                            PRE_PAGE -> if(holder.page > 1) openInventoryByPage(player, holder.prePage())
                        }

                        return@subscribeEvent
                    }
                }

                buttons[slot]?.let {
                    buttonOnClick[slot]?.invoke(this)
                    SunSTCore.pluginManager.callEvent(SMenuClickEvent(this@SMenu, id, title, view.asPlayer(), slot, it.first, it.second))
                }
            }
        }
        
        subscribeEvent<PlayerInteractEvent> { 
            val openItem = openItem ?: return@subscribeEvent
            
            if(hand == EquipmentSlot.HAND && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
                val item = item ?: return@subscribeEvent
                if(item.isItemSimilar(openItem)) {
                    val openSound = openSound
                    
                    if(maxPage > 0) {
                        if(openSound != null) openInventoryByPageWithSound(player, 1, openSound, volume, pitch)
                        else openInventoryByPage(player, 1)
                        return@subscribeEvent
                    }
                    
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
    
    fun setPageAction(page: Int, action: Inventory.() -> Unit): SMenu {
        getSPage(page).action = action
        return this
    }

    fun setPageButton(page: Int, slot: Int, item: ItemStack, name: String, onClick: InventoryClickEvent.() -> Unit): SMenu {
        getSPage(page).buttons[slot] = Triple(name, item, onClick)
        return this
    }

    fun setPageButton(page: Int, slot: Int, item: ItemStack, name: String): SMenu {
        setPageButton(page, slot, item, name) { }
        return this
    }

    fun setPageButton(page: Int, x: Int, y: Int, item: ItemStack, name: String, onClick: InventoryClickEvent.() -> Unit): SMenu {
        setPageButton(page, x orderWith y, item, name, onClick)
        return this
    }
    
    fun setTurnPageButton(page: Int, order: Int, buttonType: STurnPageType, item: ItemStack): SMenu {
        getSPage(page).turnPageButtons[order] = buttonType to item
        return this
    }
    
    fun setTurnPageButton(page: Int, x: Int, y: Int, buttonType: STurnPageType, item: ItemStack): SMenu =
        setTurnPageButton(page, x orderWith  y, buttonType, item)

    fun setPageItem(page: Int, slot: Int, item: ItemStack): SMenu {
        getSPage(page).items[slot] = item
        return this
    }

    fun setPageItem(page: Int, x: Int, y: Int, item: ItemStack): SMenu {
        setPageItem(page, x orderWith y, item)
        return this
    }
    
    fun setAllPageButton(order: Int, buttonType: STurnPageType, item: ItemStack): SMenu {
        allPageButtons[order] = buttonType to item
        return this
    }
    
    fun setAllPageButton(x: Int, y: Int, buttonType: STurnPageType, item: ItemStack): SMenu =
        setAllPageButton(x orderWith y, buttonType, item)
    
    
    fun getInventory(page: Int = 0): Inventory {
        val holder = holder.clone()
        holder.page = page
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

    fun openInventoryByPage(player: Player, page: Int) {
        val inv = getInventory(page)
        
        allPageButtons.forEach { (order, pair) -> 
            when(pair.first) {
                NEXT_PAGE -> if(page == maxPage) return@forEach
                PRE_PAGE -> if(page == 1) return@forEach
            }
            
            inv.setItem(order, pair.second)
        }

        pages[page]?.let { sPage ->
            sPage.turnPageButtons.let { map ->
                map.forEach { (order, pair) ->
                    when(pair.first) {
                        NEXT_PAGE -> if(page == maxPage) return@forEach
                        PRE_PAGE -> if(page == 1) return@forEach
                    }

                    inv.setItem(order, pair.second)
                }
            }
            
            sPage.items.forEach { (order, item) -> 
                inv.setItem(order, item)
            }
            
            sPage.buttons.forEach { (order, triple) -> 
                inv.setItem(order, triple.second)
            }
            
            sPage.action(inv)
        }
        
        player.openInventory(inv)
        SunSTCore.pluginManager.callEvent(SMenuOpenEvent(this, id, title, player, page))
    }
    
    fun openInventoryByPageWithSound(player: Player, page: Int, sound: Sound, volume: Float = 1f, pitch: Float = 1f) {
        openInventoryByPage(player, page)
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
    
    fun getSPage(page: Int): SPage {
        var sPage = pages[page]
        if(sPage == null) {
            sPage = SPage(page)
            pages[page] = sPage
        }
        
        return sPage
    }
    
}