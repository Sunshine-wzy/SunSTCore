# SunSTCore
* A Bukkit core lib plugin made by SunShine Technology.
* [MCBBS发布帖](https://www.mcbbs.net/thread-1170416-1-1.html)

# [模块 - modules](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules)  
SunSTCore的核心功能

## [task](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/task)
1. [TaskProject](https://github.com/Sunshine-wzy/SunSTCore/blob/master/docs/modules/task/TaskProject.md)  
表示一个任务项目
对应一本任务书

2. [TaskStage](https://github.com/Sunshine-wzy/SunSTCore/blob/master/docs/modules/task/TaskStage.md)  
表示一个任务阶段

3. [TaskBase]  
表示一个任务基类（不可被实例化）  
子类：
    - [ItemTask](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/task/tasks/ItemTask.kt)  
    物品任务  
    提交任务时玩家背包中含有指定物品
    - [ItemCraftTask](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/task/tasks/ItemCraftTask.kt)  
    物品制作任务  
    与 ItemTask 的唯一区别是会显示该物品的合成表
    - [MachineTask](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/task/tasks/MachineTask.kt)  
    机器任务  
    搭建指定多方块机器  
    会显示多方块机器结构  
    玩家最近一次构建的机器是指定机器才能提交任务

## [machine](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/machine)

## [data](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/data)

## [energy](https://github.com/Sunshine-wzy/SunSTCore/tree/master/src/main/kotlin/io/github/sunshinewzy/sunstcore/modules/energy)
