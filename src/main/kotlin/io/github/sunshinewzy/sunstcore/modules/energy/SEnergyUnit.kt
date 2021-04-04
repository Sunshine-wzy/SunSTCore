package io.github.sunshinewzy.sunstcore.modules.energy

/**
 * 能量单位
 * 
 * 作为能量实体的单位
 * 必须可进行加减运算
 * 必须实现到 [SunSTEnergy] 的等价转换
 */
interface SEnergyUnit {
    
    operator fun plus(unit: SEnergyUnit)
    
    operator fun minus(unit: SEnergyUnit)
    
    fun toSunSTEnergy(): SunSTEnergy
    
}