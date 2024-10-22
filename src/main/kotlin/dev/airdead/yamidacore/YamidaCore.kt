package dev.airdead.yamidacore

import dev.airdead.yamidacore.components.modules.PluginService
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.modules.commands.ModuleManageCommand
import dev.airdead.yamidacore.modules.commands.ModuleMenuCommand
import dev.airdead.yamidacore.modules.dataset.ConfigDataManager
import dev.airdead.yamidacore.modules.global.MOTDModule

class YamidaCore : YamidaPlugin() {
    override val components: Collection<PluginService> by lazy {
        listOf(
            // Global modules
            MOTDModule(this),
            ConfigDataManager(this),

            // Module commands
            ModuleManageCommand(this),
            ModuleMenuCommand(this)
        )
    }

    override fun whenEnable() {
    }

    override fun whenDisable() {
    }

}