package dev.airdead.yamidacore.ext

import dev.airdead.yamidacore.components.modules.PluginService
import dev.nikdekur.ndkore.service.Service

fun PluginService.isEnable(): Boolean = state == Service.State.Enabled
fun PluginService.isDisable(): Boolean = state == Service.State.Disabled
fun PluginService.isStarting(): Boolean = state == Service.State.Enabling
fun PluginService.isStopping(): Boolean = state == Service.State.Disabling
