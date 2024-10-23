package dev.airdead.yamidacore.listeners

import dev.airdead.yamidacore.components.modules.PluginListener
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.ext.removePrefix
import dev.airdead.yamidacore.ext.startsWith
import dev.airdead.yamidacore.ext.toComponent
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

class ChatListener(override val app: YamidaPlugin) : PluginListener() {
    val emojiMap = mutableMapOf<String, String>()

    override suspend fun onEnable() {
        val config = app.loadConfig<EmojiConfig>("emojis")
        emojiMap.putAll(config.emojis)
    }

    override suspend fun onDisable() {
        emojiMap.clear()
    }

    override suspend fun onReload() {
        emojiMap.clear()
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        var message = event.message()

        val isGlobal = message.startsWith("!")
        if (isGlobal) {
            message = message.removePrefix("!").toComponent()
        }

        message = replaceEmojisInComponent(message)
        event.message(message)

        if (!isGlobal) {
            event.isCancelled = true
            app.scheduler.runTask {
                val localRecipients = Bukkit.getOnlinePlayers().filter {
                    it.world == player.world && it.location.distance(player.location) <= 75
                }
                val renderedMessage = renderMessage(player, message, isGlobal)
                localRecipients.forEach { it.sendMessage(renderedMessage) }
            }
        } else {
            event.renderer { _, _, msg, _ ->
                renderMessage(player, msg, isGlobal)
            }
        }
    }

    fun replaceEmojisInComponent(component: Component): Component {
        var updatedComponent = component
        emojiMap.forEach { (key, value) ->
            updatedComponent = updatedComponent.replaceText { builder ->
                builder.matchLiteral(key).replacement(Component.text(value))
            }
        }
        return updatedComponent
    }

    fun renderMessage(source: Player, message: Component, isGlobal: Boolean): Component {
        val icon = getWorldIconComponent(source.world.environment)
        val messageColor = if (isGlobal) NamedTextColor.WHITE else NamedTextColor.GRAY

        return Component.empty()
            .append(source.displayName().color(NamedTextColor.WHITE))
            .append(Component.space())
            .append(icon)
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append(message.color(messageColor))
    }

    fun getWorldIconComponent(environment: World.Environment): Component {
        return when (environment) {
            World.Environment.NORMAL -> Component.text("☀").color(NamedTextColor.YELLOW)
            World.Environment.NETHER -> Component.text("🔥").color(NamedTextColor.RED)
            World.Environment.THE_END -> Component.text("🧪").color(NamedTextColor.DARK_PURPLE)
            else -> Component.text("🌍").color(NamedTextColor.GREEN)
        }
    }
}

@Serializable
data class EmojiConfig(
    val emojis: Map<String, String> = mapOf(
        "<3" to "❤",
        ":grib:" to "礈"
    )
)