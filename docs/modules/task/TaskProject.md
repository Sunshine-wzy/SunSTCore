# TaskProject 表示一个任务项目

## 参数
- projectName  
任务项目名称
- openItem  
用于打开该任务项目的物品，即任务书（右键该物品就能打开该任务项目） 
默认是名称为 "§e[projectName] §a向导" 的附魔书
- isFirstJoinGive  
玩家第一次加入游戏时是否给予该项目的任务书  
默认为true（给予）
- title  


```kotlin
    val projectName: String,
    val openItem: ItemStack = SItem(Material.ENCHANTED_BOOK, "§e$projectName §a向导"),
    val isFirstJoinGive: Boolean = true,
    val title: String,
    val edgeItem: ItemStack = SItem(Material.WHITE_STAINED_GLASS_PANE),
    val openSound: Sound = Sound.ENTITY_HORSE_ARMOR,
    val volume: Float = 1f,
    val pitch: Float = 1.2f,
    val invSize: Int = 5
```
