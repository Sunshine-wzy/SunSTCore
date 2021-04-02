package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.objects.inventoryholder.SProtectInventoryHolder

class TaskInventoryHolder(task: TaskBase, var value: Int = 0, var max: Int = 0) : SProtectInventoryHolder<Triple<String, String, String>>(
    Triple(
        task.taskStage.taskProject.projectName,
        task.taskStage.stageName,
        task.taskName
    )
) {
    
    fun getPage(): Int {
        if(value > 0){
            if(max in 1 until value){
                value = 1
            }
        }
        else value = 1

        return value
    }
    
    fun nextPage(): Int {
        val page = getPage()
        
        if(page < max) value++
        else value = 1
        
        return value
    }
    
    fun prePage(): Int {
        val page = getPage()
        
        if(page > 1) value--
        else value = max
        
        return value
    }
    
    fun toPage(index: Int): Int {
        val page = getPage()
        
        if(index in 1..max) value = index
        
        return value
    }
}