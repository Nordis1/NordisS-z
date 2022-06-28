package nordis.nordisquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import nordis.nordisquiz.databinding.ActivityFragmentTransactionBinding
import nordis.nordisquiz.fragments.UserProfile

@SuppressLint("StaticFieldLeak")
lateinit var bindingActivityTransa: ActivityFragmentTransactionBinding
private var resultLauncherTransa: ActivityResultLauncher<Intent>? = null
private const val TAG = "FragmentTransactionActi"

class FragmentTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityTransa = ActivityFragmentTransactionBinding.inflate(layoutInflater)
        setContentView(bindingActivityTransa.root)
        val stringIntent = intent.extras?.get("transaction").toString()
        Log.d(TAG, "onCreate: we got transaction name: $stringIntent")
        replaceFragmentMaster(stringIntent)
    }

    fun replaceFragmentMaster(nameIntent: String) {
        if (nameIntent.equals("UserProfile")) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, UserProfile())
                .setReorderingAllowed(true)
            transaction.commit()
        }
    }
}