package charlezz.com.mediamuxer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import org.jetbrains.anko.gridView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionManager.getInstance().checkPermission(this,C)


        val view = verticalLayout {
            val gv = gridView()
            gv.lparams(width= matchParent , height = matchParent)
            gv.numColumns = 3
        }
        setContentView(view)


    }


}
