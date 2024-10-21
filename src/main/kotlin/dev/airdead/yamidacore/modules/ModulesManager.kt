package dev.airdead.yamidacore.modules

import dev.airdead.yamidacore.components.modules.Module
import dev.airdead.yamidacore.components.modules.ModuleState
import dev.airdead.yamidacore.components.modules.commands.CommandExecutorModule
import dev.airdead.yamidacore.components.modules.commands.TabExecutorModule
import dev.airdead.yamidacore.components.modules.listeners.ListenerModule
import dev.airdead.yamidacore.ext.isEnabled
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class ModulesManager(val plugin: JavaPlugin) : TabExecutor {
    val toggleableModules = mutableListOf<Module>()
    val staticModules = mutableListOf<Module>()

    fun registerModule(module: Module) {
        module.status = ModuleState.DISABLED
        if (module.toggleable) {
            toggleableModules.add(module)
        } else {
            staticModules.add(module)
        }
    }

    fun unregisterModule(module: Module) {
        if (module.toggleable && toggleableModules.remove(module)) {
            disableModule(module)
        } else {
            staticModules.remove(module)
        }
    }

    fun enableAll() {
        plugin.getCommand("modules")?.apply {
            setExecutor(this@ModulesManager)
            tabCompleter = this@ModulesManager
        }
        (toggleableModules + staticModules).forEach { enableModule(it) }
    }

    fun disableAll() {
        plugin.getCommand("modules")?.apply {
            setExecutor(null)
            tabCompleter = null
        }
        (toggleableModules + staticModules).forEach { disableModule(it) }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender is Player && args.isNotEmpty()) {
            val action = args[0]
            val moduleName = args.getOrNull(1)
            when (action) {
                "enable", "disable", "change" -> {
                    val module = moduleName?.let { findModuleByName(it, toggleableModules) } ?: return false
                    when (action) {
                        "enable" -> enableModule(module)
                        "disable" -> disableModule(module)
                        "change" -> if (module.isEnabled()) disableModule(module) else enableModule(module)
                    }
                    sender.sendMessage("§aМодуль ${module.moduleName} ${module.status.state.lowercase()}")
                    return true
                }
                "reload" -> {
                    toggleableModules.forEach {
                        disableModule(it)
                        enableModule(it)
                    }
                    sender.sendMessage("§aМодули перезагружены")
                    return true
                }
            }
        }
        return false
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
                if (args[0] in listOf("enable", "disable", "change")) {
                    toggleableModules.map { it.moduleName }.filter { it.startsWith(args[1]) }
                } else null
            }
            else -> null
        }
    }

    fun enableModule(module: Module) = updateModuleState(module, ModuleState.LOADING, ModuleState.ENABLED) {
        module.onEnable()
        registerModuleEvents(module)
    }

    fun disableModule(module: Module) = updateModuleState(module, ModuleState.UNLOADING, ModuleState.DISABLED) {
        module.onDisable()
        unregisterModuleEvents(module)
    }

    fun updateModuleState(
        module: Module,
        loadingState: ModuleState,
        finalState: ModuleState,
        action: () -> Unit
    ) {
        module.status = loadingState
        action()
        module.status = finalState
    }

    fun registerModuleEvents(module: Module) {
        when (module) {
            is ListenerModule -> plugin.server.pluginManager.registerEvents(module, plugin)
            is CommandExecutorModule -> plugin.getCommand(module.commandName)?.setExecutor(module)
            is TabExecutorModule -> plugin.getCommand(module.commandName)?.apply {
                tabCompleter = module
                setExecutor(module)
            }
        }
    }

    fun unregisterModuleEvents(module: Module) {
        when (module) {
            is ListenerModule -> HandlerList.unregisterAll(module)
            is CommandExecutorModule -> plugin.getCommand(module.commandName)?.setExecutor(null)
            is TabExecutorModule -> plugin.getCommand(module.commandName)?.apply {
                tabCompleter = null
                setExecutor(null)
            }
        }
    }

    fun findModuleByName(name: String, modules: List<Module>) = modules.find { it.moduleName == name }
}
