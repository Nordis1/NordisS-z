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
val nameMenList = ArrayList<String>()
val nameWomenList = ArrayList<String>()
val numbersOfWomenList = ArrayList<Int>()
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
        PlayerNameCreator().menCreator()// Создаём базу игроков
        PlayerNameCreator().womenCreator()// Создаём базу игроков
        PlayerNameCreator().womenNumeresCreator()// Создаём базу игроков

    }

    fun profileInit(){

        //Name init
            getSharedPreferences("USERNAME", Context.MODE_PRIVATE)?.getString("userName","")?.let {
                if (it.isEmpty()){
                    userNameIs = getString(R.string.UserStandardName)
                }else{
                    userNameIs = it

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
                                TimeUnit.MILLISECONDS.sleep((500..1500).random().toLong())
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
        map["1"] = R.mipmap.ico1
        map["2"] = R.mipmap.ico2
        map["3"] = R.mipmap.ico3
        map["4"] = R.mipmap.ico4
        map["5"] = R.mipmap.ico5
        map["6"] = R.mipmap.ico6
        map["7"] = R.mipmap.ico7
        map["8"] = R.mipmap.ico8
        map["9"] = R.mipmap.ico9
        map["10"] = R.mipmap.ico10
        map["11"] = R.mipmap.ico11
        map["12"] = R.mipmap.ico12
        map["13"] = R.mipmap.ico13
        map["14"] = R.mipmap.ico14
        map["15"] = R.mipmap.ico15
        map["16"] = R.mipmap.ico16
        map["17"] = R.mipmap.ico17
        map["18"] = R.mipmap.ico18
        map["19"] = R.mipmap.ico19
        map["20"] = R.mipmap.ico20
        map["21"] = R.mipmap.ico21
        map["22"] = R.mipmap.ico22
        map["23"] = R.mipmap.ico23
        map["24"] = R.mipmap.ico24
        map["25"] = R.mipmap.ico25
        map["26"] = R.mipmap.ico26
        map["27"] = R.mipmap.ico27
        map["28"] = R.mipmap.ico28
        map["29"] = R.mipmap.ico29
        map["30"] = R.mipmap.ico30
        map["31"] = R.mipmap.ico31
        map["32"] = R.mipmap.ico32
        map["33"] = R.mipmap.ico33
        map["34"] = R.mipmap.ico34
        map["35"] = R.mipmap.ico35
        map["36"] = R.mipmap.ico36
        map["37"] = R.mipmap.ico37
        map["38"] = R.mipmap.ico38
        map["39"] = R.mipmap.ico39
        map["40"] = R.mipmap.ico40
        map["41"] = R.mipmap.ico41
        map["42"] = R.mipmap.ico42
        map["43"] = R.mipmap.ico43
        map["44"] = R.mipmap.ico44
        map["45"] = R.mipmap.ico45
        map["46"] = R.mipmap.ico46
        map["47"] = R.mipmap.ico47
        map["48"] = R.mipmap.ico48
        map["49"] = R.mipmap.ico49
        map["50"] = R.mipmap.ico50
    }

    
}