package dev.airdead.yamidacore.components.modules

enum class ModuleState(val state: String) {
    ENABLED("Включен"),
    LOADING("Загрузка"),
    UNLOADING("Выгрузка"),
    DISABLED("Выключен")
}