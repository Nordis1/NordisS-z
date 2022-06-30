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
import androidx.appcompat.app.AppCompatActivity
import nordis.nordisquiz.databinding.ActivityStartQuizBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


private const val TAG = "StartQuiz"
private val executorStartQuiz = Executors.newCachedThreadPool();
val playerMap = HashMap<String, ImageView>()
val botAIList = ArrayList<BotAI>()

@SuppressLint("StaticFieldLeak")
private lateinit var bindingStartQuiz: ActivityStartQuizBinding

private var handler: Handler? = null
private var questNumber: Int = -1
private var rightPlace: Int = -1
private var timeCounter: Int = 20
private var botAIEasy = 70
private var botAINormal = 80
private var botAIHard = 90
private var roundTime = 20
private var countResponse = 0

class StartQuiz : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingStartQuiz = ActivityStartQuizBinding.inflate(layoutInflater)
        setContentView(bindingStartQuiz.root)
        bindingStartQuiz.btnResponse1.setOnClickListener(this)
        bindingStartQuiz.btnResponse2.setOnClickListener(this)
        bindingStartQuiz.btnResponse3.setOnClickListener(this)
        bindingStartQuiz.btnResponse4.setOnClickListener(this)
        handlerCreating()
        gameHasStart() // Начинаем игру запуская игроков и объявление вопроса
        questionsPrepare() //тут задаёться вопрос и загружаються варианты ответов
        playerCreating() // тут создаються боты и сам пользователь



    }

    fun gameHasStart(){
        executorStartQuiz.execute(Runnable {
            TimeUnit.MILLISECONDS.sleep(1600)
            handler?.sendEmptyMessage(0)
            TimeUnit.MILLISECONDS.sleep(1400)
            handler?.sendEmptyMessage(1)

        })
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View?) {

        when(v){
            bindingStartQuiz.btnResponse1->{checkCountResponse()
            userGaveResponse(bindingStartQuiz.btnResponse1)}
            bindingStartQuiz.btnResponse2->{checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse2)}
            bindingStartQuiz.btnResponse3->{checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse3)}
            bindingStartQuiz.btnResponse4->{checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse4)}
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
    fun userGaveResponse(button: Button){
        setToYellowColor(button)
        bindingStartQuiz.btnResponse1.isEnabled  = false
        bindingStartQuiz.btnResponse2.isEnabled  = false
        bindingStartQuiz.btnResponse3.isEnabled  = false
        bindingStartQuiz.btnResponse4.isEnabled  = false
    }


    fun questionsPrepare() {
        if (questResponseList.size != 0) {
            if (questResponseList.isEmpty()){
                this.onBackPressed()
            }
            if (questNumber == -1){
                rightPlace = (1..4).random()
                questNumber = (0 until questResponseList.size).random()
            }else{
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
                                    if (!numbersOfWomenList.contains(it) && !innerArrayImages.contains(it.toString())) {
                                        innerArrayImages.add(it.toString())
                                        Log.d(TAG, "playerCreating: Создали Мужскую иконку $it"
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
                                if (numbersOfWomenList.contains(it) && !innerArrayImages.contains(it.toString()) ) {
                                    innerArrayImages.add(it.toString())
                                    Log.d(TAG, "playerCreating: Создали Женскую иконку $it"
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
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
                    botAIList.add(BotAI(innerArrayNames[i],bindingStartQuiz.playerIcon2,
                        botAIEasy, rightPlace))
                }
            }
        }


    }
    fun responseResult(){

    }

    fun checkCountResponse(){
        countResponse++
        if(countResponse == 4){
            responseResult()
        }
    }

    private fun handlerCreating() {
        handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val botname = msg.data.get("botName")
                playerMap[botname].let { it?.setBackgroundResource(R.drawable.shadow) }
                msg.data.clear()
                when (msg.what) {
                    0 ->{
                        bindingStartQuiz.quest.visibility = View.VISIBLE}
                    1 -> {buttonsVisibility(true)
                    questionsVisibility(true)
                        startTimer()}
                    2 -> bindingStartQuiz.timer.setText(timeCounter.toString())

                    3-> {checkCountResponse()}
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

fun buttonsVisibility(boolean: Boolean){
    if (boolean) {
        bindingStartQuiz.btnResponse1.visibility = View.VISIBLE
        bindingStartQuiz.btnResponse2.visibility = View.VISIBLE
        bindingStartQuiz.btnResponse3.visibility = View.VISIBLE
        bindingStartQuiz.btnResponse4.visibility = View.VISIBLE
    }else{
        bindingStartQuiz.btnResponse1.visibility = View.INVISIBLE
        bindingStartQuiz.btnResponse2.visibility = View.INVISIBLE
        bindingStartQuiz.btnResponse3.visibility = View.INVISIBLE
        bindingStartQuiz.btnResponse4.visibility = View.INVISIBLE
    }
}

    fun questionsVisibility(boolean: Boolean){
        if (boolean){
            bindingStartQuiz.questionPlace.visibility = View.VISIBLE
            bindingStartQuiz.timer.visibility = View.VISIBLE
            bindingStartQuiz.quest.visibility = View.INVISIBLE
        }else{
            bindingStartQuiz.questionPlace.visibility = View.INVISIBLE
            bindingStartQuiz.timer.visibility = View.INVISIBLE
            bindingStartQuiz.quest.visibility = View.VISIBLE

        }
    }


    fun startTimer() {
        executorStartQuiz.execute {
            while (timeCounter != 0 || countResponse == 4) {
                TimeUnit.SECONDS.sleep(1)
                timeCounter--
                //Посылаем уменьшение секунды на экран
                handler?.sendEmptyMessage(2)
            }
        }
        botThinkThreadStart()// Боты начинают думать когда запускаеться таймер
    }

    fun botThinkThreadStart(){
        for (bot in botAIList){
            executorStartQuiz.execute{
                TimeUnit.SECONDS.sleep((0..roundTime).random().toLong())
                bot.botQuestResponse()
                handler?.sendEmptyMessage(3)

                val message = handler?.obtainMessage()
                val bundle = Bundle()
                bundle.putString("botName",bot.name)
                message?.data = bundle
                handler?.sendMessage(message!!)
                }
            }
        }
    }

    fun setToGreenColor(v: View?) {
        if (v != null) {
            v.setBackgroundResource(R.drawable.btn_custom_green)
        }
    }

    fun setToYellowColor(v: View?) {
        if (v != null) {
            v.setBackgroundResource(R.drawable.btn_yellow_custom)
        }
    }

    fun setToRedColor(v: View?) {
        if (v != null) {
            v.setBackgroundResource(R.drawable.btn_red_custom)
        }
    }
