package dev.airdead.yamidacore.ext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

fun String.toComponent() = Component.text(this)

fun Component.startsWith(prefix: String) = PlainTextComponentSerializer.plainText().serialize(this).startsWith(prefix)
fun Component.removePrefix(prefix: String) = PlainTextComponentSerializer.plainText().serialize(this).removePrefix(prefix)