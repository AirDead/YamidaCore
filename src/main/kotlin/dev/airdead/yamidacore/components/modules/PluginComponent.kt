package dev.airdead.yamidacore.components.modules

import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.nikdekur.ndkore.service.ServicesComponent
import dev.nikdekur.ndkore.service.manager.ServicesManager

interface PluginComponent : ServicesComponent {
    val app: YamidaPlugin

    val toggleable: Boolean
        get() = true

    override val manager: ServicesManager
        get() = app.servicesManager
}