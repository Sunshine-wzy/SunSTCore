package io.github.sunshinewzy.sunstcore.utils

import io.izzel.taboolib.kotlin.SingleListener
import org.bukkit.event.Event

object SEventSubscriber {
    
    private val subscribers = HashMap<String, ArrayList<SEventSubscriberBuilder<out Event>>>()
    
    
    fun <E: Event> subscribeEvent(eventClass: Class<out E>, block: E.() -> Unit) {
        val name = eventClass.name
        val eventBlock = SEventSubscriberBuilder(block)
        val list = subscribers[name]
        
        if(list == null){
            SingleListener.listen(eventClass) {
                callSubscribeEvent(it)
            }
            subscribers[name] = arrayListOf(eventBlock)
        }
        else list += eventBlock
    }
    
    fun <E: Event> callSubscribeEvent(event: E) {
        val list = subscribers[event.javaClass.name]
        list?.forEach { 
            @Suppress("UNCHECKED_CAST")
            val block = it.block as E.() -> Unit
            block(event)
        }
    }
    
}

class SEventSubscriberBuilder<E: Event> internal constructor(
    val block: E.() -> Unit
)


inline fun <reified E: Event> subscribeEvent(noinline block: E.() -> Unit) = subscribeEvent(E::class.java, block)

fun <E: Event> subscribeEvent(
    eventClass: Class<out E>,
    block: E.() -> Unit
) {
    SEventSubscriber.subscribeEvent(eventClass, block)
}
