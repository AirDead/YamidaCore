package dev.airdead.yamidacore.components.modules.commands

import dev.airdead.yamidacore.components.modules.Module
import org.bukkit.command.CommandExecutor

interface CommandExecutorModule : Module, CommandExecutor {
    val commandName: String
}