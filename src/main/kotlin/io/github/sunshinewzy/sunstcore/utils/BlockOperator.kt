package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.Location
import org.bukkit.block.Block
import java.util.function.Consumer
import java.util.function.Function

class BlockOperator(val block: Block) {
    
    constructor(loc: Location) : this(loc.block)
    
    
    fun x(offset: Int, operator: Consumer<BlockOperator>) {
        val loc = block.location
        loc.x += offset
        operator.accept(BlockOperator(loc))
    }

    fun y(offset: Int, operator: Consumer<BlockOperator>) {
        val loc = block.location
        loc.y += offset
        operator.accept(BlockOperator(loc))
    }

    fun z(offset: Int, operator: Consumer<BlockOperator>) {
        val loc = block.location
        loc.z += offset
        operator.accept(BlockOperator(loc))
    }

    fun block(operator: Block.() -> Unit) {
        operator(block)
    }

    /**
     * 空间四周六个面
     */
    fun surroundings(operation: Function<Block, Boolean>): Boolean {
        var flag = false
        
        x(1) {
            flag = operation.apply(it.block)
        }
        if(flag) return true
        
        x(-1) {
            flag = operation.apply(it.block)
        }
        if(flag) return true

        y(1) {
            flag = operation.apply(it.block)
        }
        if(flag) return true

        y(-1) {
            flag = operation.apply(it.block)
        }
        if(flag) return true

        z(1) {
            flag = operation.apply(it.block)
        }
        if(flag) return true

        z(-1) {
            flag = operation.apply(it.block)
        }
        if(flag) return true
        
        return false
    }

    /**
     * 水平面四周
     * 
     * @param around 是否包含四个角
     */
    @JvmOverloads
    fun horizontal(around: Boolean = false, operation: Function<Block, Boolean>) {
        var flag = false

        x(1) { operator ->
            flag = operation.apply(operator.block)

            if(around && !flag) {
                operator.z(1) {
                    flag = operation.apply(it.block)
                }
                if(flag) return@x
                operator.z(-1) {
                    flag = operation.apply(it.block)
                }
            }
        }
        if(flag) return

        x(-1) { operator ->
            flag = operation.apply(operator.block)

            if(around && !flag) {
                operator.z(1) {
                    flag = operation.apply(it.block)
                }
                if(flag) return@x
                operator.z(-1) {
                    flag = operation.apply(it.block)
                }
            }
        }
        if(flag) return

        z(1) {
            flag = operation.apply(it.block)
        }
        if(flag) return

        z(-1) {
            flag = operation.apply(it.block)
        }
    }
    
    
    companion object {
        @JvmStatic
        fun Block.operate(operator: Consumer<BlockOperator>) {
            operator.accept(BlockOperator(this))
        }
        
        @JvmStatic
        fun Location.operate(operator: Consumer<BlockOperator>) {
            operator.accept(BlockOperator(this))
        }
    }
    
}