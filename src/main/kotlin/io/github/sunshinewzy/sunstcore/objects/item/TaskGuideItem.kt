package io.github.sunshinewzy.sunstcore.objects.item

import io.github.sunshinewzy.sunstcore.interfaces.Itemable
import io.github.sunshinewzy.sunstcore.objects.SItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class TaskGuideItem(val item: ItemStack) : Itemable {
    BACK(SItem(Material.BARRIER, "§c返回")),
    SUBMIT(SItem(Material.SLIME_BALL, "§a>> 点击以提交任务 <<")),
    WORKBENCH(SItem(Material.CRAFTING_TABLE, "§a>> §d使用工作台合成 §a<<")),
    PAGE_NEXT(SItem(Material.ENCHANTED_BOOK, "§a下一页 ⇩")),
    PAGE_PRE(SItem(Material.ENCHANTED_BOOK, "§a上一页 ⇧")),
    HOME(SItem(Material.COMPASS, "§e⇨ §a返回主界面 §e⇦"))
    
    ;


    override fun getSItem(): ItemStack = item
    
}