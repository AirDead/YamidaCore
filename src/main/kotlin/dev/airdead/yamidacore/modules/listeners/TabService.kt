package dev.airdead.yamidacore.modules.listeners

import dev.airdead.yamidacore.components.modules.PluginListener
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TabService(override val app: YamidaPlugin) : PluginListener() {

    override suspend fun onEnable() {
        Bukkit.getOnlinePlayers().forEach {
            updateTabList(it)
            updatePlayerPing(it)
        }
    }


    fun updatePlayerPing(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline) cancel()
                updateTabList(player)
            }
        }.runTaskTimer(app, 0, 6000)
    }

    fun updateTabList(player: Player) {
        val tps = Bukkit.getTPS()[0]
        val ping = player.ping

        val header = Component.text("Server Info").color(NamedTextColor.GRAY)
            .append(Component.newline())
            .append(Component.newline())

        val footer = Component.newline()
            .append(Component.text("TPS: "))
            .append(getTpsComponent(tps))
            .append(Component.text(" | Ping: $ping ms"))
            .color(NamedTextColor.GRAY)

        player.sendPlayerListHeaderAndFooter(header, footer)

        val worldIcon = getWorldIcon(player.world.environment)
        val playerName = Component.text(player.name).color(NamedTextColor.WHITE)
            .append(Component.space())
            .append(worldIcon)

        player.playerListName(playerName)
    }

    fun getWorldIcon(environment: World.Environment): Component {
        return when (environment) {
            World.Environment.NORMAL -> Component.text("☀").color(NamedTextColor.YELLOW)
            World.Environment.NETHER -> Component.text("🔥").color(NamedTextColor.RED)
            World.Environment.THE_END -> Component.text("🧪").color(NamedTextColor.DARK_PURPLE)
            else -> Component.text("🌍").color(NamedTextColor.GREEN)
        }
    }

    fun getTpsComponent(tps: Double): Component {
        return when {
            tps >= 18.9 -> Component.text("%.1f".format(tps)).color(NamedTextColor.GREEN)
            tps >= 15.0 -> Component.text("%.1f".format(tps)).color(NamedTextColor.YELLOW)
            else -> Component.text("%.1f".format(tps)).color(NamedTextColor.RED)
        }
    }
}