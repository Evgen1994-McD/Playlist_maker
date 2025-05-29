package com.example.playlistmaker.ui.player.viewModel

sealed class MediaPlayerCommand {
    object Play : MediaPlayerCommand()  // попробовал sealed класс - в дальнейшем комманды можно будет расширить
    object Pause: MediaPlayerCommand()  // Когда доавится лайк или добавление в коллекцию


}