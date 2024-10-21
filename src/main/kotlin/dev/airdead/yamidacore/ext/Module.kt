package dev.airdead.yamidacore.ext

import dev.airdead.yamidacore.components.modules.Module
import dev.airdead.yamidacore.components.modules.ModuleState

fun Module.isEnabled() = this.status == ModuleState.ENABLED
fun Module.isDisabled() = this.status == ModuleState.DISABLED