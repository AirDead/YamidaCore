package dev.airdead.yamidacore.modules.listeners

import dev.airdead.yamidacore.components.modules.PluginListener
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.ext.isPotion
import dev.airdead.yamidacore.ext.pdc
import kotlinx.serialization.Serializable
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class AlcoholListener(override val app: YamidaPlugin) : PluginListener() {
    private val alcohol = mutableMapOf<UUID, Int>()
    private lateinit var config: AlcoholConfig

    override suspend fun onEnable() {
        alcohol.clear()
        config = app.loadConfig("alcohol")
    }

    override suspend fun onDisable() = alcohol.clear()
    override suspend fun onReload() {
        alcohol.clear()
        config = app.loadConfig("alcohol")
    }

    @EventHandler
    fun onPotionDrink(event: PlayerItemConsumeEvent) {
        val item = event.item
        if (!item.isPotion()) return

        val player = event.player
        val alcoholLevel = item.pdc.get(NamespacedKey(app, "degrees_of_alcohol"), PersistentDataType.INTEGER) ?: 0
        alcohol[player.uniqueId] = alcoholLevel

        when {
            alcoholLevel >= 120 -> {
                player.damage(player.health)
                player.sendMessage(config.messages.death)
            }
            alcoholLevel >= 115 -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 150, 1))
                player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 150, 1))
                player.sendMessage(config.messages.blindness)
            }
            alcoholLevel >= 95 -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 100, 1))
                player.sendMessage(config.messages.wither)
            }
            alcoholLevel >= 90 -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 0))
                player.sendMessage(config.messages.nausea)
            }
        }
        event.replacement = ItemStack(Material.AIR)
    }
}

@Serializable
data class AlcoholConfig(
    val data: List<AlcoholData>,
    val messages: AlcoholMessages
)

@Serializable
data class AlcoholData(
    val name: String,
    val level: Int
)

@Serializable
data class AlcoholMessages(
    val overdose: String,
    val death: String,
    val blindness: String,
    val wither: String,
    val nausea: String
)