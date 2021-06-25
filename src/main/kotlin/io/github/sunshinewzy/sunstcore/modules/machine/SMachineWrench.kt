package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.events.smachine.SMachineAddEvent
import io.github.sunshinewzy.sunstcore.events.smachine.SMachineRemoveEvent
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.modules.machine.SMachine.Companion.getSMachine
import io.github.sunshinewzy.sunstcore.modules.machine.SMachine.Companion.hasSMachine
import io.github.sunshinewzy.sunstcore.modules.machine.SMachine.Companion.judgeSMachineStructure
import io.github.sunshinewzy.sunstcore.objects.SBlock
import io.github.sunshinewzy.sunstcore.objects.SItem
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import io.github.sunshinewzy.sunstcore.utils.subscribeEvent
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class SMachineWrench(val plugin: JavaPlugin, item: ItemStack) : SItem(item) {
    private val machines = HashMap<SBlock, ArrayList<SMachine>>()
    
    var prefix = "&b扳手"
    var msgDestroy = "&c多方块机器已被破坏！"
    var msgAlreadyExist = "&e这里已经有多方块机器了~"
    var msgBuildSuccessful = "&a构建成功！"
    var msgIncorrectStructure = "&f多方块机器&c结构不正确"
    var msgMachineUpgrade = "&a多方块机器升级成功！"
    
    
    init {
        addAction { 
            val clickedBlock = clickedBlock
            if(action == Action.RIGHT_CLICK_BLOCK && hand == EquipmentSlot.HAND && clickedBlock != null && clickedBlock.type != Material.AIR){
                isCancelled = true
                
                val loc = clickedBlock.location
                if(loc.hasSMachine()){
                    if(loc.judgeSMachineStructure(player, true)){
                        player.playSound(loc, Sound.BLOCK_PISTON_CONTRACT, 1f, 1.5f)
                        player.sendMsg(prefix, msgAlreadyExist)
                    }
                    
                    return@addAction
                }
                
                machines.forEach machines@{ (sBlock, listMachine) -> 
                    if(!sBlock.isSimilar(clickedBlock)) return@machines
                    
                    listMachine.forEach machine@{ sMachine -> 
                        if(sMachine.judgeStructure(loc, true)){
                            sMachine.addMachine(loc, player)
                            
                            loc.world?.playEffect(loc, Effect.ENDER_SIGNAL, 1)
                            loc.world?.playEffect(loc, Effect.CLICK1, 1)
                            player.sendMsg(sMachine.name, msgBuildSuccessful)
                            return@addAction
                        }
                    }
                }

                player.playEffect(loc, Effect.STEP_SOUND, 1)
                player.sendMsg(prefix, msgIncorrectStructure)
            }
        }
    }
    
    
    fun addMachine(machine: SMachine) {
        val centerBlock = machine.structure.centerBlock
        if(machines.containsKey(centerBlock)){
            val listMachine = machines[centerBlock] ?: kotlin.run {
                machines[centerBlock] = arrayListOf(machine)
                return
            }
            
            listMachine.add(machine)
        } else machines[centerBlock] = arrayListOf(machine)
    }
    
    
    companion object : Initable {
        private val playerLastAddMachine = HashMap<UUID, String>()
        
        
        override fun init() {
            subscribeEvent<PlayerInteractEvent> { 
                val clickedBlock = clickedBlock ?: return@subscribeEvent
                
                if(action == Action.RIGHT_CLICK_BLOCK && hand == EquipmentSlot.HAND && clickedBlock.type != Material.AIR){
                    val loc = clickedBlock.location
                    val machine = loc.getSMachine()
                    if(machine != null){
                        if(loc.judgeSMachineStructure(player)){
                            if(machine is SMachineManual){
                                machine.runMachine(SMachineRunEvent.Manual(loc, player))
                            }
                            
                            if(machine.isCancelInteract)
                                isCancelled = true
                        }
                    }
                }
            }
            
            subscribeEvent<SMachineAddEvent> {
                sMachine.sMachines[SLocation(loc)] = SMachineInformation(player.uniqueId.toString())
                
                playerLastAddMachine[player.uniqueId] = sMachine.id
                
            }
            
            subscribeEvent<SMachineRemoveEvent> { 
                sMachine.sMachines.remove(SLocation(loc))
                
            }
            
        }
        
        fun Player.getLastAddMachine(): String =
            if(playerLastAddMachine.containsKey(uniqueId))
                playerLastAddMachine[uniqueId] ?: ""
            else ""
    }
}