package io.github.sunshinewzy.sunstcore.modules.machine

import io.github.sunshinewzy.sunstcore.objects.SLocation

class SSingleMachine(val name: String) {
    
    
    
    
    companion object {
        /**
         * 所有机器的位置
         */
        private val machines = HashMap<SLocation, SSingleMachine>()
        
    }
    
}