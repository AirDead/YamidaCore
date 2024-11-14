package dev.airdead.yamidacore.modules.commands.moderation

import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.ext.online
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class SpectateCommand(override val app: YamidaPlugin) : PluginCommand() {
    override val commandName = "spectate"
    val spectating = mutableMapOf<UUID, Location>()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = sender as? Player ?: run {
            sender.sendMessage("§cКоманда доступна только игрокам.")
            return true
        }

        if (args.isEmpty()) {
            spectating.remove(player.uniqueId)?.let {
                player.teleport(it)
                player.sendMessage("§aВы больше не находитесь в режиме наблюдения.")
            }
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null || !target.isOnline) {
            player.sendMessage("§cИгрок с ником ${args[0]} не найден.")
            return true
        }

        spectating[player.uniqueId] = player.location
        player.gameMode = GameMode.SPECTATOR
        player.teleport(target)
        player.spectatorTarget = target
        player.sendMessage("§aВы находитесь в режиме наблюдения за игроком ${target.name}.")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        return online()
    }
}