package dev.airdead.yamidacore.components.modules

import dev.nikdekur.ndkore.service.AbstractService
import kotlin.reflect.KClass

abstract class PluginService : AbstractService(), PluginComponent {
    open val bindClass: KClass<out Any> = this::class
}