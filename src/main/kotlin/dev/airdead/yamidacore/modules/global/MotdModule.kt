package dev.airdead.yamidacore.modules.global

import dev.airdead.yamidacore.components.modules.ModuleState
import dev.airdead.yamidacore.components.modules.listeners.ListenerModule
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.components.plugin.loadConfig
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

class MOTDModule(override val app: YamidaPlugin) : ListenerModule {
    override val moduleName = "MOTD"
    override var status = ModuleState.DISABLED
    override val toggleable = true

    lateinit var motdTimer: BukkitTask
    var serverName = ""
    var currentMOTD = ""


    override fun onEnable() {
        val config = app.loadConfig<MotdConfig>("motd")

        currentMOTD = config.motd.random()

        motdTimer = app.scheduler.runAsyncTaskTimer(0, 20 * 60) {
            val motd = config.motd.random()
            if (currentMOTD != motd) {
                currentMOTD = motd
                serverName = config.name
            }
        }
    }


    override fun onDisable() {
        motdTimer.cancel()
    }

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent) {
        val miniMessage = MiniMessage.miniMessage()

        event.motd(miniMessage.deserialize("""
            $serverName
            $currentMOTD
        """.trimIndent()))
    }
}