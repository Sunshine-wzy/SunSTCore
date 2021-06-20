package io.github.sunshinewzy.sunstcore.objects.item.constructionstick

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.utils.*
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.block.Action
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.NumberConversions

/**
 * 范围型建筑手杖
 * @param radius 建筑手杖的作用半径
 */
class RangeStick(item: ItemStack, val radius: Int) : ConstructionStick(item) {
    
    init {
        sticks.add(this)
        
        addAction {
            if(clickedBlock != null && action == Action.RIGHT_CLICK_BLOCK && hand == EquipmentSlot.HAND){
                if(player.location.add(0.0, 1.0, 0.0).block.type != Material.AIR) return@addAction

                val block = player.getTargetBlock(null, 5)
                if(block.type == Material.AIR) return@addAction

                val lastBlocks = player.getLastTwoTargetBlocks(null, 5)
                val face = lastBlocks[1].getFace(lastBlocks[0]) ?: return@addAction
                block.getRelative(face)
                val setLoc = hashSetOf(block.location)

                face.transform().forEach {
                    judgeCircle(block.location, it, face, radius, setLoc)
                }

                player.spawnStickSelectParticle(setLoc, face)
                setLoc.forEach {
                    val placeLoc = it.getFaceLocation(face)
                    val itemBlock = ItemStack(block.type)
                    
                    player.tryToPlaceBlock(placeLoc, block, itemBlock) {
                        if(player.gameMode == GameMode.SURVIVAL || player.gameMode == GameMode.ADVENTURE){
                            val inv = player.inventory
                            if(inv.containsItem(itemBlock)){
                                inv.removeSItem(itemBlock)
                                player.damageItemInMainHand()
                                return@tryToPlaceBlock true
                            }

                            return@tryToPlaceBlock false
                        }
                        
                        true
                    }
                }
                
                isCancelled = true
                return@addAction
            }
        }
    }

    constructor(map: Map<String, Any>) : this(deserialize(map), NumberConversions.toInt(map["radius"]))

    
    override fun checkFirstRun() {
        if(sticks.isEmpty()){
            Bukkit.getScheduler().runTaskTimer(SunSTCore.plugin, Runnable {
                if(sticks.isEmpty()) return@Runnable

                Bukkit.getServer().onlinePlayers.forEach players@{ player ->
                    val handItem = player.inventory.itemInMainHand
                    if(handItem.type == Material.AIR || player.location.add(0.0, 1.0, 0.0).block.type != Material.AIR) return@players

                    sticks.forEach { stick ->
                        if(!handItem.isItemSimilar(stick)) return@forEach

                        val block = player.getTargetBlock(null, 5)
                        if(block.type == Material.AIR) return@forEach

                        val lastBlocks = player.getLastTwoTargetBlocks(null, 5)
                        if(lastBlocks.size < 2) return@forEach
                        val face = lastBlocks[1].getFace(lastBlocks[0]) ?: return@forEach
                        block.getRelative(face)
                        val setLoc = hashSetOf(block.location)

                        face.transform().forEach {
                            judgeCircle(block.location, it, face, stick.radius, setLoc)
                        }

                        player.spawnStickSelectParticle(setLoc, face)
                        return@players
                    }

                }
            }, period, period)
        }
    }

    override fun serialize(): MutableMap<String, Any> {
        val map = super.serialize()
        map["radius"] = radius
        return map
    }

    companion object {
        private val sticks = ArrayList<RangeStick>()

        
        /**
         * 建筑手杖检测周期(默认: 10tick)
         */
        var period = 10L

        private fun judgeLine(loc: Location, face: BlockFace, distance: Int, setLoc: MutableSet<Location>) {
            if(distance <= 0) return
            val nextLoc = loc.getFaceLocation(face)

            if(SBlock(loc).isSimilar(nextLoc)){
                setLoc.add(nextLoc)

                judgeLine(nextLoc, face, distance - 1, setLoc)
            }
        }

        private fun judgeCircle(loc: Location, face: BlockFace, excludeFace: BlockFace, distance: Int, setLoc: MutableSet<Location>) {
            if(distance <= 0) return
            val nextLoc = loc.getFaceLocation(face)

            if(SBlock(loc).isSimilar(nextLoc)){
                setLoc.add(nextLoc)

                face.transform(excludeFace).forEach {
                    judgeLine(nextLoc, it, distance, setLoc)
                }

                judgeCircle(nextLoc, face, excludeFace, distance - 1, setLoc)
            }
        }
    }

}