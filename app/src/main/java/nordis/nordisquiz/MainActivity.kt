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
val avatarIconMap = HashMap<String,Int>()
private val executor = Executors.newCachedThreadPool();
private const val TAG = "MainActivity"
var handlerMain: Handler? = null

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
                avatarIconMap[userAvatarNameIs]
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
                if(executorStartQuiz == null || executorStartQuiz?.isTerminated == true ) {
                    binding.button.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                    if (CheckInternetClass().checkInternetAvailable(applicationContext)) {
                        executor.execute {
                            TimeUnit.MILLISECONDS.sleep(650)
                            handlerMain?.sendEmptyMessage(1)//Показываем подключение к серверу
                            Log.d(TAG, "onClick: Launch connecting to Server")
                            TimeUnit.MILLISECONDS.sleep(2000)//2 сек подключаемся
                            for (i in 2..5) {
                                if (CheckInternetClass().checkInternetAvailable(applicationContext)) {
                                    Log.d(TAG, "onClick: Incrementing Players $i")
                                    handlerMain?.sendEmptyMessage(i)//Сбор игроков
                                    TimeUnit.MILLISECONDS.sleep((500..1500).random().toLong())
                                } else {
                                    Log.d(TAG, "onClick: No Internet to Incrementing")
                                    handlerMain?.sendEmptyMessage(8)
                                    TimeUnit.MILLISECONDS.sleep(700)
                                    handlerMain?.sendEmptyMessage(9)
                                    return@execute
                                }
                            }
                            Log.d(TAG, "onClick: Start game text launch")
                            handlerMain?.sendEmptyMessage(6)
                            TimeUnit.MILLISECONDS.sleep(700)
                            handlerMain?.sendEmptyMessage(7)
                        }
                    } else {
                        Log.d(TAG, "onClick: If No Connecting")
                        executor.execute {
                            TimeUnit.MILLISECONDS.sleep(650)
                            handlerMain?.sendEmptyMessage(1)
                            TimeUnit.MILLISECONDS.sleep(2000)
                            handlerMain?.sendEmptyMessage(8)
                            TimeUnit.MILLISECONDS.sleep(700)
                            handlerMain?.sendEmptyMessage(9)
                        }

                    }
                }else{
                    Toast.makeText(applicationContext,getString(R.string.connecting_state_take_a_brake),Toast.LENGTH_LONG).show()
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
        handlerMain = object : Handler(Looper.myLooper()!!) {
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
                            val intent = Intent(applicationContext, StartQuiz()::class.java)
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
                    10->{
                        //вызов идёт со StartQuizActivity когда backPressed
                        executor.execute(Runnable {
                            while (executorStartQuiz?.isTerminated != true){
                                TimeUnit.SECONDS.sleep(1)
                                Log.d(TAG, "handleMessage: executorStartQuiz is not terminated!")}
                            Log.d(TAG, "handleMessage: executer is terminated!")

                        }) }
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
        avatarIconMap["st"] = R.mipmap.user_profile2
        avatarIconMap["1"] = R.mipmap.ico1
        avatarIconMap["2"] = R.mipmap.ico2
        avatarIconMap["3"] = R.mipmap.ico3
        avatarIconMap["4"] = R.mipmap.ico4
        avatarIconMap["5"] = R.mipmap.ico5
        avatarIconMap["6"] = R.mipmap.ico6
        avatarIconMap["7"] = R.mipmap.ico7
        avatarIconMap["8"] = R.mipmap.ico8
        avatarIconMap["9"] = R.mipmap.ico9
        avatarIconMap["10"] = R.mipmap.ico10
        avatarIconMap["11"] = R.mipmap.ico11
        avatarIconMap["12"] = R.mipmap.ico12
        avatarIconMap["13"] = R.mipmap.ico13
        avatarIconMap["14"] = R.mipmap.ico14
        avatarIconMap["15"] = R.mipmap.ico15
        avatarIconMap["16"] = R.mipmap.ico16
        avatarIconMap["17"] = R.mipmap.ico17
        avatarIconMap["18"] = R.mipmap.ico18
        avatarIconMap["19"] = R.mipmap.ico19
        avatarIconMap["20"] = R.mipmap.ico20
        avatarIconMap["21"] = R.mipmap.ico21
        avatarIconMap["22"] = R.mipmap.ico22
        avatarIconMap["23"] = R.mipmap.ico23
        avatarIconMap["24"] = R.mipmap.ico24
        avatarIconMap["25"] = R.mipmap.ico25
        avatarIconMap["26"] = R.mipmap.ico26
        avatarIconMap["27"] = R.mipmap.ico27
        avatarIconMap["28"] = R.mipmap.ico28
        avatarIconMap["29"] = R.mipmap.ico29
        avatarIconMap["30"] = R.mipmap.ico30
        avatarIconMap["31"] = R.mipmap.ico31
        avatarIconMap["32"] = R.mipmap.ico32
        avatarIconMap["33"] = R.mipmap.ico33
        avatarIconMap["34"] = R.mipmap.ico34
        avatarIconMap["35"] = R.mipmap.ico35
        avatarIconMap["36"] = R.mipmap.ico36
        avatarIconMap["37"] = R.mipmap.ico37
        avatarIconMap["38"] = R.mipmap.ico38
        avatarIconMap["39"] = R.mipmap.ico39
        avatarIconMap["40"] = R.mipmap.ico40
        avatarIconMap["41"] = R.mipmap.ico41
        avatarIconMap["42"] = R.mipmap.ico42
        avatarIconMap["43"] = R.mipmap.ico43
        avatarIconMap["44"] = R.mipmap.ico44
        avatarIconMap["45"] = R.mipmap.ico45
        avatarIconMap["46"] = R.mipmap.ico46
        avatarIconMap["47"] = R.mipmap.ico47
        avatarIconMap["48"] = R.mipmap.ico48
        avatarIconMap["49"] = R.mipmap.ico49
        avatarIconMap["50"] = R.mipmap.ico50
    }

    
}