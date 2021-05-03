# [TaskStage](https://github.com/Sunshine-wzy/SunSTCore/blob/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/task/TaskStage.kt)   
任务阶段

## 示例
- [SkyDream-SDTask](https://github.com/Sunshine-wzy/SkyDream/blob/master/src/main/kotlin/io/github/sunshinewzy/skydream/tasks/SDTask.kt)  
```kotlin
    val stage1 = TaskStage(
        sdTaskProject,
        "<第一阶段> §6木器时代",
        3 orderWith 3,
        null,
        SItem(Material.LOG, "§f<第一阶段>", "§6木器时代","§a在这一阶段你将学会最基本的生存法则","§a并向石器时代迈进"),
        SItem(Material.STAINED_GLASS_PANE, 12, 1)
    )
    
    val stage2 = TaskStage(
        sdTaskProject,
        "<第二阶段> §7石器时代",
        4 orderWith 3,
        stage1,
        SItem(Material.COBBLESTONE, "§f<第二阶段>", "§7石器时代","§a在这一阶段你将拥有刷石机","§a并获得一些基础材料"),
        SItem(Material.STAINED_GLASS_PANE, 7, 1)
    )
    
    val stage3 = TaskStage(
        sdTaskProject,
        "<第三阶段> §d铁器时代",
        5 orderWith 3,
        stage2,
        SItem(Material.IRON_INGOT, "§f<第三阶段>","§d铁器时代","§a在这一阶段你将获得简单的矿物","§a并为蒸汽时代做准备"),
        SItem(Material.STAINED_GLASS_PANE, 6, 1)
    )
    
    val stage4 = TaskStage(
        sdTaskProject,
        "<第四阶段> §e蒸汽时代",
        6 orderWith 3,
        stage3,
        SItem(Material.CLAY_BRICK, "§f<第四阶段>", "§e蒸汽时代","§a在这一阶段你将拥有蒸汽动力","§a并进行一些简单的自动化！"),
        SItem(Material.STAINED_GLASS_PANE, 4, 1)
    )
    
    
    private fun setFinalTasks() {
        stage1.finalTask = SDTStage1.task5_1
        stage2.finalTask = SDTStage2.task5_1
        
    }
```
