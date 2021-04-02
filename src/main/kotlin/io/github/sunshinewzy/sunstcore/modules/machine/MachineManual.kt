package io.github.sunshinewzy.sunstcore.modules.machine

/**
 * 手动机器
 */
abstract class MachineManual(
    name: String,
    wrench: SMachineWrench,
    structure: SMachineStructure
) : SMachine(name, wrench, structure) {

    final override fun runMachine(event: SMachineRunEvent) {
        if(event is SMachineRunEvent.Manual)
            manualRun(event)
    }
    
    abstract fun manualRun(event: SMachineRunEvent.Manual)
    
}