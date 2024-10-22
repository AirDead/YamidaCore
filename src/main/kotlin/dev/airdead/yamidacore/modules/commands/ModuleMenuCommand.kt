package dev.airdead.yamidacore.modules.commands

import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.ext.ButtonData
import dev.airdead.yamidacore.ext.ButtonPosition
import dev.airdead.yamidacore.ext.isEnable
import dev.airdead.yamidacore.ext.sendCustomMessage
import dev.airdead.yamidacore.modules.dataset.DataManager
import dev.airdead.yamidacore.modules.dataset.ModulesConfig
import dev.nikdekur.ndkore.service.inject
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

class ModuleMenuCommand(
    override val app: YamidaPlugin
) : PluginCommand() {
    override val moduleName = "module_menu"
    override val commandName = "menu"
    override val toggleable = false
    override val bindClass: KClass<out Any>
        get() = this::class

    lateinit var config: ModulesConfig

    override suspend fun enable() {
        val dataManager by inject<DataManager>()
        config = dataManager.config.modules
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player || !sender.isOp) return false

        sendMenu(sender)
        return true
    }

    fun sendMenu(sender: Player) {
        repeat(100) { sender.sendMessage("") }

        val itemButtons = mutableListOf<ButtonData>()

        app.toggleableComponents.forEach { module ->
            val actionText = if (module.isEnable()) config.disabled else config.enabled

            itemButtons.add(
                ButtonData(
                    messageText = config.pattern.replace("{module}", module.moduleName),
                    buttonText = actionText,
                    command = "/modules change ${module.moduleName}",
                    hoverText = "Нажмите чтобы ${if (module.isEnable()) "отключить" else "включить"} модуль ${module.moduleName}",
                    position = ButtonPosition.AFTER,
                    separator = " "
                )
            )
            itemButtons.add(
                ButtonData(
                    messageText = "",
                    buttonText = config.reload,
                    command = "/modules reload ${module.moduleName}",
                    hoverText = "Нажмите чтобы перезагрузить модуль ${module.moduleName}",
                    position = ButtonPosition.AFTER,
                    separator = "\n"
                )
            )
        }
        itemButtons.add(
            ButtonData(
                messageText = config.reloadTitle,
                buttonText = config.reload,
                command = "/modules reload all",
                hoverText = "Нажмите чтобы перезагрузить все модули",
                position = ButtonPosition.AFTER,
                separator = "\n"
            )
        )

        sender.sendCustomMessage(
            headerMessage = config.title,
            buttonData = itemButtons
        )
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): List<String?>? {
        return emptyList()
    }
}