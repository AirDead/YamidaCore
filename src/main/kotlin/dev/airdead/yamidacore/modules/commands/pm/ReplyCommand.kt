package dev.airdead.yamidacore.modules.commands.pm

import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.nikdekur.ndkore.service.inject
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReplyCommand(override val app: YamidaPlugin) : PluginCommand() {
    override val commandName = "reply"

    val pmCommand by inject<PmCommand>()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§cИспользование: /reply <сообщение>")
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§cЭта команда доступна только для игроков.")
            return true
        }

        val lastRecipientName = pmCommand.lastMessageRecipient[sender.name]
        if (lastRecipientName == null) {
            sender.sendMessage("§cВы еще не отправляли личных сообщений.")
            return true
        }

        val target = Bukkit.getPlayer(lastRecipientName)
        if (target == null || !target.isOnline) {
            sender.sendMessage("§cИгрок с ником $lastRecipientName не найден.")
            return true
        }

        val message = args.joinToString(" ")

        target.sendMessage("§7[ЛС] ${sender.name} -> Вам: §e$message")
        sender.sendMessage("§7[ЛС] Вы -> ${target.name}: §e$message")

        pmCommand.lastMessageRecipient[sender.name] = target.name

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        return emptyList()
    }
}