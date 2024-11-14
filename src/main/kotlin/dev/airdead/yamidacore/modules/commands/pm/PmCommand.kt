package dev.airdead.yamidacore.modules.commands.pm

import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

class PmCommand(override val app: YamidaPlugin) : PluginCommand() {
    override val commandName = "pm"

    override val bindClass: KClass<out Any>
        get() = this::class

    val lastMessageRecipient = mutableMapOf<String, String>()


    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.size < 2) {
            sender.sendMessage("§cИспользование: /pm <ник> <сообщение>")
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null || !target.isOnline) {
            sender.sendMessage("§cИгрок с ником ${args[0]} не найден.")
            return true
        }

        val message = args.drop(1).joinToString(" ")

        if (sender is Player) {
            target.sendMessage("§7[ЛС] ${sender.name} -> Вам: §e$message")
            sender.sendMessage("§7[ЛС] Вы -> ${target.name}: §e$message")
            lastMessageRecipient[sender.name] = target.name
        } else if (sender is ConsoleCommandSender) {
            target.sendMessage("§7[ЛС] Консоль -> Вам: §e$message")
            sender.sendMessage("§7Сообщение отправлено игроку ${target.name}.")
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], true) }
        }
        return emptyList()
    }
}
