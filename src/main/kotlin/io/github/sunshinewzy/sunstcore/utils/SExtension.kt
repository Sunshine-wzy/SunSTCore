package io.github.sunshinewzy.sunstcore.utils

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.listeners.BlockListener
import io.github.sunshinewzy.sunstcore.modules.task.TaskBase
import io.github.sunshinewzy.sunstcore.modules.task.TaskProgress
import io.github.sunshinewzy.sunstcore.modules.task.TaskProject
import io.github.sunshinewzy.sunstcore.modules.task.TaskStage
import io.github.sunshinewzy.sunstcore.objects.*
import io.github.sunshinewzy.sunstcore.objects.SItem.Companion.isItemSimilar
import io.github.sunshinewzy.sunstcore.utils.SReflect.damage
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockFace.*
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.*
import org.bukkit.material.MaterialData
import org.bukkit.metadata.MetadataValueAdapter
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.random.Random


//region Any 对象

inline fun <reified T> Any.castList(): ArrayList<T> {
    val list = ArrayList<T>()
    if(this is List<*>){
        forEach { 
            if(it is T)
                list += it
        }
    }
    return list
}

/**
 * Object转Map
 */
inline fun <reified K, reified V> Any.castMap(kClazz: Class<K>, vClazz: Class<V>): MutableMap<K, V>? {
    val result = HashMap<K, V>()
    if (this is Map<*, *>) {
        for ((key, value) in this) {
            if(key is K && value is V){
                result[kClazz.cast(key)] = vClazz.cast(value)
            }
        }
        return result
    }
    return null
}

inline fun <reified K, reified V> Any.castMap(kClazz: Class<K>, vClazz: Class<V>, targetMap: MutableMap<K, V>): Boolean {
    if (this is Map<*, *>) {
        for ((key, value) in this) {
            if(key is K && value is V)
                targetMap[kClazz.cast(key)] = vClazz.cast(value)
        }
        return true
    }
    return false
}

inline fun <reified K, reified V> Any.castMap(targetMap: MutableMap<K, V>): Boolean {
    if(castMap(K::class.java, V::class.java, targetMap))
        return true
    return false
}

fun Any.castMapBoolean(): MutableMap<String, Boolean> {
    val map = HashMap<String, Boolean>()
    
    if(this is Map<*, *>){
        forEach { key, value -> 
            if(key is String && value is Boolean){
                map[key] = value
            }
        }
    }
    
    return map
}

//endregion

//region TaskModule 任务模块

fun Player.hasCompleteTask(task: TaskBase?): Boolean {
    if(task == null) return true
    
    val taskProject = task.taskStage.taskProject
    val progress = taskProject.getProgress(this)
    
    return progress.hasCompleteTask(task)
}

fun Player.hasCompleteStage(stage: TaskStage?): Boolean {
    if(stage == null) return true
    if(stage.finalTask == null) return true
    
    val progress = stage.taskProject.getProgress(this)
    return progress.hasCompleteStage(stage)
}

//endregion

//region Player 玩家

fun Player.openInvWithSound(inv: Inventory, openSound: Sound, volume: Float, pitch: Float) {
    playSound(location, openSound, volume, pitch)
    openInventory(inv)
}

/**
 * 给予玩家物品时检测玩家背包是否已满
 * 如果未满则直接添加到玩家背包
 * 否则以掉落物的形式生成到玩家附近
 */
fun Player.giveItem(item: ItemStack, amount: Int = 0) {
    if(amount > 0){
        if(amount < 64){
            item.amount = amount
        } else item.amount = 64
    }
    
    if(inventory.isFull()){
        world.dropItem(location, item)
    }
    else {
        inventory.addItem(item)
    }
}

fun Player.giveItem(items: Array<ItemStack>) {
    items.forEach { 
        giveItem(it)
    }
}

fun Player.giveItem(item: Itemable, amount: Int = 0) {
    giveItem(item.getSItem(), amount)
}

/**
 * 获取 [taskProject] 的任务进度
 */
fun Player.getProgress(taskProject: TaskProject): TaskProgress = taskProject.getProgress(this)

/**
 * 获取 [task] 所在 [TaskProject] 的任务进度
 */
fun Player.getProgress(task: TaskBase): TaskProgress = getProgress(task.taskStage.taskProject)

/**
 * 完成某项任务
 * @param task 任务
 */
fun Player.completeTask(task: TaskBase, isCompleted: Boolean = true) {
    val progress = getProgress(task)
    progress.completeTask(task, isCompleted)
}

/**
 * 发送消息 (使用 '&' 作为颜色符号)
 */
fun Player.sendMsg(msg: String) {
    sendMessage(msg.replace('&', '§'))
}

fun Player.sendMsg(prefix: String, msg: String) {
    sendMessage("&f[$prefix&f] ".replace('&', '§') + msg.replace('&', '§'))
}

/**
 * 生成粒子效果
 */
fun Player.spawnParticle(particle: Particle, listLoc: List<Location>, count: Int) {
    listLoc.forEach {
        spawnParticle(particle, it, count)
    }
}

fun Player.spawnParticle(particle: Particle, listLoc: List<Location>, count: Int, offsetX: Double, offsetY: Double, offsetZ: Double) {
    listLoc.forEach {
        spawnParticle(particle, it, count, offsetX, offsetY, offsetZ)
    }
}

/**
 * 在玩家权限允许的范围内尝试放置方块
 * @param loc 放置方块的位置
 * @param clickedBlock 玩家点击的方块
 * @param item 尝试放置的物品
 * @param block 判断是否放置方块的代码块
 */
fun Player.tryToPlaceBlock(loc: Location, clickedBlock: Block, item: ItemStack, block: BlockPlaceEvent.() -> Boolean) {
    val event = BlockPlaceEvent(
        loc.block,
        loc.block.state,
        clickedBlock,
        item,
        this,
        SReflect.canBuild(world, this, loc.blockX, loc.blockZ),
        EquipmentSlot.HAND
    )
    BlockListener.tryToPlaceBlockLocations[loc.clone()] = block
    SunSTCore.pluginManager.callEvent(event)
}

fun Player.damageItemInMainHand(damage: Int = 1) {
    inventory.setItemInMainHand(inventory.itemInMainHand.damage(damage, this)) 
}

fun Player.damageItemInOffHand(damage: Int = 1) {
    inventory.setItemInOffHand(inventory.itemInOffHand.damage(damage, this))
}

fun Player.openInventoryWithSound(
    inv: Inventory,
    sound: Sound = Sound.ENTITY_HORSE_ARMOR,
    volume: Float = 1f,
    pitch: Float = 1.2f
) {
    playSound(location, sound, volume, pitch)
    openInventory(inv)
}

//endregion

//region Inventory 物品栏

/**
 * 判断物品栏中是否含有 [amount] 数量的物品 [item]
 */
fun Inventory.containsItem(item: ItemStack, amount: Int = 1): Boolean {
    if(amount <= 0) return true
    
    val theItem = item.clone()
    var cnt = theItem.amount * amount
    theItem.amount = 1
    
    storageContents.forEach {
        if(it == null) return@forEach
        
        if (it.isItemSimilar(theItem)) {
            cnt -= it.amount
            if (cnt <= 0) return true
        }
    }
    
    return false
}

fun Inventory.containsItem(items: Array<ItemStack>): Boolean {
    items.forEach { 
        if(!containsItem(it)) return false
    }
    return true
}

/**
 * 移除物品栏中 [amount] 数量的物品 [item]
 */
fun Inventory.removeSItem(item: ItemStack, amount: Int = 1): Boolean {
    if(amount <= 0) return true

    val theItem = item.clone()
    var cnt = theItem.amount * amount
    theItem.amount = 1
    
    storageContents.forEach {
        if(it == null) return@forEach

        if (it.isItemSimilar(theItem)) {
            val theCnt = cnt
            cnt -= it.amount

            if(it.amount > theCnt) it.amount -= theCnt
            else it.amount = 0
            
            if (cnt <= 0) return true
        }
    }

    return false
}

fun Inventory.removeSItem(items: Array<ItemStack>): Boolean {
    items.forEach { 
        if(!removeSItem(it)) return false
    }
    return true
}

fun Inventory.removeSItem(type: Material, amount: Int): Boolean {
    if(amount <= 0) return true
    var cnt = amount

    storageContents.forEach {
        if(it == null) return@forEach

        if (it.type == type) {
            val theCnt = cnt
            cnt -= it.amount

            if(it.amount > theCnt) it.amount -= theCnt
            else it.amount = 0

            if (cnt <= 0) return true
        }
    }

    return false
}

fun Inventory.isFull(): Boolean = firstEmpty() > size

fun Inventory.setItem(order: Int, item: Itemable) {
    setItem(order, item.getSItem())
}

fun Inventory.setItem(x: Int, y: Int, item: ItemStack) {
    setItem(x orderWith y, item)
}

fun Inventory.setItem(x: Int, y: Int, item: Itemable) {
    setItem(x orderWith y, item.getSItem())
}

/**
 * 快速创建 5*9 边框
 */
fun Inventory.createEdge(invSize: Int, edgeItem: ItemStack) {
    val meta = (if (edgeItem.hasItemMeta()) edgeItem.itemMeta else Bukkit.getItemFactory().getItemMeta(edgeItem.type)) ?: return
    meta.setDisplayName(" ")
    edgeItem.itemMeta = meta

    for(i in 0..8) {
        setItem(i, edgeItem)
        setItem(i + 9 * (invSize - 1), edgeItem)
    }
    for(i in 9..9*(invSize - 2) step 9) {
        setItem(i, edgeItem)
        setItem(i + 8, edgeItem)
    }
}

fun Inventory.setCraftSlotItem(craftOrder: Int, item: ItemStack, baseX: Int = 0, baseY: Int = 1) {
    setItem(baseX + craftOrder.toX(3), baseY + craftOrder.toY(3), item)
}

fun Inventory.setCraftSlotItem(items: Array<ItemStack>, baseX: Int = 0, baseY: Int = 1) {
    items.forEachIndexed {
            i, itemStack ->
        setCraftSlotItem(i, itemStack, baseX, baseY)
    }
}

fun Inventory.setCraftSlotItem(items: List<ItemStack>, baseX: Int = 0, baseY: Int = 1) {
    items.forEachIndexed {
            i, itemStack ->
        setCraftSlotItem(i, itemStack, baseX, baseY)
    }
}

fun Inventory.clearCraftSlotItem(baseX: Int = 0, baseY: Int = 1) {
    for(i in 0..8)
        setCraftSlotItem(i, ItemStack(Material.AIR), baseX, baseY)
}


fun InventoryView.getSPlayer(): Player = player as Player

//endregion

//region File 文件

fun File.getDataPath(plugin: JavaPlugin) 
    = absolutePath.split("(?<=${plugin.dataFolder.absolutePath.replace('\\', '/')}/)[\\s\\S]*(?=/$name)".toRegex()).last()

//endregion

//region Recipe 配方

fun ShapedRecipe.getRecipe(): Array<ItemStack> {
    val recipe = Array(9) { ItemStack(Material.AIR) }
    val rows = shape
    val ingredients = ingredientMap
    
    for(i in rows.indices){
        val base = i * 3
        val str = rows[i]
        
        for(j in str.indices){
            if(str[j] != ' '){
                val item = ingredients[str[j]] ?: continue
                recipe[base + j] = ItemStack(item.type)
            }
        }
    }
    
    return recipe
}

//endregion

//region Block 方块

fun Block.getDurability() = state.data.toItemStack(1).durability

fun Block.getFaceLocation(face: BlockFace): Location = location.getFaceLocation(face)

fun Block.getSMetadata(plugin: JavaPlugin, key: String): SMetadataValue {
    var meta = SMetadataValue(plugin, 0)
    if(hasMetadata(key)) {
        for(metadata in getMetadata(key)){
            if(metadata is SMetadataValue){
                meta = metadata
                break
            }
        }
    }
    return meta
}

fun Block.getSMetadataInt(plugin: JavaPlugin, key: String): Int = getSMetadata(plugin, key).asInt()


fun BlockFace.transform(): MutableList<BlockFace> =
    when(this) {
        NORTH, SOUTH -> arrayListOf(EAST, WEST, UP, DOWN)
        EAST, WEST -> arrayListOf(NORTH, SOUTH, UP, DOWN)
        UP, DOWN -> arrayListOf(NORTH, SOUTH, EAST, WEST)
        else -> arrayListOf()
    }

fun BlockFace.transform(excludeFace: BlockFace): MutableList<BlockFace> {
    val list = transform()
    list.remove(excludeFace)
    list.remove(excludeFace.oppositeFace)
    return list
}

//endregion

//region Location 位置

fun Location.getFaceLocation(face: BlockFace): Location = clone().add(face.modX.toDouble(), face.modY.toDouble(), face.modZ.toDouble())


fun Location.addClone(x: Int, y: Int, z: Int): Location =
    clone().add(x.toDouble(), y.toDouble(), z.toDouble())

fun Location.addClone(coord: Triple<Int, Int, Int>): Location =
    clone().add(coord.first.toDouble(), coord.second.toDouble(), coord.third.toDouble())

fun Location.addClone(y: Int): Location =
    addClone(0, y, 0)

fun Location.removeClone(x: Int, y: Int, z: Int): Location =
    clone().add(-x.toDouble(), -y.toDouble(), -z.toDouble())

fun Location.removeClone(coord: Triple<Int, Int, Int>): Location =
    clone().add(-coord.first.toDouble(), -coord.second.toDouble(), -coord.third.toDouble())

fun Location.removeClone(y: Int): Location =
    removeClone(0, y, 0)


fun Location.judgePlaneAround(type: Material, includeCorners: Boolean = false): Boolean {
    val loc = clone()
    
    loc.x = x + 1
    if(loc.block.type != type) return false

    loc.x = x - 1
    if(loc.block.type != type) return false
    loc.x = x

    loc.z = z + 1
    if(loc.block.type != type) return false

    loc.z = z - 1
    if(loc.block.type != type) return false
    loc.z = z
    
    if(includeCorners){
        loc.x = x + 1
        
        loc.z = z + 1
        if(loc.block.type != type) return false
        
        loc.z = z - 1
        if(loc.block.type != type) return false
        
        
        loc.x = x - 1

        loc.z = z + 1
        if(loc.block.type != type) return false

        loc.z = z - 1
        if(loc.block.type != type) return false

        loc.x = x
        loc.z = z
    }
    
    return true
}

fun Location.judgePlaneAround(types: List<Material>, includeCorners: Boolean = false): Boolean {
    val loc = clone()

    loc.x = x + 1
    if(!types.contains(loc.block.type)) return false

    loc.x = x - 1
    if(!types.contains(loc.block.type)) return false
    loc.x = x

    loc.z = z + 1
    if(!types.contains(loc.block.type)) return false

    loc.z = z - 1
    if(!types.contains(loc.block.type)) return false
    loc.z = z

    if(includeCorners){
        loc.x = x + 1

        loc.z = z + 1
        if(!types.contains(loc.block.type)) return false

        loc.z = z - 1
        if(!types.contains(loc.block.type)) return false


        loc.x = x - 1

        loc.z = z + 1
        if(!types.contains(loc.block.type)) return false

        loc.z = z - 1
        if(!types.contains(loc.block.type)) return false

        loc.x = x
        loc.z = z
    }

    return true
}

fun Location.countPlaneAround(type: Material, includeCorners: Boolean = false): Int {
    val loc = clone()
    var cnt = 0

    loc.x = x + 1
    if(loc.block.type == type) cnt++

    loc.x = x - 1
    if(loc.block.type == type) cnt++
    loc.x = x

    loc.z = z + 1
    if(loc.block.type == type) cnt++

    loc.z = z - 1
    if(loc.block.type == type) cnt++
    loc.z = z

    if(includeCorners){
        loc.x = x + 1

        loc.z = z + 1
        if(loc.block.type == type) cnt++

        loc.z = z - 1
        if(loc.block.type == type) cnt++


        loc.x = x - 1

        loc.z = z + 1
        if(loc.block.type == type) cnt++

        loc.z = z - 1
        if(loc.block.type == type) cnt++

        loc.x = x
        loc.z = z
    }

    return cnt
}

//endregion

//region Material 材料

fun MaterialData.getDurability(): Short = toItemStack(1).durability

//endregion

//region Metadata 自定义元数据

inline fun <reified F, reified S> MetadataValueAdapter.asPair(default: Pair<F, S>): Pair<F, S> {
    val obj = value()
    if(obj is Pair<*, *>){
        val (first, second) = obj
        if(first is F && second is S){
            return first to second
        }
    }
    
    return default
}

//endregion

//region Random 随机数

fun Random.Default.getInt(st: Int, ed: Int): Int = nextInt(st, ed + 1)

fun Random.Default.getInt(ed: Int): Int = getInt(1, ed)

fun Random.Default.isInPercent(percent: Int): Boolean = getInt(100) in 1..percent

//endregion