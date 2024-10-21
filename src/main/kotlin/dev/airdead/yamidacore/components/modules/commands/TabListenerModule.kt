package dev.airdead.yamidacore.components.modules.commands

import dev.airdead.yamidacore.components.modules.Module
import org.bukkit.command.TabExecutor
import org.bukkit.event.Listener

interface TabListenerModule : Module, Listener, TabExecutor