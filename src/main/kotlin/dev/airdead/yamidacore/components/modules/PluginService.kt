package dev.airdead.yamidacore.components.modules

import dev.nikdekur.ndkore.service.AbstractService
import kotlin.reflect.KClass

abstract class PluginService : AbstractService(), PluginComponent {
    abstract val moduleName: String
    open val bindClass: KClass<out Any> = this::class
    override val toggleable: Boolean
        get() = false
}