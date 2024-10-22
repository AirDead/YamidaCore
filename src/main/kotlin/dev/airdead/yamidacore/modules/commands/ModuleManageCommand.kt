package dev.airdead.yamidacore.modules.commands

import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.ext.isEnable
import dev.airdead.yamidacore.ext.toComponent
import dev.nikdekur.ndkore.service.inject
import kotlinx.coroutines.runBlocking
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ModuleManageCommand(
    override val app: YamidaPlugin
) : PluginCommand() {
    override val commandName = "modules"
    override val moduleName: String = "module_manager"

    private val menuCommand by inject<ModuleMenuCommand>()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender is Player && args.isNotEmpty()) {
            if (!sender.isOp) return false
            val action = args[0]
            val moduleName = args.getOrNull(1)

            runBlocking {
                when (action) {
                    "reload" -> {
                        if (moduleName == "all") {
                            app.toggleableComponents.forEach { it.reload() }
                            menuCommand.sendMenu(sender)
                            sender.sendMessage("§aВсе модули перезагружены")
                        } else {
                            handleModuleAction(sender, moduleName, "reload")
                        }
                    }
                    "enable", "disable", "change" -> {
                        handleModuleAction(sender, moduleName, action)
                    }
                }
            }
            return true
        }

        sender.sendPlayerListFooter("123".toComponent())
        return false
    }

    suspend fun handleModuleAction(sender: Player, moduleName: String?, action: String) {
        val module = app.toggleableComponents.find { it.moduleName == moduleName } ?: return
        when (action) {
            "enable" -> module.enable()
            "disable" -> module.disable()
            "change" -> if (module.isEnable()) module.disable() else module.enable()
            "reload" -> module.reload()
        }
        menuCommand.sendMenu(sender)
        sender.sendMessage("§aМодуль ${module.moduleName} ${module.state.toString().lowercase()}")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        return when (args.size) {
            1 -> listOf("enable", "disable", "change", "reload").filter { it.startsWith(args[0]) }
            2 -> {
                if (args[0] in listOf("enable", "disable", "change", "reload")) {
                    val modules = app.toggleableComponents.map { it.moduleName }.toMutableList()
                    if (args[0] == "reload") {
                        modules.add("all")
                    }
                    modules.filter { it.startsWith(args[1]) }
                } else null
            }
            else -> null
        }
    }
}