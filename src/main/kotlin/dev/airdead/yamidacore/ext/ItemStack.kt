package dev.airdead.yamidacore.ext

import io.papermc.paper.persistence.PersistentDataContainerView
import org.bukkit.inventory.ItemStack

fun ItemStack.isPotion(): Boolean {
    return this.type.name.endsWith("POTION")
}

val ItemStack.pdc: PersistentDataContainerView
    get() = this.persistentDataContainer