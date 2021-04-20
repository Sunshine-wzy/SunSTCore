package io.github.sunshinewzy.sunstcore.utils

import org.bukkit.Location
import org.bukkit.block.Block

class BlockOperator(val block: Block) {
    
    constructor(loc: Location) : this(loc.block)
    
    
    fun x(offset: Int, operator: BlockOperator.() -> Unit) {
        val loc = block.location
        loc.x += offset
        operator(BlockOperator(loc))
    }
    
    fun y(offset: Int, operator: BlockOperator.() -> Unit) {
        val loc = block.location
        loc.y += offset
        operator(BlockOperator(loc))
    }
    
    fun z(offset: Int, operator: BlockOperator.() -> Unit) {
        val loc = block.location
        loc.z += offset
        operator(BlockOperator(loc))
    }
    
    fun block(operator: Block.() -> Unit) {
        operator(block)
    }
    
    fun surroundings(operation: Block.() -> Boolean) {
        var flag = false
        
        x(1) {
            flag = operation(block)
        }
        if(flag) return
        
        x(-1) {
            flag = operation(block)
        }
        if(flag) return

        y(1) {
            flag = operation(block)
        }
        if(flag) return

        y(-1) {
            flag = operation(block)
        }
        if(flag) return

        z(1) {
            flag = operation(block)
        }
        if(flag) return

        z(-1) {
            flag = operation(block)
        }
    }
    
    fun horizontal(around: Boolean = false, operation: Block.() -> Boolean) {
        var flag = false

        x(1) {
            flag = operation(block)

            if(around && !flag) {
                z(1) {
                    flag = operation(block)
                }

                z(-1) {
                    flag = operation(block)
                }
            }
        }
        if(flag) return

        x(-1) {
            flag = operation(block)

            if(around && !flag) {
                z(1) {
                    flag = operation(block)
                }

                z(-1) {
                    flag = operation(block)
                }
            }
        }
        if(flag) return

        z(1) {
            flag = operation(block)
        }
        if(flag) return

        z(-1) {
            flag = operation(block)
        }
    }
    
    
    companion object {
        fun Block.operate(operator: BlockOperator.() -> Unit) {
            operator(BlockOperator(this))
        }
        
        fun Location.operate(operator: BlockOperator.() -> Unit) {
            operator(BlockOperator(this))
        }
    }
    
}