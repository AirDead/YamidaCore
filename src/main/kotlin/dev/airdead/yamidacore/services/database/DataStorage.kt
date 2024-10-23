package dev.airdead.yamidacore.services.database

import java.util.UUID

interface DataStorage {
    fun getDataBy(uuid: UUID): PlayerData?
    fun getDataBy(name: String): PlayerData?
    fun updateData(data: PlayerData): Boolean
}

data class PlayerData(
    val uuid: UUID,
    val name: String,
    val firstJoin: Long = System.currentTimeMillis(),
    val lastJoin: Long = System.currentTimeMillis(),
    val lastQuit: Long = 0,
    val playTime: Long = 0,
    val ip: String,
) {
    constructor(uuid: String, name: String, ip: String) : this(
        UUID.fromString(uuid), name, ip = ip
    )
}
