package io.github.sunshinewzy.sunstcore.objects.inventoryholder

open class SPartProtectInventoryHolder<T>(val allowClickSlots: MutableList<Int>, data: T) : SProtectInventoryHolder<T>(data)