package dev.airdead.yamidacore.modules.dataset

import dev.airdead.yamidacore.components.modules.PluginService
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import kotlin.reflect.KClass

class ConfigDataManager(
    override val app: YamidaPlugin
) : DataManager, PluginService() {
    override val moduleName = "data_manager"
    override val toggleable = false

    override val bindClass: KClass<out Any>
        get() = DataManager::class

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