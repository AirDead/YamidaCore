package dev.airdead.yamidacore.components.modules.commands

import dev.airdead.yamidacore.components.modules.Module
import org.bukkit.command.TabExecutor

interface TabExecutorModule : Module, TabExecutor {
    val commandName: String
}