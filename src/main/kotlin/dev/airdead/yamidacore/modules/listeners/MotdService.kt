package dev.airdead.yamidacore.modules.listeners

import dev.airdead.yamidacore.components.modules.PluginListener
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.ext.toComponent
import kotlinx.serialization.Serializable
import org.bukkit.event.EventHandler
import org.bukkit.event.server.ServerListPingEvent

class MotdService(override val app: YamidaPlugin) : PluginListener() {

    lateinit var config: MotdConfig

    override suspend fun onEnable() {
        config = app.loadConfig<MotdConfig>("motd")
    }

    override suspend fun onReload() {
        config = app.loadConfig<MotdConfig>("motd")
    }


    @EventHandler
    fun onMotd(event: ServerListPingEvent) {
        event.motd(("""
            ${config.name}
            ${config.motd}
        """.trimIndent()).toComponent())

    }
}

@Serializable
data class MotdConfig(
    val name: String,
    val motd: String
)