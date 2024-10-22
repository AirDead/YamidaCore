package dev.airdead.yamidacore.modules.global

import dev.airdead.yamidacore.components.modules.PluginListener
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.server.ServerListPingEvent
import org.bukkit.scheduler.BukkitTask

@Serializable
data class MotdConfig(
    val name: String = "motd",
    val motd: List<String> = listOf("Добро пожаловать на сервер!")
)

class MOTDModule(override val app: YamidaPlugin) : PluginListener() {
    override val moduleName = "MOTD"
    override val toggleable = true

    lateinit var motdTimer: BukkitTask
    var serverName = ""
    var currentMOTD = ""

    override suspend fun onEnable() {
        loadConfigAndStartTimer()
    }

    override suspend fun onDisable() {
        motdTimer.cancel()
        serverName = ""
        currentMOTD = ""
    }

    override suspend fun onReload() {
        motdTimer.cancel()
        loadConfigAndStartTimer()
    }

    fun loadConfigAndStartTimer() {
        val config = app.loadConfig<MotdConfig>("motd")
        currentMOTD = config.motd.random()
        serverName = config.name

        motdTimer = app.scheduler.runAsyncTaskTimer(0, 20 * 60) {
            val motd = config.motd.random()
            if (currentMOTD != motd) {
                currentMOTD = motd
                serverName = config.name
            }
        }
    }

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent) {
        val miniMessage = MiniMessage.miniMessage()
        event.motd(miniMessage.deserialize("$serverName\n$currentMOTD"))
    }
}