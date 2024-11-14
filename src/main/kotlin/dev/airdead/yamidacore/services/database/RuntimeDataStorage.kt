package dev.airdead.yamidacore.services.database

import dev.airdead.yamidacore.components.modules.PluginService
import dev.airdead.yamidacore.components.plugin.YamidaPlugin
import java.util.UUID

class RuntimeDataStorage(
    override val app: YamidaPlugin
) : DataStorage, PluginService() {

    val playerData = mutableListOf<PlayerData>()

    override fun getDataBy(uuid: UUID): PlayerData? {
        return playerData.find { it.uuid == uuid }
    }

    override fun getDataBy(name: String): PlayerData? {
        return playerData.find { it.name == name }
    }

    override fun updateData(data: PlayerData): Boolean {
        val index = playerData.indexOfFirst { it.uuid == data.uuid }
        if (index == -1) {
            playerData.add(data)
        } else {
            playerData[index] = data
        }
        return true
    }
}
