package dev.airdead.yamidacore.modules.commands

import dev.airdead.yamidacore.components.modules.ModuleState
import dev.airdead.yamidacore.components.modules.commands.CommandExecutorModule
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.components.plugin.loadConfig
import dev.airdead.yamidacore.ext.ButtonData
import dev.airdead.yamidacore.ext.ButtonPosition
import dev.airdead.yamidacore.ext.sendCustomMessage
import kotlinx.serialization.Serializable
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Serializable
data class ModuleMenuConfig(
    val title: String = "<color:#ffcffd><b>▎</b> </color><color:#ffcffd>Параметры разработчика</color>:\n",
    val pattern: String = "<color:#ffcffd>▎</color><color:#ffebff>Модуль {module}</color>",
    val enable: String = "<color:#0dc70a>[Включить]</color>",
    val disable: String = "<color:#ff0000>[Отключить]</color>",
    val reload: String = "<color:#ff0000>[Перезагрузить]</color>"
)

class ModulesCommands(
    override val app: YamidaPlugin
) : CommandExecutorModule {
    override val moduleName = "module_menu"
    override val commandName = "menu"
    override var status = ModuleState.DISABLED
    override val toggleable = false

    lateinit var config: ModuleMenuConfig

    override fun onEnable() {
        config = app.loadConfig("modules")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false

        val itemButtons = mutableListOf<ButtonData>()

        app.modulesManager.toggleableModules.forEach { module ->
            val actionText = if (module.status == ModuleState.ENABLED) config.disable else config.enable

            itemButtons.add(ButtonData(
                messageText = config.pattern.replace("{module}", module.moduleName),
                buttonText = actionText,
                command = "/modules change ${module.moduleName}",
                hoverText = "Нажмите чтобы ${if (module.status == ModuleState.ENABLED) "отключить" else "включить"} модуль ${module.moduleName}",
                position = ButtonPosition.AFTER,
                separator = "\n"

            ))
        }

        itemButtons.add(
            ButtonData(
                messageText = "<color:#ffcffd><b>▎</b> </color>Перезагрузить все модули",
                buttonText = config.reload,
                command = "/modules reload",
                hoverText = "Нажмите чтобы перезагрузить все модули"
            )
        )

        sender.sendCustomMessage(
            headerMessage = config.title,
            buttonData = itemButtons,
        )
        return true
    }

}