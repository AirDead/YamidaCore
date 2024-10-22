package dev.airdead.yamidacore.components.modules

import org.bukkit.command.TabExecutor

abstract class PluginCommand : PluginService(), TabExecutor {
    abstract val commandName: String
}