package nordis.nordisquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import nordis.nordisquiz.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding
val questResponseList = ArrayList<QuestionClass>()
val nanesList = ArrayList<String>()
private val executor = Executors.newCachedThreadPool();
private const val TAG = "MainActivity"
private var handler: Handler? = null
var name: String? = null

open class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.button.setOnClickListener(this)

        handlerCreating()
        creatingQuestions()
        creatingPlayerNames()
        Log.d(TAG, "onCreate: ")

    }

    fun creatingQuestions() {
        questResponseList.add(
            QuestionClass(
                "С какой из этих стран Чехия не граничит?",
                "Венгрия", "Германия", "Австрия", "Польша"
            )
        )
        questResponseList.add(
            QuestionClass(
                "Кто считается основоположником кубизма?",
                "П. Пикассо", "В. Кандинский", "Ф. Леже", "К. Малевич"
            )
        )
        questResponseList.add(
            QuestionClass(
                "Кто из этих знаменитых людей не является тезкой остальных?",
                "Лужков", "Боярский", "Лермонтов", "Горбачев"
            )
        )

        questResponseList.add(
            QuestionClass(
                "Какая березка стояла во поле?",
                "Кудрявая", "Засохшая", "Зеленая", "Высокая"
            )
        )

        questResponseList.add(
            QuestionClass(
                "Какая из этих кислот является витамином?",
                "Никотиновая", "Янтарная", "Яблочная", "Молочная"
            )
        )

        questResponseList.add(
            QuestionClass(
                "Территория какой из этих стран - наибольшая?",
                "Япония", "Финляндия", "Италия", "Германия"
            )
        )

        questResponseList.add(
            QuestionClass(
                "Какой из этих городов - родина Казановы?",
                "Венеция", "Флоренция", "Милан", "Неаполь"
            )
        )

        questResponseList.add(
            QuestionClass(
                "Чье произведение легло в основу оперы Дж. Верди 'Травиата'?",
                "А.Дюма-сына", "Г. Флобера", "О. Бальзака", "В. Гюго"
            )
        )

        questResponseList.add(
            QuestionClass(
                "Кто является чемпионом гонок 'Формулы-1' 1998-99 г.г.?",
                "Хаккинен", "Кулхард", "Барикелло", "М. Шумахер"
            )
        )

    }

    fun creatingPlayerNames() {
        nanesList.add("Александр")
        nanesList.add("Алексей")
        nanesList.add("Андрей")
        nanesList.add("Антон")
        nanesList.add("Арсений")
        nanesList.add("Артем")
        nanesList.add("Василий")
        nanesList.add("Василий")
        nanesList.add("Виктор")
        nanesList.add("Виталий")
        nanesList.add("Владимир")
        nanesList.add("Владислав")
        nanesList.add("Вячеслав")
        nanesList.add("Георгий")
        nanesList.add("Глеб")
        nanesList.add("Давид")
        nanesList.add("Даниил")
        nanesList.add("Денис")
        nanesList.add("Дмитрий")
        nanesList.add("Евгений")
        nanesList.add("Егор")
        nanesList.add("Иван")
        nanesList.add("Игорь")
        nanesList.add("Илья")
        nanesList.add("Кирилл")
        nanesList.add("Константин")
        nanesList.add("Лев")
        nanesList.add("Максим")
        nanesList.add("Марк")
        nanesList.add("Матвей")
        nanesList.add("Михаил")
        nanesList.add("Никита")
        nanesList.add("Николай")
        nanesList.add("Олег")
        nanesList.add("Павел")
        nanesList.add("Роман")
        nanesList.add("Руслан")
        nanesList.add("Сергей")
        nanesList.add("Станислав")
        nanesList.add("Степан")
        nanesList.add("Тимофей")
        nanesList.add("Тимур")
        nanesList.add("Федор")
        nanesList.add("Юрий")
        nanesList.add("Ярослав")
        nanesList.add("Александра")
        nanesList.add("Алена")
        nanesList.add("Алина")
        nanesList.add("Алиса")
        nanesList.add("Анастасия")
        nanesList.add("Ангелина")
        nanesList.add("Анна")
        nanesList.add("Арина")
        nanesList.add("Валерия")
        nanesList.add("Варвара")
        nanesList.add("Василиса")
        nanesList.add("Вера")
        nanesList.add("Вероника")
        nanesList.add("Виктория")
        nanesList.add("Дарья")
        nanesList.add("Диана")
        nanesList.add("Ева")
        nanesList.add("Евгения")
        nanesList.add("Екатерина")
        nanesList.add("Елена")
        nanesList.add("Елизавета")
        nanesList.add("Злата")
        nanesList.add("Ирина")
        nanesList.add("Карина")
        nanesList.add("Кира")
        nanesList.add("Кристина")
        nanesList.add("Ксения")
        nanesList.add("Лилия")
        nanesList.add("Любовь")
        nanesList.add("Людмила")
        nanesList.add("Маргарита")
        nanesList.add("Марина")
        nanesList.add("Мария")
        nanesList.add("Милана")
        nanesList.add("Надежда")
        nanesList.add("Наталья")
        nanesList.add("Ольга")
        nanesList.add("Полина")
        nanesList.add("Светлана")
        nanesList.add("София")
        nanesList.add("Таисия")
        nanesList.add("Ульяна")
        nanesList.add("Юлия")
        nanesList.add("Яна")
        nanesList.shuffle()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
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
}