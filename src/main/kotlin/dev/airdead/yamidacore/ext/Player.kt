package dev.airdead.yamidacore.ext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

enum class ButtonPosition(val isSpaced: Boolean = false) {
    BEFORE, // Перед текстом
    AFTER, // После текста
    INLINE, // Внутри строки
    ABOVE, // Над текстом
    BELOW, // Под текстом
    BEFORE_SPACED(true), // Перед текстом с пробелом
    AFTER_SPACED(true), // После текста с пробелом
    INLINE_SPACED(true), // Внутри строки с пробелом
    ABOVE_SPACED(true), // Над текстом с пробелом
    BELOW_SPACED(true) // Под текстом с пробелом
}

fun Player.sendCustomMessage(
    headerMessage: String? = null,
    buttonData: List<ButtonData>,
) {
    val miniMessage = MiniMessage.miniMessage()

    var messageComponent: Component = headerMessage?.let {
        miniMessage.deserialize(it)
    } ?: Component.empty()

    buttonData.forEach { button ->
        val messagePart = miniMessage.deserialize(button.messageText)

        val buttonComponent = miniMessage.deserialize(button.buttonText)
            .clickEvent(ClickEvent.runCommand(button.command))
            .hoverEvent(HoverEvent.showText(miniMessage.deserialize(button.hoverText)))

        val updatedComponent = when (button.position) {
            ButtonPosition.BEFORE -> buttonComponent.append(messagePart)
            ButtonPosition.AFTER -> messagePart.append(buttonComponent)
            ButtonPosition.INLINE -> messagePart.append(Component.space()).append(buttonComponent)
            ButtonPosition.ABOVE -> Component.text("\n").append(buttonComponent).append(messagePart)
            ButtonPosition.BELOW -> messagePart.append(Component.text("\n")).append(buttonComponent)
            ButtonPosition.BEFORE_SPACED -> buttonComponent.append(" ".toComponent()).append(messagePart)
            ButtonPosition.AFTER_SPACED -> messagePart.append(" ".toComponent()).append(buttonComponent)
            ButtonPosition.INLINE_SPACED -> messagePart.append(" ".toComponent()).append(buttonComponent)
            ButtonPosition.ABOVE_SPACED -> Component.text("\n").append(buttonComponent).append(messagePart)
            ButtonPosition.BELOW_SPACED -> messagePart.append(Component.text("\n")).append(buttonComponent)
        }

        messageComponent = messageComponent.append(updatedComponent).append(Component.text(button.separator))
    }

    this.sendMessage(messageComponent)
}
data class ButtonData(
    val messageText: String,
    val buttonText: String,
    val command: String,
    val hoverText: String,
    val separator: String = "",
    val position: ButtonPosition = ButtonPosition.INLINE
)
