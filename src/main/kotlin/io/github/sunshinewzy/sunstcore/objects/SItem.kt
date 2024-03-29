package io.github.sunshinewzy.sunstcore.objects

import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.modules.data.sunst.WorldConfig
import io.github.sunshinewzy.sunstcore.utils.getInt
import io.github.sunshinewzy.sunstcore.utils.giveItem
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer
import java.util.function.Function
import kotlin.random.Random

open class SItem(item: ItemStack) : ItemStack(item) {
    
    constructor(item: ItemStack, amount: Int) : this(item) {
        this.amount = amount
    }
    constructor(item: ItemStack, name: String) : this(item) {
        setName(name)
    }
    constructor(item: ItemStack, name: String, vararg lore: String) : this(item) {
        setNameAndLore(name, lore.toList())
    }
    constructor(item: ItemStack, lore: List<String>) : this(item) {
        setLore(lore)
    }
    
    constructor(type: Material) : this(ItemStack(type))
    constructor(type: Material, name: String) : this(type) {
        setName(name)
    }
    constructor(type: Material, name: String, vararg lore: String) : this(type) {
        setNameAndLore(name, lore.toList())
    }
    constructor(type: Material, name: String, lore: List<String>) : this(type) {
        setNameAndLore(name, lore)
    }
    constructor(type: Material, lore: List<String>) : this(type) {
        setLore(lore)
    }
    
    constructor(type: Material, amount: Int) : this(ItemStack(type, amount))
    constructor(type: Material, amount: Int, name: String) : this(type, amount) {
        setName(name)
    }
    constructor(type: Material, amount: Int, name: String, vararg lore: String) : this(type, amount) {
        setNameAndLore(name, lore.toList())
    }
    
    constructor(type: Material, damage: Short, amount: Int) : this(ItemStack(type, amount, damage))
    constructor(type: Material, damage: Short, amount: Int, name: String) : this(type, damage, amount) {
        setName(name)
    }
    constructor(type: Material, damage: Short, amount: Int, name: String, vararg lore: String) : this(type, damage, amount) {
        setNameAndLore(name, lore.toList())
    }
    constructor(type: Material, damage: Short, amount: Int, name: String, lore: List<String>) : this(type, damage, amount) {
        setNameAndLore(name, lore)
    }


    /**
     * 添加物品行为 - 当玩家手持物品交互时调用添加的行为
     * 此函数已经帮您判断好了 [PlayerInteractEvent] 事件的物品(确保为你的 [SItem])
     * 无需重复判断
     */
    fun addAction(filter: Function<PlayerInteractEvent, Boolean>, block: Consumer<PlayerInteractEvent>): SItem {
        val actions = itemActions[this] ?: kotlin.run {
            itemActions[this] = filter to arrayListOf(block)
            return this
        }
        
        actions.second.add(block)
        return this
    }
    
    fun addAction(block: Consumer<PlayerInteractEvent>): SItem {
        addAction({ true }, block)
        return this
    }
    

    override fun equals(other: Any?): Boolean =
        when {
            other == null -> false
            this === other -> true
            other !is ItemStack -> false
            else -> isItemSimilar(other) 
        }

    override fun hashCode(): Int {
        var hash = 1
        hash = hash * 31 + type.hashCode()
        hash = hash * 31 + amount
        hash = hash * 31 + if(hasItemMeta()) itemMeta.hashCode() else 0
        return hash
    }

    companion object {
        private val itemActions = HashMap<SItem, Pair<Function<PlayerInteractEvent, Boolean>, ArrayList<Consumer<PlayerInteractEvent>>>>()
        private val protectedItems = ArrayList<ItemStack>()
        
        val items = HashMap<String, ItemStack>()


        @JvmStatic
        fun createTaskSymbol(type: Material, vararg lore: String = arrayOf()): SItem {
            val loreList = arrayListOf("§a>点我查看任务<")
            if(lore.isNotEmpty())
                loreList.addAll(lore)

            return SItem(type,"", loreList)
        }

        @JvmStatic
        fun createTaskSymbolWithDamage(type: Material, damage: Short, vararg lore: String = arrayOf()): SItem {
            val loreList = arrayListOf("§a>点我查看任务<")
            if(lore.isNotEmpty())
                loreList.addAll(lore)

            return SItem(type, damage, 1, "", loreList)
        }
        
        internal fun initAction() {
            subscribeEvent<PlayerInteractEvent> { 
                val item = item
                if(item == null || item.type == Material.AIR) return@subscribeEvent
                if(!WorldConfig.isWorldEnabled(player.world)) return@subscribeEvent
                
                protectedItems.forEach { 
                    if(item.isItemSimilar(it)) {
                        isCancelled = true
                    }
                }
                
                itemActions.forEach { (sItem, pair) -> 
                    if(pair.first.apply(this) && item.isItemSimilar(sItem)){
                        pair.second.forEach { it.accept(this) }
                    }
                }
            }
        }

        @JvmStatic
        fun ItemStack.protect(): ItemStack {
            val item = clone()
            item.amount = 1
            protectedItems += item
            return this
        }

        @JvmStatic
        fun ItemStack.setName(name: String): ItemStack {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type) 
            
            meta?.setDisplayName(name.replace("&", "§"))
            itemMeta = meta
            return this
        }

        @JvmStatic
        fun ItemStack.setLore(vararg lore: String): ItemStack {
            setLore(lore.toList())
            return this
        }

        @JvmStatic
        fun ItemStack.setLore(lore: List<String>): ItemStack {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta?.lore = lore.map { it.replace("&", "§") }
            itemMeta = meta
            return this
        }

        @JvmStatic
        fun ItemStack.setNameAndLore(name: String, vararg lore: String): ItemStack {
            setNameAndLore(name, lore.toList())
            return this
        }

        @JvmStatic
        fun ItemStack.setNameAndLore(name: String, lore: List<String>): ItemStack {
            val meta = if(hasItemMeta()) itemMeta else Bukkit.getItemFactory().getItemMeta(type)
            meta?.lore = lore.map { it.replace("&", "§") }
            meta?.setDisplayName(name.replace("&", "§"))
            itemMeta = meta
            return this
        }

        @JvmStatic
        fun ItemStack?.isItemSimilar(
            item: ItemStack,
            checkLore: Boolean = true,
            checkAmount: Boolean = true,
            checkDurability: Boolean = false,
            ignoreLastTwoLine: Boolean = false
        ): Boolean {
            return if(this == null) {
                false
            } else if (type != item.type) {
                false
            } else if (checkAmount && amount < item.amount) {
                false
            } else if (checkDurability && durability != item.durability) {
                false
            } else if (hasItemMeta()) {
                val itemMeta = itemMeta ?: return true

                if (item.hasItemMeta()){
                    val itemMeta2 = item.itemMeta ?: return true
                    itemMeta.isMetaEqual(itemMeta2, checkLore, ignoreLastTwoLine)
                }
                    
                else false
            } else !item.hasItemMeta()
        }
        
        @JvmStatic
        fun ItemStack?.isItemSimilar(
            item: Itemable,
            checkLore: Boolean = true,
            checkAmount: Boolean = true,
            checkDurability: Boolean = false,
            ignoreLastTwoLine: Boolean = false
        ): Boolean = isItemSimilar(item.getSItem(), checkLore, checkAmount, checkDurability, ignoreLastTwoLine)
        
        @JvmStatic
        fun ItemStack?.isItemSimilar(item: ItemStack): Boolean = isItemSimilar(item, true)
        
        @JvmStatic
        fun ItemStack?.isItemSimilar(item: ItemStack, checkLore: Boolean): Boolean = isItemSimilar(item, checkLore, true)

        
        @JvmStatic
        fun ItemStack.addRecipe(plugin: JavaPlugin, recipe: Recipe): ItemStack {
            plugin.server.addRecipe(recipe)
            return this
        }

        @JvmStatic
        fun ItemStack.addRecipe(plugin: JavaPlugin, vararg recipes: Recipe): ItemStack {
            recipes.forEach { 
                plugin.server.addRecipe(it)
            }
            
            return this
        }

        @JvmStatic
        fun ItemStack.addShapedRecipe(
            plugin: JavaPlugin,
            key: String,
            ingredient: Map<Char, Material>,
            line1: String = "",
            line2: String = "",
            line3: String = ""
        ): ItemStack {
            addRecipe(
                plugin,
                SShapedRecipe(
                    NamespacedKey(plugin, key),
                    this,
                    ingredient,
                    line1,
                    line2,
                    line3
                )
            )
            return this
        }

        @JvmStatic
        fun ItemStack.addShapedRecipeByChoice(
            plugin: JavaPlugin,
            key: String,
            ingredient: Map<Char, SRecipeChoice>,
            line1: String = "",
            line2: String = "",
            line3: String = ""
        ): ItemStack {
            addRecipe(plugin, SShapedRecipe.byChoice(
                plugin, key, this, ingredient, line1, line2, line3
            ))
            
            return this
        }

        @JvmStatic
        fun ItemStack.addShapelessRecipe(
            plugin: JavaPlugin,
            key: String,
            ingredients: List<Pair<Int, Material>>
        ): ItemStack {
            val recipe = ShapelessRecipe(
                NamespacedKey(plugin, key),
                this
            )
            ingredients.forEach { 
                recipe.addIngredient(it.first, it.second)
            }
            
            addRecipe(plugin, recipe)
            return this
        }

        @JvmStatic
        fun ItemStack.addShapelessRecipe(
            plugin: JavaPlugin,
            key: String,
            count: Int,
            ingredient: Material
        ): ItemStack {
            addRecipe(
                plugin,
                ShapelessRecipe(
                    NamespacedKey(plugin, key),
                    this
                ).addIngredient(count, ingredient)
            )
            return this
        }

        @JvmStatic
        fun ItemStack.getSMeta(): ItemMeta = if(hasItemMeta()) itemMeta!! else Bukkit.getItemFactory().getItemMeta(type)!!

        @JvmStatic
        fun ItemStack.addUseCount(player: Player, maxCnt: Int): Boolean {
            var itemGive: ItemStack? = null
            if(amount > 1) {
                itemGive = clone()
                itemGive.amount--
                amount = 1
            }
            var flag = false
            
            val meta = getSMeta()
            val lore = meta.lore ?: ArrayList<String>()
            
            if(lore.isNotEmpty() && lore.last().startsWith("§7||§a=")) {
                val last = lore.last()
                var str = last.substringBefore('>')
                val cnt = last.filter { it == '=' }.length
                
                if(cnt >= maxCnt) {
                    lore.removeAt(lore.lastIndex)
                    lore.removeAt(lore.lastIndex)
                    amount--
                    
                    flag = true
                } else {
                    str += "=> §e"
                    val num = (100 / maxCnt) * (cnt + 1)
                    str += "$num%"
                    lore[lore.lastIndex] = str
                }
            } else {
                lore.add("")
                lore.add("§7||§a=> §e${100 / maxCnt}%")
            }
            
            meta.lore = lore
            itemMeta = meta
            itemGive?.let { player.giveItem(itemGive) }
            
            return flag
        }

        @JvmStatic
        fun ItemStack.randomAmount(st: Int, ed: Int): ItemStack {
            amount = Random.getInt(st, ed)
            return this
        }

        @JvmStatic
        fun ItemStack.randomAmount(ed: Int): ItemStack = randomAmount(1, ed)

        @JvmStatic
        fun ItemStack.cloneRandomAmount(st: Int, ed: Int): ItemStack {
            val randItem = clone()
            randItem.amount = Random.getInt(st, ed)
            return randItem
        }

        @JvmStatic
        fun ItemStack.cloneRandomAmount(ed: Int): ItemStack = randomAmount(1, ed)


        @JvmStatic
        fun ItemMeta.isMetaEqual(
            itemMeta: ItemMeta,
            checkLore: Boolean = true,
            ignoreLastTwoLine: Boolean = false
        ): Boolean {
            return if (itemMeta.hasDisplayName() != hasDisplayName()) {
                false
            } else if (itemMeta.hasDisplayName() && hasDisplayName() && itemMeta.displayName != displayName) {
                false
            } else if (!checkLore) {
                true
            } else if (itemMeta.hasLore() && hasLore()) {
                val lore = lore ?: return true
                val lore2 = itemMeta.lore ?: return true
                
                lore.isLoreEqual(lore2, ignoreLastTwoLine)
            } else !itemMeta.hasLore() && !hasLore()
        }


        @JvmStatic
        fun List<String>.isLoreEqual(
            lore: List<String>,
            ignoreLastTwoLine: Boolean = false
        ): Boolean {
            if(isEmpty() && lore.isEmpty()) return true
            
            if(ignoreLastTwoLine && last().startsWith("§7||")){
                val loreIgnoreLastTwoLine = ArrayList<String>()
                for(i in 0..lastIndex-2)
                    loreIgnoreLastTwoLine.add(this[i])
                
                return loreIgnoreLastTwoLine.toString() == lore.toString()
            }

            return toString() == lore.toString()
        }


        @JvmStatic
        fun ItemStack.addToSunSTItem(keyName: String): ItemStack {
            items[keyName] = this
            return this
        }

        @JvmStatic
        fun ItemStack.removeOne() {
            amount--
        }
        
    }
}