package dev.airdead.yamidacore.services.appdata

import dev.airdead.yamidacore.components.modules.PluginService
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import kotlin.reflect.KClass

class AppDataStorage(
    override val app: YamidaPlugin
) : AppData, PluginService() {

    override val bindClass: KClass<out Any>
        get() = AppData::class

    override lateinit var config: GlobalConfigData

    override suspend fun enable() {
        config = app.loadConfig("config")
    }

    override suspend fun disable() {
        app.saveConfig("config", config)
    }

    override suspend fun reload() {
        app.saveConfig("config", config)
        config = app.loadConfig("config")
    }


}