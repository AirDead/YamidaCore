package dev.airdead.yamidacore.modules.commands.rp

import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class TryCommand(
    override val app: YamidaPlugin
) : PluginCommand() {
    override val commandName = "try"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if (sender is Player) {
            val player = sender

            if (args.isNullOrEmpty()) {
                player.sendMessage("§cИспользование: /try <действие>")
                return true
            }

            val action = args.joinToString(" ")

            val success = Random.nextBoolean()

            val playersInRadius = player.location.world?.getNearbyPlayers(player.location, 40.0)


            val resultMessage = if (success) {
                "${player.name} §7$action §f[§aУспешно§f]"
            } else {
                "${player.name} §7$action §f[§cПровал§f]"
            }

            playersInRadius?.forEach { it.sendMessage(resultMessage) }

            return true
        }

        sender.sendMessage("§cЭту команду может использовать только игрок.")
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): List<String?> {
        return listOf(
            "дать пощечину",
        )
    }
}
