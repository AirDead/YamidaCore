package dev.airdead.yamidacore

import dev.airdead.yamidacore.components.modules.Module
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.modules.commands.ModulesCommands
import dev.airdead.yamidacore.modules.global.MOTDModule

class YamidaCore : YamidaPlugin() {
    override val components: Collection<Module> by lazy {
        listOf(
            MOTDModule(this),
            ModulesCommands(this)
        )
    }

    override fun whenEnable() {
    }

    override fun whenDisable() {
    }

}