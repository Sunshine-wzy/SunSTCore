package io.github.sunshinewzy.sunstcore.objects

class SBlockMap : HashMap<Char, SBlock>() {
    
    fun set(key: Char, sBlock: SBlock): SBlockMap {
        put(key, sBlock)
        return this
    }
    
}