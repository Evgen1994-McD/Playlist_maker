import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R

object FragmentManager { // урок 2, создали фрагмент менеджер.
    var currentFragment: Fragment? = null

    fun setFragment(newFragment: Fragment, act: AppCompatActivity){
        val transaction = act.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)  // задаём анимацию. Для задания анимации можно использовать стандартные, например слайд слева на право
        transaction.replace(R.id.viewpager, newFragment)
        transaction.commit()
        currentFragment = newFragment
    }
}