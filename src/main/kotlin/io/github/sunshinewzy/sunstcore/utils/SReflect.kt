package io.github.sunshinewzy.sunstcore.utils

import io.github.sunshinewzy.sunstcore.interfaces.Initable
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object SReflect : Initable {
    val version = Bukkit.getServer().javaClass.getPackage().name.replace(".", ",").split(",")[3]
    val nms = "net.minecraft.server.$version"
    val obc = "org.bukkit.craftbukkit.$version"
    
    val classItemStack: Class<*> = Class.forName("$nms.ItemStack")
    val classEntityPlayer: Class<*> = Class.forName("$nms.EntityPlayer")
    val classEntityLiving: Class<*> = Class.forName("$nms.EntityLiving")
    
    val classCraftEventFactory: Class<*> = Class.forName("$obc.event.CraftEventFactory")
    val classWorld: Class<*> = Class.forName("$obc.CraftWorld")
    val classCraftPlayer: Class<*> = Class.forName("$obc.entity.CraftPlayer")
    val classCraftItemStack: Class<*> = Class.forName("$obc.inventory.CraftItemStack")

    
    val methodCanBuild = classCraftEventFactory.getDeclaredMethod("canBuild", classWorld, Player::class.java, Int::class.java, Int::class.java).apply {
        isAccessible = true
    }
    val methodGetEntityPlayer = classCraftPlayer.getMethod("getHandle")
    val methodItemDamage = classItemStack.getMethod("damage", Int::class.java, classEntityLiving)
    val methodItemAsNMSCopy = classCraftItemStack.getMethod("asNMSCopy", ItemStack::class.java)
    val methodItemAsBukkitCopy = classCraftItemStack.getMethod("asBukkitCopy", classItemStack)
    
    
    override fun init() {
        
    }
    
    
    fun canBuild(world: World, player: Player, x: Int, z: Int): Boolean
        = methodCanBuild.invoke(null, classWorld.cast(world), player, x, z) as Boolean
    
    fun Player.getCraftPlayer(): Any = classCraftPlayer.cast(this)
    
    fun Player.getEntityPlayer(): Any = methodGetEntityPlayer.invoke(getCraftPlayer())
    
    fun ItemStack.asNMSCopy(): Any = methodItemAsNMSCopy.invoke(null, this)
    
    fun ItemStack.damage(damage: Int, player: Player): ItemStack {
        val nmsItem = asNMSCopy()
        methodItemDamage.invoke(nmsItem, damage, player.getEntityPlayer())
        return methodItemAsBukkitCopy(null, nmsItem) as ItemStack
    }
    
}