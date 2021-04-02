package io.github.sunshinewzy.sunstcore.modules.task

import io.github.sunshinewzy.sunstcore.utils.castMapBoolean
import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * 任务进度
 */
class TaskProgress() : ConfigurationSerializable {
    private val progress = HashMap<String, MutableMap<String, Boolean>>()

    
    init {
        
    }
    
    constructor(map: Map<String, Any>) : this() {
        map.forEach { (key, value) ->
            if(key == "==") return@forEach
            
            val mapCast = value.castMapBoolean()
            progress[key] = mapCast
        }
    }
    
    
    override fun serialize(): Map<String, Any> {
        val map = HashMap<String, Any>()
        
        progress.forEach { (key, value) -> 
            map[key] = value
        }
        
        return map
    }
    
    
    fun completeTask(task: TaskBase, isCompleted: Boolean = true) {
        val stageName = task.taskStage.stageName
        val taskName = task.taskName

        if(progress.containsKey(stageName)){
            val stagePro = progress[stageName] ?: kotlin.run { 
                val map = HashMap<String, Boolean>()
                progress[stageName] = map
                map
            }
            stagePro[taskName] = isCompleted
        }
        else progress[stageName] = hashMapOf(taskName to isCompleted)
    }
    
    fun hasCompleteTask(task: TaskBase): Boolean {
        val taskStage = task.taskStage
        
        if(progress.containsKey(taskStage.stageName)){
            val stagePro = progress[taskStage.stageName]
            if(stagePro?.containsKey(task.taskName) == true){
                val taskPro = stagePro[task.taskName]
                if(taskPro == true){
                    return true
                }
            }
        }
        
        return false
    }
    
    fun hasCompleteStage(taskStage: TaskStage): Boolean {
        val finalTask = taskStage.finalTask ?: return true

        if(progress.containsKey(taskStage.stageName)){
            val stagePro = progress[taskStage.stageName]
            if(stagePro?.containsKey(finalTask.taskName) == true){
                val taskPro = stagePro[finalTask.taskName]
                if(taskPro == true){
                    return true
                }
            }
        }
        
        return false
    }
}