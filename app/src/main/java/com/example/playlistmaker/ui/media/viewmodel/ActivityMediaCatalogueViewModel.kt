
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class ActivityMediaCatalogueViewModel(private val switchThemeUseCase: SwitchThemeUseCase): ViewModel() {

    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }



}