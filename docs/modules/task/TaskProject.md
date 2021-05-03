# TaskProject  
表示一个任务项目  
对应一本任务书

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
任务书标题  
- edgeItem  
边框物品  
默认为白色染色玻璃板  
- openSound, volume, pitch  
打开任务书的音效、响度、音调
- invSize  
任务书界面行数
默认为5（即5*9格）
## 函数
-     getProgress(p: Player): TaskProgress  
获取玩家的任务进度
