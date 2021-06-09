package io.github.sunshinewzy.sunstcore.enums

import io.github.sunshinewzy.sunstcore.interfaces.Materialsable
import org.bukkit.Material
import org.bukkit.Material.*

enum class SMaterial(val types: List<Material>): Materialsable {
    FENCE(OAK_FENCE, ACACIA_FENCE, BIRCH_FENCE, CRIMSON_FENCE, DARK_OAK_FENCE, JUNGLE_FENCE, NETHER_BRICK_FENCE, SPRUCE_FENCE, WARPED_FENCE),
    FENCE_WOOD(OAK_FENCE, ACACIA_FENCE, BIRCH_FENCE, CRIMSON_FENCE, DARK_OAK_FENCE, JUNGLE_FENCE, SPRUCE_FENCE),
    
    WOOD(OAK_WOOD, JUNGLE_WOOD, ACACIA_WOOD, BIRCH_WOOD, DARK_OAK_WOOD, SPRUCE_WOOD),
    
    WOOL(BLACK_WOOL, BLUE_WOOL, BROWN_WOOL, CYAN_WOOL, GRAY_WOOL, GREEN_WOOL, LIGHT_BLUE_WOOL, LIGHT_GRAY_WOOL, LIME_WOOL, MAGENTA_WOOL, ORANGE_WOOL, PINK_WOOL, PURPLE_WOOL, RED_WOOL, WHITE_WOOL, YELLOW_WOOL),
    
    LEAVES(ACACIA_LEAVES, BIRCH_LEAVES, DARK_OAK_LEAVES, JUNGLE_LEAVES, OAK_LEAVES, SPRUCE_LEAVES),
    
    LOG(ACACIA_LOG, BIRCH_LOG, DARK_OAK_LOG, JUNGLE_LOG, OAK_LOG, SPRUCE_LOG)
    
    ;
    
    
    constructor(vararg types: Material) : this(types.toList())


    override fun types(): List<Material> = types

    companion object {
        val leavesToSapling = mapOf(ACACIA_LEAVES to ACACIA_SAPLING, BIRCH_LEAVES to BIRCH_SAPLING, DARK_OAK_LEAVES to DARK_OAK_SAPLING, JUNGLE_LEAVES to JUNGLE_SAPLING, OAK_LEAVES to OAK_SAPLING, SPRUCE_LEAVES to SPRUCE_SAPLING)
        
        
        fun Material.getSapling(): Material =
            leavesToSapling[this] ?: OAK_SAPLING
    }
    
}