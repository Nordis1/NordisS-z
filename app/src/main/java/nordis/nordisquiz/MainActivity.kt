package nordis.nordisquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import nordis.nordisquiz.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding
val questResponseList = ArrayList<QuestionClass>()
val nanesList = ArrayList<String>()
val map = HashMap<String,Int>()
private val executor = Executors.newCachedThreadPool();
private const val TAG = "MainActivity"
private var handler: Handler? = null

var userNameIs: String? = null
var userAvatarNameIs: String? = null
var resultLauncher: ActivityResultLauncher<Intent>? = null

open class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")
        binding.button.setOnClickListener(this)
        binding.profilImage.setOnClickListener(this)
        imageMapCreating()//нужен тут, так как подгрузка картинок в методе onResume(), привязываеться к переменной map

        handlerCreating()
        QuestCreator().createQuestions()// создаём базу вопросов
        PlayerCreator().playerCreator()// Создаём базу игроков

    }

    fun profileInit(){

        //Name init
            getSharedPreferences("USERNAME", Context.MODE_PRIVATE)?.getString("userName","")?.let {
                if (it.isEmpty()){
                    userNameIs = getString(R.string.UserStandardName)
                    binding.profileNameMain.text = userNameIs
                }else{
                    binding.profileNameMain.text = it

                }
            }

        //Icon init
            userAvatarNameIs = getSharedPreferences("AVATAR", Context.MODE_PRIVATE)
                ?.getString("avatarName", "")
            if (userAvatarNameIs == null || userAvatarNameIs.equals("") ) {
                //binding.profilImage.setBackgroundResource(R.mipmap.user_profile2)
                binding.profilImage.setBackgroundResource(R.mipmap.user_profile2)
            }else{
                map[userAvatarNameIs]
                    ?.let { binding.profilImage.setBackgroundResource(it) }
            }

    }

    fun activityRequestResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    if (data != null) {
                        if (data.hasExtra("Profil")) {


                        }
                    }
                }
            }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            binding.button.id -> {
                binding.button.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                if (CheckInternetClass().checkInternetAvailable(applicationContext)) {
                    executor.execute {
                        TimeUnit.MILLISECONDS.sleep(650)
                        handler?.sendEmptyMessage(1)//Показываем подключение к серверу
                        Log.d(TAG, "onClick: Launch connecting to Server")
                        TimeUnit.MILLISECONDS.sleep(2000)//2 сек подключаемся
                        for (i in 2..5) {
                            if (CheckInternetClass().checkInternetAvailable(applicationContext)) {
                                Log.d(TAG, "onClick: Incrementing Players $i")
                                handler?.sendEmptyMessage(i)//Сбор игроков
                                TimeUnit.SECONDS.sleep((1..4).random().toLong())
                            } else {
                                Log.d(TAG, "onClick: No Internet to Incrementing")
                                handler?.sendEmptyMessage(8)
                                TimeUnit.MILLISECONDS.sleep(700)
                                handler?.sendEmptyMessage(9)
                                return@execute
                            }
                        }
                        Log.d(TAG, "onClick: Start game text launch")
                        handler?.sendEmptyMessage(6)
                        TimeUnit.MILLISECONDS.sleep(700)
                        handler?.sendEmptyMessage(7)
                    }
                } else {
                    Log.d(TAG, "onClick: If No Connecting")
                    executor.execute {
                        TimeUnit.MILLISECONDS.sleep(650)
                        handler?.sendEmptyMessage(1)
                        TimeUnit.MILLISECONDS.sleep(2000)
                        handler?.sendEmptyMessage(8)
                        TimeUnit.MILLISECONDS.sleep(700)
                        handler?.sendEmptyMessage(9)
                    }

                }
            }
            binding.profilImage.id->{
                val intent = Intent(applicationContext, FragmentTransactionActivity::class.java)
                intent.putExtra("transaction","UserProfile")
                startActivity(intent)
                //resultLauncher?.launch(intent)
            }

        }
    }

    private fun handlerCreating() {
        handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> binding.startConnectingTV.visibility = View.VISIBLE
                    2 -> binding.startConnectingTV.text =
                        getString(R.string.connecting_state_2)
                    3 -> binding.startConnectingTV.text =
                        getString(R.string.connecting_state_2_1)
                    4 -> binding.startConnectingTV.text =
                        getString(R.string.connecting_state_2_2)
                    5 -> binding.startConnectingTV.text =
                        getString(R.string.connecting_state_2_3)
                    6 -> binding.startConnectingTV.text =
                        getString(R.string.connecting_state_3)
                    7 -> {
                        if (CheckInternetClass().checkInternetAvailable(applicationContext)) {
                            Log.d(TAG, "handleMessage: start launch the new Activity ")
                            val intent = Intent(applicationContext, StartQuiz::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.connecting_state_2_4),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.startConnectingTV.visibility = View.GONE
                        binding.startConnectingTV.setText(R.string.connectingToServer)
                        binding.button.isEnabled = true
                    }
                    8 -> binding.startConnectingTV.text =
                        getString(R.string.connecting_state_2_4)
                    9 -> {
                        binding.progressBar.visibility = View.GONE
                        binding.startConnectingTV.visibility = View.GONE
                        binding.startConnectingTV.setText(R.string.connectingToServer)
                        binding.button.isEnabled = true
                    }
                }
            }
        }
    }


    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        profileInit()
        super.onResume()
    }
    override fun onPause() {
        Log.d(TAG, "onPause: ")
        super.onPause()

    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        super.onStop()
    }
    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }
    fun imageMapCreating(){
        map["st"] = R.mipmap.user_profile2
        map["m1"] = R.mipmap.m1
        map["m2"] = R.mipmap.m2
        map["m3"] = R.mipmap.m3
        map["m4"] = R.mipmap.m4
        map["m5"] = R.mipmap.m5
        map["m6"] = R.mipmap.m6
        map["w1"] = R.mipmap.w1
        map["w2"] = R.mipmap.w2
        map["w3"] = R.mipmap.w3
        map["w4"] = R.mipmap.w4
        map["w5"] = R.mipmap.w5
        map["w6"] = R.mipmap.w6
    }

    
}