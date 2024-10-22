package dev.airdead.yamidacore.modules.dataset

import dev.airdead.yamidacore.components.modules.PluginComponent
import kotlinx.serialization.Serializable

interface DataManager : PluginComponent {
    var config: GlobalConfigData
}

@Serializable
data class GlobalConfigData(
    val modules: ModulesConfig
)

@Serializable
data class ModulesConfig(
    val title: String = "<color:#ffcffd><b>▎</b> </color><color:#ffcffd>Параметры разработчика</color>:\n",
    val pattern: String = "<color:#ffcffd><b>▎</b> </color><color:#ffebff>Модуль {module}</color> ",
    val enabled: String = "<color:#0dc70a>[Включить]</color>",
    val disabled: String = "<color:#ff0000>[Отключить]</color>",
    val reloadTitle: String = "<color:#ffcffd><b>▎</b> </color><color:#ffcffd>Перезагрузка всех модулей</color>: ",
    val reload: String = "<color:#fbff1c>Перезагрузить</color>"
)