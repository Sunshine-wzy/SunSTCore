package io.github.sunshinewzy.sunstcore.objects

import org.bukkit.Material

object SCollection {
    
    val materials = Material.values()

    
    fun matchMaterials(name: String, isEndsWith: Boolean = true): List<Material> {
        val list = arrayListOf<Material>()

        if(isEndsWith) {
            materials.forEach {
                if(it.name.endsWith(name, true)) {
                    list += it
                }
            }
        } else {
            materials.forEach {
                if(it.name.contains(name, true)) {
                    list += it
                }
            }
        }

        return list
    }
    
}