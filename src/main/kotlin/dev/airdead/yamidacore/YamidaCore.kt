package dev.airdead.yamidacore

import dev.airdead.yamidacore.components.modules.PluginService
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.listeners.ChatListener
import dev.airdead.yamidacore.services.appdata.AppDataStorage
import xyz.xenondevs.invui.InvUI

class YamidaCore : YamidaPlugin() {
    override val components: Collection<PluginService> by lazy {
        listOf(
            AppDataStorage(this),
            ChatListener(this)

        )
    }

    override fun whenEnable() {
        InvUI.getInstance().setPlugin(this)
    }

    override fun whenDisable() {
    }

}