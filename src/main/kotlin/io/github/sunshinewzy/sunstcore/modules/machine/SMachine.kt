package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.SunSTCore
import io.github.sunshinewzy.sunstcore.events.smachine.SMachineAddEvent
import io.github.sunshinewzy.sunstcore.events.smachine.SMachineRemoveEvent
import io.github.sunshinewzy.sunstcore.interfaces.Initable
import io.github.sunshinewzy.sunstcore.modules.data.sunst.SMachineData
import io.github.sunshinewzy.sunstcore.objects.SLocation
import io.github.sunshinewzy.sunstcore.objects.SLocation.Companion.toSLocation
import io.github.sunshinewzy.sunstcore.utils.SunSTTestApi
import io.github.sunshinewzy.sunstcore.utils.getSMetadata
import io.github.sunshinewzy.sunstcore.utils.removeClone
import io.github.sunshinewzy.sunstcore.utils.sendMsg
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player

/**
 * 多方块机器
 * 
 * @param wrench 构建该多方块机器的扳手
 * @param structure 该多方块机器的结构
 */
abstract class SMachine(
    val name: String,
    val wrench: SMachineWrench,
    val structure: SMachineStructure
) : Initable {
    val sMachines = HashMap<SLocation, SMachineInformation>()
    
    
    init {
        wrench.addMachine(this)

        SMachineData(this)
    }


    /**
     * 运行机器
     * 
     * 如果您不在新建一个 像 [SMachineManual] 和 [SMachineTimer] 的 [SMachine] API，请不要重写此函数。
     * 请优先重写函数 [SMachineManual.manualRun] 或 [SMachineTimer.timerRun]
     * 
     * If you are not writing a new [SMachine] API like [SMachineManual] and [SMachineTimer] , please do not override this function.
     * Override the function [SMachineManual.manualRun] or [SMachineTimer.timerRun] first.
     */
    abstract fun runMachine(event: SMachineRunEvent)


    /**
     * 多方块机器结构判定
     * 
     * @param loc 机器中心位置
     */
    fun judgeStructure(loc: Location, isFirst: Boolean = false, level: Short = 0): Boolean {
        val baseLoc = loc.removeClone(structure.center)
        
        if(structure.judgeStructure(baseLoc, level))
            return specialJudge(baseLoc, isFirst, level)
        
        return false
    }

    /**
     * 多方块机器结构特判
     * 在通过一般结构判定后调用
     * 
     * @param loc 机器基准位置，一般为机器底部中心位置
     */
    open fun specialJudge(loc: Location, isFirst: Boolean, level: Short = 0): Boolean = true


    fun getOwner(sLocation: SLocation): String = sMachines[sLocation]?.owner ?: ""
    
    /**
     * 添加机器
     */
    fun addMachine(loc: Location, player: Player) {
        val sLoc = loc.toSLocation()
        allSMachines[sLoc] = this
        SunSTCore.pluginManager.callEvent(SMachineAddEvent(this, loc, player))
    }
    
    fun addMachine(sLocation: SLocation, information: SMachineInformation = SMachineInformation()) {
        allSMachines[sLocation] = this
        sMachines[sLocation] = information
    }

    /**
     * 移除机器
     */
    fun removeMachine(loc: Location) {
        val sLoc = SLocation(loc)
        
        if(allSMachines.containsKey(sLoc)){
            val machine = allSMachines[sLoc] ?: kotlin.run { 
                allSMachines.remove(sLoc)
                return
            }
            
            if(machine.name == name) {
                allSMachines.remove(sLoc)
                SunSTCore.pluginManager.callEvent(SMachineRemoveEvent(machine, loc))
            }
        }
    }
    
    override fun init() {
        
    }

    /**
     * 增加 [block] 的 Metadata 中的整型数据直至 [maxCnt]
     * 
     * 若增加前 Metadata 中的数据:
     * 
     * · == 0 -> 设置为 1 并返回 [SMachineStatus.START]
     * 
     * · >= 1 -> 增加 [addCnt] 并返回 [SMachineStatus.RUNNING]
     * 
     * · >= [maxCnt] -> 设置为 [addCnt] 并返回 [SMachineStatus.FINISH]
     */
    protected fun addMetaCnt(block: Block, maxCnt: Int, addCnt: Int = 1): SMachineStatus {
        val meta = block.getSMetadata(wrench.plugin, name)
        var cnt = meta.asInt()
        
        val status = when {
            cnt >= maxCnt -> {
                cnt = 0
                SMachineStatus.FINISH
            }
            cnt >= 1 -> {
                cnt += addCnt
                SMachineStatus.RUNNING
            }
            else -> {
                cnt = addCnt
                SMachineStatus.START
            }
        }
        
        meta.data = cnt
        block.setMetadata(name, meta)
        return status
    }
    
    protected fun addMetaCnt(loc: Location, maxCnt: Int, addCnt: Int = 1): SMachineStatus =
        addMetaCnt(loc.block, maxCnt, addCnt)
    
    protected fun addMetaCnt(event: SMachineRunEvent, maxCnt: Int, addCnt: Int = 1): SMachineStatus =
        addMetaCnt(event.loc, maxCnt, addCnt)

    /**
     * 设置 [block] 的 Metadata 中的整型数据
     */
    protected fun setMetaCnt(block: Block, cnt: Int) {
        val meta = block.getSMetadata(wrench.plugin, name)
        meta.data = cnt
        block.setMetadata(name, meta)
    }
    
    protected fun setMetaCnt(loc: Location, cnt: Int) {
        setMetaCnt(loc.block, cnt)
    }
    
    protected fun setMetaCnt(event: SMachineRunEvent, cnt: Int) {
        setMetaCnt(event.loc, cnt)
    }


    /**
     * 数据
     */
    fun setData(sLocation: SLocation, key: String, value: Any): Boolean {
        sMachines[sLocation]?.data?.let { information ->
            information[key] = value
            return true
        }

        return false
    }

    fun removeData(sLocation: SLocation, key: String) {
        sMachines[sLocation]?.data?.remove(key)
    }

    fun clearData(sLocation: SLocation) {
        sMachines[sLocation]?.data?.clear()
    }

    fun getData(sLocation: SLocation, key: String): Any? {
        sMachines[sLocation]?.data?.let { data ->
            if(data.containsKey(key)) {
                return data[key]
            }
        }
        return null
    }

    fun getDataOrFail(sLocation: SLocation, key: String): Any =
        getData(sLocation, key) ?: throw IllegalArgumentException("The SLocation '${toString()}' doesn't have SMachine($name) data of $key.")

    inline fun <reified T> getDataByType(sLocation: SLocation, key: String): T? {
        sMachines[sLocation]?.data?.let { data ->
            if(data.containsKey(key)) {
                data[key]?.let {
                    if(it is T) {
                        return it
                    }
                }
            }
        }
        return null
    }

    inline fun <reified T> getDataByTypeOrFail(sLocation: SLocation, key: String): T {
        getDataOrFail(sLocation, key).let {
            if(it is T) {
                return it
            }
        }

        throw IllegalArgumentException("Cannot cast data of $key to ${T::class.java.name}.")
    }



    /**
     * 在 [loc] 位置生成该机器的结构 (不会构建机器)
     */
    @SunSTTestApi
    fun buildMachine(loc: Location) {
        var theLoc: Location
        structure.structure.forEach { (coord, sBlock) -> 
            theLoc = loc.clone()
            theLoc.add(coord.x.toDouble(), coord.y.toDouble(), coord.z.toDouble())
            
            sBlock.setLocation(theLoc)
        }
    }
    
    
    companion object {
        /**
         * 所有机器的位置
         */
        private val allSMachines = HashMap<SLocation, SMachine>()


        fun Location.hasSMachine(): Boolean =
            allSMachines.containsKey(toSLocation())
        
        fun Location.getSMachine(): SMachine? {
            val sLoc = toSLocation()

            if(allSMachines.containsKey(sLoc)){
                return allSMachines[sLoc]
            }

            return null
        }
        
        fun Location.judgeSMachineStructure(player: Player, isWrench: Boolean = false): Boolean {
            val sLoc = SLocation(this)

            if(allSMachines.containsKey(sLoc)){
                val machine = allSMachines[sLoc] ?: kotlin.run {
                    allSMachines.remove(sLoc)
                    return true
                }
                
                if(!machine.judgeStructure(this)){
                    player.sendMsg(machine.wrench.msgDestroy)
                    allSMachines.remove(sLoc)
                    world?.playSound(this, Sound.ENTITY_ITEM_BREAK, 1f, 0.2f)

                    SunSTCore.pluginManager.callEvent(SMachineRemoveEvent(machine, this))
                    return false
                }
            }

            return true
        }
    }
    
}