package dev.airdead.yamidacore.components.plugin

import com.charleskorn.kaml.Yaml
import dev.airdead.yamidacore.components.modules.PluginCommand
import dev.airdead.yamidacore.components.modules.PluginListener
import dev.airdead.yamidacore.components.modules.PluginService
import dev.nikdekur.ndkore.koin.SimpleKoinContext
import dev.nikdekur.ndkore.service.manager.KoinServicesManager
import dev.nikdekur.ndkore.service.manager.ServicesManager
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.koin.environmentProperties
import java.io.File

abstract class YamidaPlugin : JavaPlugin() {
    abstract val components: Collection<PluginService>
    lateinit var servicesManager: ServicesManager
    val scheduler = Scheduler(this)


    override fun onEnable() {
        whenEnable()
        koinContext.startKoin {
            environmentProperties()
        }
        servicesManager = KoinServicesManager(koinContext)
        runBlocking {
            components.forEach { component ->
                servicesManager.registerService(component, component.bindClass)

                when (component) {
                    is PluginCommand -> {
                        getCommand(component.commandName)?.apply {
                            setExecutor(component)
                            tabCompleter = component
                        }
                    }
                    is PluginListener -> {
                        server.pluginManager.registerEvents(component, this@YamidaPlugin)
                    }
                }
            }
            servicesManager.enable()
        }
    }

    override fun onDisable() {
        whenDisable()

        components.filterIsInstance<PluginCommand>().forEach { command ->
            getCommand(command.commandName)?.apply {
                setExecutor(null)
                tabCompleter = null
            }
        }

        components.filterIsInstance<PluginListener>().forEach { listener ->
            HandlerList.unregisterAll(listener)
        }

        runBlocking {
            servicesManager.disable()
        }
    }

    abstract fun whenEnable()
    abstract fun whenDisable()

    @Suppress("NO_REFLECTION_IN_CLASS_PATH")
    @OptIn(InternalSerializationApi::class)
    fun <T : Any> loadConfig(
        configName: String,
        clazz: Class<T>,
        requireFilled: Boolean = false,
        folder: File? = null
    ): T {
        val configFileName = if (!configName.endsWith(".yml")) "$configName.yml" else configName
        val file = loadFile(configFileName, folder)

        if (file.length() == 0L) {
            check(!requireFilled) { "Config file `$configFileName` is empty but should be filled." }

            val config = clazz.kotlin.objectInstance ?: runCatching {
                clazz.getDeclaredConstructor().newInstance()
            }.getOrElse {
                throw IllegalArgumentException(
                    "Failed to create a default instance of ${clazz.simpleName}. Ensure the class has a no-args constructor.", it
                )
            }

            saveConfig(configFileName, config, folder)
            return config
        }

        val serializer = clazz.kotlin.serializer()
        return Yaml.default.decodeFromString(serializer, file.readText())
    }

    @OptIn(InternalSerializationApi::class)
    fun saveConfig(configName: String, config: Any, folder: File? = null) {
        val configFileName = if (!configName.endsWith(".yml")) "$configName.yml" else configName
        val file = loadFile(configFileName, folder)

        val serializer = config::class.serializer() as KSerializer<Any>
        val yamlContent = Yaml.default.encodeToString(serializer, config)
        file.writeText(yamlContent)
    }

    fun loadFile(fileName: String, folder: File? = null): File {
        val directory = folder ?: dataFolder
        directory.mkdirs()
        return File(directory, fileName).apply {
            if (!exists()) createNewFile()
        }
    }

    inline fun <reified T : Any> loadConfig(
        configName: String,
        requireFilled: Boolean = false,
        folder: File? = null
    ): T = loadConfig(configName, T::class.java, requireFilled, folder)

    companion object {
        val koinContext = SimpleKoinContext()
    }
}
