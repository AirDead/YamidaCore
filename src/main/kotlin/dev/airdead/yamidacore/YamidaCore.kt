package dev.airdead.yamidacore

import dev.airdead.yamidacore.components.modules.PluginService
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import dev.airdead.yamidacore.modules.commands.moderation.SpectateCommand
import dev.airdead.yamidacore.modules.commands.pm.PmCommand
import dev.airdead.yamidacore.modules.commands.pm.ReplyCommand
import dev.airdead.yamidacore.modules.commands.rp.TryCommand
import dev.airdead.yamidacore.modules.listeners.AlcoholListener
import dev.airdead.yamidacore.modules.listeners.ChatListener
import dev.airdead.yamidacore.modules.listeners.MotdService
import dev.airdead.yamidacore.modules.listeners.TabService
import xyz.xenondevs.invui.InvUI

class YamidaCore : YamidaPlugin() {
    override val components: Collection<PluginService> by lazy {
        listOf(
            ChatListener(this),
            TryCommand(this),
            PmCommand(this),
            ReplyCommand(this),
            TabService(this),
            MotdService(this),
            SpectateCommand(this),
            AlcoholListener(this)

        )
    }

    override fun whenEnable() {
        InvUI.getInstance().setPlugin(this)
    }

    override fun whenDisable() {
    }

}