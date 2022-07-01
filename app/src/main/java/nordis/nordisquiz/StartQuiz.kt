package nordis.nordisquiz


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nordis.nordisquiz.databinding.ActivityStartQuizBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


private const val TAG = "StartQuiz"
var executorStartQuiz: ExecutorService? = null
val playerMap = HashMap<String, ImageView>()
val botAIList = ArrayList<BotAI>()

@SuppressLint("StaticFieldLeak")
private lateinit var bindingStartQuiz: ActivityStartQuizBinding

private var handlerStartQuiz: Handler? = null
private var questNumber: Int = -1
private var rightPlace: Int = -1
private var timeCounter = 20
private var botAIEasy = 70
private var botAINormal = 80
private var botAIHard = 90
private var countResponse = 0

class StartQuiz : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingStartQuiz = ActivityStartQuizBinding.inflate(layoutInflater)
        setContentView(bindingStartQuiz.root)
        Log.d(TAG, "onCreate: ")
        /** На важные глобальные переменные ставим дефолтные значения
         * так как с прошлого потока могут оставаться старые значения */
        countResponse = 0
        timeCounter = 20
        botAIList.clear()
        executorStartQuiz = Executors.newCachedThreadPool();

        bindingStartQuiz.btnResponse1.setOnClickListener(this)
        bindingStartQuiz.btnResponse2.setOnClickListener(this)
        bindingStartQuiz.btnResponse3.setOnClickListener(this)
        bindingStartQuiz.btnResponse4.setOnClickListener(this)

        handlerCreating()
        gameHasStart() // Начинаем игру запуская игроков и объявление вопроса
        questionsPrepare() //тут задаёться вопрос и загружаються варианты ответов
        playerCreating() // тут создаються боты и сам пользователь


    }

    fun gameHasStart() {
        executorStartQuiz?.execute(Runnable {
            TimeUnit.MILLISECONDS.sleep(1600)
            handlerStartQuiz?.sendEmptyMessage(0)
            TimeUnit.MILLISECONDS.sleep(1400)
            handlerStartQuiz?.sendEmptyMessage(1)

        })
    }

    fun startQuestionShow(boolean: Boolean) {
        if (boolean) {
            questionsAndButtonsVisibility(true)

        } else {
            questionsAndButtonsVisibility(false)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View?) {
        /** Ответил Пользователь:
         * Красим кнопку
         * Выключаем другие кнопки*/
        when (v) {
            bindingStartQuiz.btnResponse1 -> {
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse1)
            }
            bindingStartQuiz.btnResponse2 -> {
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse2)
            }
            bindingStartQuiz.btnResponse3 -> {
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse3)
            }
            bindingStartQuiz.btnResponse4 -> {
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse4)
            }
        }

/*        when (rightPlace) {
            1 -> if (v == bindingStartQuiz.btnResponse1) {
                setToGreenColor(bindingStartQuiz.btnResponse1)
                playerMap["user"]?.setBackgroundResource(R.drawable.shadow)
                Log.d(TAG, "onClick: Variant 1 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 1: Ответ не правильный")
            }
            2 -> if (v == bindingStartQuiz.btnResponse2) {
                setToGreenColor(bindingStartQuiz.btnResponse2)
                playerMap["user"]?.setBackgroundResource(R.drawable.shadow)
                Log.d(TAG, "onClick: Variant 2 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 2: Ответ не правильный")
            }
            3 -> if (v == bindingStartQuiz.btnResponse3) {
                setToGreenColor(bindingStartQuiz.btnResponse3)
                playerMap["user"]?.setBackgroundResource(R.drawable.shadow)
                Log.d(TAG, "onClick: Variant 3 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 3: Ответ не правильный")
            }
            4 -> if (v == bindingStartQuiz.btnResponse4) {
                setToGreenColor(bindingStartQuiz.btnResponse4)
                playerMap["user"]?.setBackgroundResource(R.drawable.shadow)
                Log.d(TAG, "onClick: Variant 4 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 4: Ответ не правильный")
            }
        }*/

    }

    fun userGaveResponse(button: Button) {
        playerMap["user"]?.setBackgroundResource(R.drawable.shadow)
        setToUserGaveChooseColor(button)
        bindingStartQuiz.btnResponse1.isEnabled = false
        bindingStartQuiz.btnResponse2.isEnabled = false
        bindingStartQuiz.btnResponse3.isEnabled = false
        bindingStartQuiz.btnResponse4.isEnabled = false
    }


    fun responseResult() {
        Toast.makeText(baseContext, "All done!", Toast.LENGTH_LONG).show()
    }


    fun checkCountResponse() {
        countResponse++
        Log.d(TAG, "checkCountResponse: $countResponse")
        if (countResponse == 4) {
            responseResult()
        }

    }


    private fun handlerCreating() {
        handlerStartQuiz = object : Handler(Looper.getMainLooper()!!) {
            @Synchronized
            override fun handleMessage(msg: Message) {
                if (!msg.data.isEmpty) {
                    playerMap[msg.data.get("botName")].let {
                        it?.setBackgroundResource(R.drawable.shadow)
                        msg.data.clear()
                        Log.d(TAG, "handleMessage: Зашли боту дать фон рамки")
                    }
                } else {
                    when (msg.what) {
                        0 -> {
                            Log.d(
                                TAG,
                                "handleMessage: зашли что бы сделать объявление вопроса видимым"
                            )
                            bindingStartQuiz.quest.visibility = View.VISIBLE
                        }
                        1 -> {
                            Log.d(
                                TAG,
                                "handleMessage: Защшли что бы сделать сам вопрос видимым и запустить таймер"
                            )
                            startQuestionShow(true)
                            startTimer()
                        }
                        2 -> bindingStartQuiz.timer.setText(timeCounter.toString())

                        3 -> {
                            checkCountResponse()
                        }
                        4 -> responseResult()
                        20 -> {
                            val dialog = DialogClass(
                                bindingStartQuiz.root.context,
                                "Время вышло",
                                "Вы не ответили на вопрос во время!",
                                null,
                                null
                            )
                            dialog.standartDialogMessage()

                        }
                    }
                }
            }
        }
    }

    fun startTimer() {
        Log.d(TAG, "startTimer: $timeCounter")
        Log.d(TAG, "startTimer: $countResponse")
        executorStartQuiz?.execute(Runnable {

            run timerWhile@{
                while (timeCounter != 0) {
                    if (timeCounter <= 0 || countResponse == 4) {
                        return@Runnable
                    }
                    //Посылаем уменьшение секунды на экран
                    handlerStartQuiz?.sendEmptyMessage(2)
                    timeCounter--
                    TimeUnit.SECONDS.sleep(1)
                }
            }

        })
        botThinkThreadStart()// Боты начинают думать когда запускаеться таймер
    }


    fun questionsAndButtonsVisibility(boolean: Boolean) {
        if (boolean) {
            bindingStartQuiz.btnResponse1.visibility = View.VISIBLE
            bindingStartQuiz.btnResponse2.visibility = View.VISIBLE
            bindingStartQuiz.btnResponse3.visibility = View.VISIBLE
            bindingStartQuiz.btnResponse4.visibility = View.VISIBLE
            bindingStartQuiz.quest.visibility = View.INVISIBLE
            bindingStartQuiz.questionPlace.visibility = View.VISIBLE
            bindingStartQuiz.timer.visibility = View.VISIBLE
        } else {
            bindingStartQuiz.btnResponse1.visibility = View.GONE
            bindingStartQuiz.btnResponse2.visibility = View.GONE
            bindingStartQuiz.btnResponse3.visibility = View.GONE
            bindingStartQuiz.btnResponse4.visibility = View.GONE
            bindingStartQuiz.questionPlace.visibility = View.INVISIBLE
            bindingStartQuiz.timer.visibility = View.INVISIBLE
            bindingStartQuiz.quest.visibility = View.VISIBLE

        }
    }


    fun botThinkThreadStart() {
        for (bot in botAIList) {
            executorStartQuiz?.execute(Runnable {
                try {
                    TimeUnit.SECONDS.sleep((4 until 19).random().toLong())
                    bot.botQuestResponse()
                    handlerStartQuiz?.sendEmptyMessage(3)

                    val message = handlerStartQuiz?.obtainMessage()
                    val bundle = Bundle()
                    bundle.putString("botName", bot.name)
                    message?.data = bundle
                    handlerStartQuiz?.sendMessage(message!!)
                } catch (e: Exception) {

                }
            })
        }
    }

    fun questionsPrepare() {
        if (questResponseList.size != 0) {
            if (questResponseList.isEmpty()) {
                this.onBackPressed()
            }
            if (questNumber == -1) {
                rightPlace = (1..4).random()
                questNumber = (0 until questResponseList.size).random()
            } else {
                questResponseList.removeAt(questNumber)
                rightPlace = (1..4).random()
                questNumber = (0 until questResponseList.size).random()
            }

            bindingStartQuiz.questionPlace.text = questResponseList[questNumber].question
            if (rightPlace == 1) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].rightResponse)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].response4)
            } else if (rightPlace == 2) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].rightResponse)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].response4)
            } else if (rightPlace == 3) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].rightResponse)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].response4)
            } else if (rightPlace == 4) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].response4)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].rightResponse)
            }
        }
    }

    fun playerCreating() {

        /** Подготовка к распределению! */
        val innerArrayNames = ArrayList<String>()
        val innerArrayImages = ArrayList<String>()

        //Получаем рандомный лист с именами 3 штуки  для подгрузки побов
        while (innerArrayNames.size != 3) {
            /** Создаём бабу или мужика(Чётные мужики, нечётные бабы)*/
            run playerCreator@{
                (0..10).random().let {
                    if (it % 2 == 0) {
                        /** Создаём имя мужику*/
                        nameMenList[(0 until nameMenList.size).random()].let {
                            if (!innerArrayNames.contains(it)) {
                                innerArrayNames.add(it)
                                Log.d(TAG, "playerCreating: Создали имя мужчины $it")
                            } else return@playerCreator
                        }
                        /**Теперь создаём картинку мужику*/

                        val count1 = innerArrayImages.size
                        while (count1 == innerArrayImages.size) {
                            (1..50).random().let {
                                /** Если женский список не содержит этого номера то пихаем*/
                                if (!numbersOfWomenList.contains(it) && !innerArrayImages.contains(
                                        it.toString()
                                    )
                                ) {
                                    innerArrayImages.add(it.toString())
                                    Log.d(
                                        TAG, "playerCreating: Создали Мужскую иконку $it"
                                    )
                                }
                            }
                        }

                    } else {
                        /** Создаём женщину */
                        nameWomenList[(0 until nameWomenList.size).random()].let {
                            if (!innerArrayNames.contains(it)) {
                                innerArrayNames.add(it)
                                Log.d(TAG, "playerCreating: Создали имя женщины $it")
                            } else return@playerCreator
                        }

                        /** Теперь создаём картинку женщине */
                        val count2 = innerArrayImages.size
                        while (count2 == innerArrayImages.size) {
                            (1..50).random().let {
                                /** Если женский список содержит этого номера то пихаем*/
                                if (numbersOfWomenList.contains(it) && !innerArrayImages.contains(it.toString())) {
                                    innerArrayImages.add(it.toString())
                                    Log.d(
                                        TAG, "playerCreating: Создали Женскую иконку $it"
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }

        //Этот рандом создаётся для для позиции Пользователя
        /** Старт главного распределения */
        val random = (1..4).random()
        if (random == 1) {
            /** Сначала размещаем пользователя */
            map[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon1.setImageResource(it) }
            bindingStartQuiz.playerName1.text = userNameIs
            playerMap["user"] = bindingStartQuiz.playerIcon1

            /** После основного пользователя размещаем остальных ботов */
            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                }
            }
        } else if (random == 2) {
            map[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon2.setImageResource(it) }
            playerMap["user"] = bindingStartQuiz.playerIcon2
            bindingStartQuiz.playerName2.text = userNameIs

            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName1.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                }
            }
        } else if (random == 3) {
            map[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon3.setImageResource(it) }
            playerMap["user"] = bindingStartQuiz.playerIcon3
            bindingStartQuiz.playerName3.text = userNameIs

            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName1.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                }
            }

        } else if (random == 4) {
            map[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon4.setImageResource(it) }
            playerMap["user"] = bindingStartQuiz.playerIcon4
            bindingStartQuiz.playerName4.text = userNameIs

            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName1.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i], bindingStartQuiz.playerIcon2,
                            botAIEasy, rightPlace
                        )
                    )
                }
            }
        }


    }

    override fun onDestroy() {
        handlerStartQuiz?.sendEmptyMessage(10)
        super.onDestroy()
        Log.d(TAG, "onDestroy:")
        //executorStartQuiz?.shutdownNow()
        /*    countResponse = 0
            timeCounter = 20*/

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onBackPressed() {
        executorStartQuiz?.shutdown()
        super.onBackPressed()
        Log.d(TAG, "onBackPressed: ")
    }


    fun setToUserGaveChooseColor(v: View?) {
        v?.setBackgroundResource(R.drawable.btn_yellow_tume_custom)
    }

    fun setToGreenColor(v: View?) {
        v?.setBackgroundResource(R.drawable.btn_custom_green)
    }

    fun setToYellowColor(v: View?) {
        v?.setBackgroundResource(R.drawable.btn_yellow_custom)
    }

    fun setToRedColor(v: View?) {
        v?.setBackgroundResource(R.drawable.btn_red_custom)

    }
}




