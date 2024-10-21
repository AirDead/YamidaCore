package dev.airdead.yamidacore.components.plugin

import org.bukkit.plugin.java.JavaPlugin

class Scheduler(
    val app: JavaPlugin
) {

    fun runTaskLater(delay: Long, task: () -> Unit) =
        app.server.scheduler.runTaskLater(app, task, delay)

    fun runTaskTimer(delay: Long, period: Long, task: () -> Unit) =
        app.server.scheduler.runTaskTimer(app, task, delay, period)

    fun runTask(task: () -> Unit) =
        app.server.scheduler.runTask(app, task)

    fun runAsyncTask(task: () -> Unit) =
        app.server.scheduler.runTaskAsynchronously(app, task)

    fun runAsyncTaskLater(delay: Long, task: () -> Unit) =
        app.server.scheduler.runTaskLaterAsynchronously(app, task, delay)

    fun runAsyncTaskTimer(delay: Long, period: Long, task: () -> Unit) =
        app.server.scheduler.runTaskTimerAsynchronously(app, task, delay, period)
}