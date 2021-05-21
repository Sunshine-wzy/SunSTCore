package io.github.sunshinewzy.sunstcore.test

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.modules.machine.SSingleMachine
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.objects.SMenu
import io.github.sunshinewzy.sunstcore.utils.getSPlayer
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object Millstone : SSingleMachine(SunSTCore.plugin, "Millstone", SItem(Material.STONE_BRICKS)) {

    private val millstoneMenu = SMenu("Millstone", "Millstone", 3)

    init {
        millstoneMenu.setButton(1, 1, SItem(Material.STONE_BRICKS), "Brick") {
            val player = view.getSPlayer()
            player.sendMsg("awa")
        }
    }


    override fun onClick(sLocation: SLocation, event: PlayerInteractEvent) {
        millstoneMenu.openInventory(event.player)
        
    }
}