package dev.airdead.yamidacore.components.modules

import dev.airdead.yamidacore.components.plugin.YamidaPlugin

interface Module {
    val moduleName: String
    val app: YamidaPlugin
    var status: ModuleState
    val toggleable: Boolean

    fun onEnable() {

    }

    fun onDisable() {

    }
}