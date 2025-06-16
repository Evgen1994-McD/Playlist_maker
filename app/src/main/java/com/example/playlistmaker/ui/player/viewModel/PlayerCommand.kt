package com.example.playlistmaker.ui.player.viewModel

sealed class PlayerCommand {
    object Play : PlayerCommand()  // попробовал sealed класс - в дальнейшем комманды можно будет расширить
    object Pause: PlayerCommand()  // Когда доавится лайк или добавление в коллекцию


}