package dev.airdead.yamidacore.components.plugin

import com.charleskorn.kaml.Yaml
import dev.airdead.yamidacore.components.modules.Module
import dev.airdead.yamidacore.modules.ModulesManager
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class YamidaPlugin : JavaPlugin() {
    abstract val components: Collection<Module>
    val modulesManager = ModulesManager(this)
    val scheduler = Scheduler(this)

    override fun onEnable() {
        whenEnable()
        components.forEach {
            modulesManager.registerModule(it)
        }
        modulesManager.enableAll()
    }

    override fun onDisable() {
        whenDisable()
        components.forEach {
            it.onDisable()
        }
        modulesManager.disableAll()
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

}

inline fun <reified T : Any> YamidaPlugin.loadConfig(
    configName: String,
    requireFilled: Boolean = false,
    folder: File? = null
): T = loadConfig(configName, T::class.java, requireFilled, folder)