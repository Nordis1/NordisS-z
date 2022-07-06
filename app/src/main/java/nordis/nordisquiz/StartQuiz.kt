package nordis.nordisquiz


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nordis.nordisquiz.databinding.ActivityStartQuizBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


private const val TAG = "StartQuiz"
var executorStartQuiz: ExecutorService? = null
val playerReadyIconMap = HashMap<String, ImageView>()
val botAIList = ArrayList<BotAI>()

@SuppressLint("StaticFieldLeak")
private lateinit var bindingStartQuiz: ActivityStartQuizBinding


private var handlerStartQuiz: Handler? = null
private var questNumber: Int = -1
private var rightPlace: Int = -1
private var timeCounter = 20
private var countResponse = 0
private var mainUserResponse: Int = 0
private var mainUserWinCounter: Int = 0
private var mainUserBoolAnswer: Boolean = false

@SuppressLint("StaticFieldLeak")
private var mainUserWindow: TextView? = null


private val setHandler_QwestCountNotification_0 = 0
private val setHandler_showQwest_andLuanch_Timer_1 = 1
private val setHandler_timerDownGrade_2 = 2
private val setHandler_checkCountRespose_3 = 3
private val setHandler_responseResult_4 = 4
private val setHandler_showCountion_of_votes_5 = 5
private val setHandler_showPlayersCountWindows_6 = 6
private val setHandler_chargeCounts_7 = 7
private val setHandler_returnTextSizeBack_8 = 8
private val setHandler_removePlayerIfLose_9 = 9
private val setHandler_removeQwest_and_Timer_10 = 10


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

    private fun gameHasStart() {
        executorStartQuiz?.execute(Runnable {
            TimeUnit.MILLISECONDS.sleep(1600)
            handlerStartQuiz?.sendEmptyMessage(setHandler_QwestCountNotification_0)
            TimeUnit.MILLISECONDS.sleep(1500)
            handlerStartQuiz?.sendEmptyMessage(setHandler_showQwest_andLuanch_Timer_1)
        })
    }

    private fun startQuestionShow(boolean: Boolean) {
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
                mainUserResponse = 1
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse1)
            }
            bindingStartQuiz.btnResponse2 -> {
                mainUserResponse = 2
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse2)
            }
            bindingStartQuiz.btnResponse3 -> {
                mainUserResponse = 3
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse3)
            }
            bindingStartQuiz.btnResponse4 -> {
                mainUserResponse = 4
                checkCountResponse()
                userGaveResponse(bindingStartQuiz.btnResponse4)
            }
        }
    }

    private fun userGaveResponse(button: Button) {
        playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow)
        setToUserGaveChooseColor(button)
        bindingStartQuiz.btnResponse1.isEnabled = false
        bindingStartQuiz.btnResponse2.isEnabled = false
        bindingStartQuiz.btnResponse3.isEnabled = false
        bindingStartQuiz.btnResponse4.isEnabled = false
    }


    private fun responseResult() {
        /** Когда все игроки дали свой ответ тогда заходим сюда
         * и начинаем окрашивать иконки в правильный и неправильные цвета
         * заодно и саму кнопку правильного ответа.*/
        var v: View
        for (item in botAIList) {
            Log.d(TAG, "responseResult: botNameis: ${item.name}")
            Log.d(TAG, "responseResult: botIconis: ${item.icon.toString()}")
            if (item.botAnswer == item.rightPlace) {
                //playerReadyIconMap[item.name]?.setBackgroundResource(R.drawable.shadow_green)
                item.icon?.setBackgroundResource(R.drawable.shadow_green)
            } else {
                //playerReadyIconMap[item.name]?.setBackgroundResource(R.drawable.shadow_red)
                item.icon?.setBackgroundResource(R.drawable.shadow_red)
            }
        }
        when (rightPlace) {
            1 -> {
                setToGreenColor(bindingStartQuiz.btnResponse1)
                if (mainUserResponse == 1) {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_green)
                    mainUserBoolAnswer = true
                } else {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_red)
                    mainUserBoolAnswer = false

                }
            }
            2 -> {
                setToGreenColor(bindingStartQuiz.btnResponse2)
                if (mainUserResponse == 2) {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_green)
                    mainUserBoolAnswer = true

                } else {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_red)
                    mainUserBoolAnswer = false

                }
            }
            3 -> {
                setToGreenColor(bindingStartQuiz.btnResponse3)
                if (mainUserResponse == 3) {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_green)
                    mainUserBoolAnswer = true

                } else {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_red)
                    mainUserBoolAnswer = false

                }
            }
            4 -> {
                setToGreenColor(bindingStartQuiz.btnResponse4)
                if (mainUserResponse == 4) {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_green)
                    mainUserBoolAnswer = true

                } else {
                    playerReadyIconMap["user"]?.setBackgroundResource(R.drawable.shadow_red)
                    mainUserBoolAnswer = false

                }
            }
        }
        if (!isDestroyed) {
            executorStartQuiz?.execute {
                TimeUnit.MILLISECONDS.sleep(800)

                /** Вызываем подсчёт голосов*/
                handlerStartQuiz?.sendEmptyMessage(setHandler_showCountion_of_votes_5)
                TimeUnit.MILLISECONDS.sleep(1400)

                /** Показываем ячейки с правильными ответами те что рядом с иконками игроков */
                handlerStartQuiz?.sendEmptyMessage(setHandler_showPlayersCountWindows_6)
                TimeUnit.MILLISECONDS.sleep(500)

                /** Начисляем данные ячеек согласно правильным ответам  и немного увеличиваем их */
                handlerStartQuiz?.sendEmptyMessage(setHandler_chargeCounts_7)
                TimeUnit.MILLISECONDS.sleep(250)

                /** Возвращаем в исходное положение  размер шрифта */
                handlerStartQuiz?.sendEmptyMessage(setHandler_returnTextSizeBack_8)
                TimeUnit.MILLISECONDS.sleep(500)

                /** Убираем проигравших игроков если таковые имеються */
                handlerStartQuiz?.sendEmptyMessage(setHandler_removePlayerIfLose_9)
                TimeUnit.MILLISECONDS.sleep(1700)

                //handlerStartQuiz?.sendEmptyMessage(setHandler_removeQwest_and_Timer_10)
            }
        }
    }

    private fun counterVotes() {
        /** Создаём переменные */
        var answer1 = 0
        var answer2 = 0
        var answer3 = 0
        var answer4 = 0
        var noAnswer = 0

        /** Подсчитываем их*/
        mainUserResponse.let {
            Log.d(TAG, "counterVotes: пользователь ответил $it")
            when (it) {
                1 -> answer1++
                2 -> answer2++
                3 -> answer3++
                4 -> answer4++
                else -> {
                    Log.d(TAG, "counterVotes: hee")
                    noAnswer++
                }
            }
        }
        for (item in botAIList) {
            Log.d(TAG, "counterVotes: ${item.name}")
            Log.d(TAG, "counterVotes: ${item.botAnswer}")
            item.botAnswer.let {
                when (it) {
                    1 -> answer1++
                    2 -> answer2++
                    3 -> answer3++
                    4 -> answer4++
                    else -> {
                        noAnswer++
                    }
                }
            }
        }

        Log.d(TAG, "counterVotes: answer1: $answer1")
        Log.d(TAG, "counterVotes: answer2: $answer2")
        Log.d(TAG, "counterVotes: answer3: $answer3")
        Log.d(TAG, "counterVotes: answer4: $answer4")
        Log.d(TAG, "counterVotes: noAnswer: $noAnswer")


        /** Делаем тексты видимыми*/
        showCounterView(true)

        /** Вносим данные переменных в тексты*/

        bindingStartQuiz.textViewCounter1.text = answer1.toString()
        bindingStartQuiz.textViewCounter2.text = answer2.toString()
        bindingStartQuiz.textViewCounter3.text = answer3.toString()
        bindingStartQuiz.textViewCounter4.text = answer4.toString()


    }

    private fun removePlayerIfLose() {
        Log.d(TAG, "removePlayerIfLose: зашли в метод кого убрать из за ошибки")
        val mapWhoDidMistake = HashMap<String, Int>()
        val mapWhoDidNotMistake = HashMap<String, Int>()
        val allPlayersCount = botAIList.count()
        allPlayersCount.plus(1)


        /** Считываем кто дал неправильный ответ и его заработанные очки пихаем в мапу*/
        for (item in botAIList) {
            if (!item.botBoolAnswer) {
                /** Добавляем всех с неправильными ответами в мапу*/
                mapWhoDidMistake[item.name.toString()] = item.botWinCounter
            } else {
                mapWhoDidNotMistake[item.name.toString()] = item.botWinCounter
            }
        }
        if (!mainUserBoolAnswer) {
            mapWhoDidMistake["user"] = mainUserWinCounter
        } else {
            mapWhoDidNotMistake["user"] = mainUserWinCounter
        }

        /** Отсортировываем нашу мапу по нарастанию */
        val whoDidMistakesSortedMap = mapWhoDidMistake.toList()
            .sortedBy { (key, value) -> value }
            .toMap()



        if (mapWhoDidMistake.isEmpty()) {
            gameContinue("ошибок никто не сделал, продолжаем игру!")
        } else if (mapWhoDidMistake.size == allPlayersCount) {
            /** тогда повезло все сделали ошибку и никто не вылетает*/
        } else {
            when (mapWhoDidMistake.size) {
                1 -> {
                    Log.d(TAG, "removePlayerIfLose: была совершена одна ошибка")
                    if (allPlayersCount == 2) {
                        Log.d(TAG, "removePlayerIfLose: всего осталось в игре 2 игрока")
                        if (whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first].toString() == "user") {
                            mainUserGameOver()
                        } else {
                            Log.d(
                                TAG,
                                "removePlayerIfLose: ошибка была бота, Пользователь одержал победу!"
                            )
                            botGameOver(whoDidMistakesSortedMap, 0)
                            /** ВАЖНО! Тогда после того как убрали последнего бота игрок должен победить */
                        }
                    } else {
                        Log.d(TAG, "removePlayerIfLose: в игре ещё много игроков")
                        /** Если игроков больше чем двое и ошибку сделал только один игрок тогда -> */
                        if (isMistakeCritical(
                                mapWhoDidMistake.toList()[0].second,
                                mapWhoDidNotMistake
                            )
                        ) {
                            Log.d(
                                TAG,
                                "removePlayerIfLose: ошибка критична, так как у ошибившився самое маленькое значение правильных ответов"
                            )
                            /** Если у кого то кто не сделал ошибку в этом раунде правильных ответов меньше чем тот кто ошибился
                             * тогда ошибка не критична */
                            if (whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first].toString() == "user") {
                                mainUserGameOver()
                            } else {
                                botGameOver(whoDidMistakesSortedMap, 0)
                            }

                        } else {
                            gameContinue("ошибка не критична игра продолжаеться")
                            /** ошибка не критична игра продолжаеться */
                        }
                    }
                }
                2 -> {
                    Log.d(TAG, "removePlayerIfLose: было совершено две ошибки")
                    whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first]?.compareTo(
                        whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[1].first]!!
                    )
                        .let {
                            when (it) {
                                -1 -> {
                                    /** выгоняем его*/
                                    if (whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first].toString() == "user") {
                                        mainUserGameOver()
                                    } else {
                                        botGameOver(whoDidMistakesSortedMap, 0)
                                    }
                                }
                                else -> {
                                    /** Повезло Равные значения*/
                                    gameContinue("2 сразу не могут вылететь игра продолжаеться")
                                }
                            }
                        }
                }
                3 -> {
                    Log.d(TAG, "removePlayerIfLose: было совершено три ошибки")
                    whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first]?.compareTo(
                        whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[2].first]!!
                    )
                        .let { it1 ->
                            when (it1) {
                                -1 -> {
                                    /** первое значение меньше, значит сравниваем его со вторым что бы решить выгнать ли игрока*/
                                    whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first]?.compareTo(
                                        whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[1].first]!!
                                    )
                                        .let {
                                            when (it) {
                                                -1 -> {
                                                    /** выгоняем его*/
                                                    if (isMainUser(whoDidMistakesSortedMap)) {
                                                        mainUserGameOver()
                                                    } else {
                                                        botGameOver(whoDidMistakesSortedMap, 0)
                                                    }
                                                }
                                                1 -> {
                                                    /** выгоняем его*/
                                                    if (isMainUser(whoDidMistakesSortedMap)) {
                                                        mainUserGameOver()
                                                    } else {
                                                        botGameOver(whoDidMistakesSortedMap, 1)
                                                    }
                                                }
                                                else -> {
                                                    gameContinue("2 сразу не могут вылететь игра продолжаеться")
                                                    /** Значит значения равные никто не вылетает так как 2 сразу вылететь не могут */
                                                }
                                            }
                                        }
                                }

                                else -> {
                                    gameContinue("3 сразу не могут вылететь игра продолжаеться")
                                    /** Значит значения одинаковые и вылетать некому!
                                     * а больше оно быть не может так как лист отсортирован от меньшего к большему*/

                                }
                            }
                        }
                }
                4 -> {
                    Log.d(TAG, "removePlayerIfLose: была совершено четыре ошибки")
                    whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first]?.compareTo(
                        whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[3].first]!!
                    )
                        .let { it1 ->
                            when (it1) {
                                -1 -> {
                                    /** первое значение меньше, значит сравниваем его со вторым что бы решить выгнать ли игрока*/
                                    whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first]?.compareTo(
                                        whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[2].first]!!
                                    )
                                        .let { it2 ->
                                            when (it2) {
                                                -1 -> {
                                                    /** первое значение меньше, значит сравниваем его со вторым что бы решить выгнать ли игрока*/
                                                    whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[0].first]?.compareTo(
                                                        whoDidMistakesSortedMap[whoDidMistakesSortedMap.toList()[1].first]!!
                                                    )
                                                        .let {
                                                            when (it) {
                                                                -1 -> {
                                                                    /** выгоняем его*/
                                                                    if (isMainUser(
                                                                            whoDidMistakesSortedMap
                                                                        )
                                                                    ) {
                                                                        mainUserGameOver()
                                                                    } else {
                                                                        botGameOver(
                                                                            whoDidMistakesSortedMap,
                                                                            0
                                                                        )
                                                                    }
                                                                }
                                                                else -> {
                                                                    gameContinue("2 сразу не могут вылететь игра продолжаеться")
                                                                    /** Значит значения равные никто не вылетает так как 2 сразу вылететь не могут */
                                                                }
                                                            }
                                                        }
                                                }
                                                else -> {
                                                    gameContinue("3 сразу не могут вылететь игра продолжаеться")
                                                    /** Значит значения равные никто не вылетает так как 2 сразу вылететь не могут */
                                                }
                                            }
                                        }
                                }

                                else -> {
                                    gameContinue("4 сразу не могут вылететь игра продолжаеться")
                                    /** Значит значения одинаковые и вылетать некому!
                                     * а больше оно быть не может так как лист отсортирован от меньшего к большему*/

                                }
                            }
                        }
                }
            }


        }
    }

    private fun gameContinue(string: String) {
        Log.d(TAG, "gameContinue: $string")
    }

    private fun isMistakeCritical(int: Int, whoDidNotMap: Map<String, Int>): Boolean {

        for (itemWhoDidNot in whoDidNotMap) {
            if (itemWhoDidNot.value < int) {
                //не критично
                return false
            }
        }
        //ошибка кричина тогда тру
        return true
    }

    private fun isMainUser(sortMap: Map<String, Int>): Boolean {

        sortMap.iterator()?.let {
            while (it.hasNext()) {
                val item = it.next()
                if (item.key.equals("user")) {
                    return true
                }

            }
        }
        return false
    }

    private fun botGameOver(sortMap: Map<String, Int>, intElementInList: Int) {
        Log.d(TAG, "botGameOver: ошибка была бота, убираем его")
        botAIList.iterator().let {
            while (it.hasNext()) {
                val item = it.next()
                if (item.name == sortMap.toList()[intElementInList].first) {
                    removePlayer(item)
                    it.remove()
                }
            }
        }
    }

    private fun mainUserGameOver() {
        Log.d(TAG, "mainUserGameOver:  ошибка была главного пользователя игра окончена!")
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK;
        startActivity(intent);
    }

    private fun removePlayer(item: BotAI) {
        item.icon?.visibility = View.GONE
        item.botNameTV?.visibility = View.GONE
        item.botResponseTV?.visibility = View.GONE
    }

    private fun showCounterView(boolean: Boolean) {
        if (boolean) {
            bindingStartQuiz.textViewCounter1.visibility = View.VISIBLE
            bindingStartQuiz.textViewCounter2.visibility = View.VISIBLE
            bindingStartQuiz.textViewCounter3.visibility = View.VISIBLE
            bindingStartQuiz.textViewCounter4.visibility = View.VISIBLE
        } else {
            bindingStartQuiz.textViewCounter1.visibility = View.INVISIBLE
            bindingStartQuiz.textViewCounter2.visibility = View.INVISIBLE
            bindingStartQuiz.textViewCounter3.visibility = View.INVISIBLE
            bindingStartQuiz.textViewCounter4.visibility = View.INVISIBLE
        }
    }


    private fun checkCountResponse() {
        countResponse++
        Log.d(TAG, "checkCountResponse: countResponse is:  $countResponse")
        if (countResponse == 4) {
            if (!isDestroyed) {
                executorStartQuiz?.execute {
                    TimeUnit.MILLISECONDS.sleep(800)
                    handlerStartQuiz?.sendEmptyMessage(setHandler_responseResult_4)
                }
            }
        }

    }


    private fun handlerCreating() {
        handlerStartQuiz = object : Handler(Looper.getMainLooper()!!) {
            @Synchronized
            override fun handleMessage(msg: Message) {
                if (!msg.data.isEmpty) {
                    playerReadyIconMap[msg.data.get("botName")].let {
                        it?.setBackgroundResource(R.drawable.shadow)
                        msg.data.clear()
                        Log.d(TAG, "handleMessage: Зашли боту дать фон рамки")
                    }
                } else {
                    when (msg.what) {
                        setHandler_QwestCountNotification_0 -> {
                            bindingStartQuiz.quest.visibility = View.VISIBLE
                        }
                        setHandler_showQwest_andLuanch_Timer_1 -> {
                            startQuestionShow(true)
                            startTimer()
                        }
                        setHandler_timerDownGrade_2 -> bindingStartQuiz.timer.setText(
                            timeCounter.toString()
                        )
                        setHandler_checkCountRespose_3 -> checkCountResponse()
                        setHandler_responseResult_4 -> responseResult()
                        setHandler_showCountion_of_votes_5 -> counterVotes()
                        setHandler_showPlayersCountWindows_6 -> showPlayersCountsWindows(true)
                        setHandler_chargeCounts_7 -> handlePlayerCount()
                        setHandler_returnTextSizeBack_8 -> returnSizeBack()
                        setHandler_removePlayerIfLose_9 -> removePlayerIfLose()
                        setHandler_removeQwest_and_Timer_10 -> {
                            bindingStartQuiz.quest.text = getString(R.string.Quest_two)
                            startQuestionShow(false)
                            showCounterView(false)
                        }

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

    private fun returnSizeBack() {
        for (item in botAIList) {
            item.botResponseTV?.textSize = 16F
        }

        mainUserWindow?.textSize = 16F

    }

    private fun showPlayersCountsWindows(boolean: Boolean) {
        if (boolean) {

            bindingStartQuiz.player1win.visibility = View.VISIBLE
            bindingStartQuiz.player2win.visibility = View.VISIBLE
            bindingStartQuiz.player3win.visibility = View.VISIBLE
            bindingStartQuiz.player4win.visibility = View.VISIBLE
        } else {
            bindingStartQuiz.player1win.visibility = View.INVISIBLE
            bindingStartQuiz.player2win.visibility = View.INVISIBLE
            bindingStartQuiz.player3win.visibility = View.INVISIBLE
            bindingStartQuiz.player4win.visibility = View.INVISIBLE

        }
    }

    private fun handlePlayerCount() {
        for (item in botAIList) {
            if (item.botAnswer == rightPlace) {
                var count1 = 0
                item.botResponseTV?.text.toString().toInt().let {
                    count1 = it
                    count1++
                    item.botResponseTV?.text = count1.toString()
                    item.botResponseTV?.textSize = 18F
                    item.botWinCounter++

                }

            }
        }
        if (mainUserResponse == rightPlace) {
            var count2 = mainUserWindow?.text.toString().toInt()
            count2++
            mainUserWindow?.text = count2.toString()
            mainUserWindow?.textSize = 18F
            mainUserWinCounter++
        }
    }

    private fun startTimer() {
        Log.d(TAG, "startTimer: $timeCounter")
        Log.d(TAG, "startTimer: $countResponse")
        executorStartQuiz?.execute(Runnable {

            run timerWhile@{
                while (timeCounter != 0) {
                    if (timeCounter <= 0 || countResponse == 4) {
                        return@timerWhile
                    }
                    //Посылаем уменьшение секунды на экран
                    handlerStartQuiz?.sendEmptyMessage(setHandler_timerDownGrade_2)
                    timeCounter--
                    TimeUnit.SECONDS.sleep(1)
                }
            }
            //Если Пользователь не ответил в положенное время то засчитываем как ошибку
            if (mainUserResponse == 0) {
                checkCountResponse()
            }

        })
        botThinkThreadStart()// Боты начинают думать когда запускаеться таймер
    }


    private fun questionsAndButtonsVisibility(boolean: Boolean) {
        if (boolean) {
            /** Кнопки показать  да\нет  таймер и квест */
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


    private fun botThinkThreadStart() {
        for (bot in botAIList) {
            executorStartQuiz?.execute(Runnable {
                try {
                    TimeUnit.SECONDS.sleep((4 until 19).random().toLong())
                    bot.botQuestResponse()
                    handlerStartQuiz?.sendEmptyMessage(setHandler_checkCountRespose_3)

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

    private fun questionsPrepare() {
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
                bindingStartQuiz.btnResponse1.text =
                    (questResponseList[questNumber].rightResponse)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].response4)
            } else if (rightPlace == 2) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse2.text =
                    (questResponseList[questNumber].rightResponse)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].response4)
            } else if (rightPlace == 3) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse3.text =
                    (questResponseList[questNumber].rightResponse)
                bindingStartQuiz.btnResponse4.text = (questResponseList[questNumber].response4)
            } else if (rightPlace == 4) {
                bindingStartQuiz.btnResponse1.text = (questResponseList[questNumber].response2)
                bindingStartQuiz.btnResponse2.text = (questResponseList[questNumber].response3)
                bindingStartQuiz.btnResponse3.text = (questResponseList[questNumber].response4)
                bindingStartQuiz.btnResponse4.text =
                    (questResponseList[questNumber].rightResponse)
            }
        }
    }

    private fun playerCreating() {

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
                                if (numbersOfWomenList.contains(it) && !innerArrayImages.contains(
                                        it.toString()
                                    )
                                ) {
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
            avatarIconMap[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon1.setImageResource(it) }
            bindingStartQuiz.playerName1.text = userNameIs
            mainUserWindow = bindingStartQuiz.player1win
            playerReadyIconMap["user"] = bindingStartQuiz.playerIcon1

            /** После основного пользователя размещаем остальных ботов */
            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon2,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player2win,
                            bindingStartQuiz.playerName2
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon3,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player3win,
                            bindingStartQuiz.playerName3
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon4,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player4win,
                            bindingStartQuiz.playerName4
                        )
                    )
                }
            }
        } else if (random == 2) {
            avatarIconMap[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon2.setImageResource(it) }
            playerReadyIconMap["user"] = bindingStartQuiz.playerIcon2
            bindingStartQuiz.playerName2.text = userNameIs
            mainUserWindow = bindingStartQuiz.player2win

            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName1.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon1,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player1win,
                            bindingStartQuiz.playerName1
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon3,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player3win,
                            bindingStartQuiz.playerName3
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon4,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player4win,
                            bindingStartQuiz.playerName4
                        )
                    )
                }
            }
        } else if (random == 3) {
            avatarIconMap[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon3.setImageResource(it) }
            playerReadyIconMap["user"] = bindingStartQuiz.playerIcon3
            bindingStartQuiz.playerName3.text = userNameIs
            mainUserWindow = bindingStartQuiz.player3win

            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName1.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon1,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player1win,
                            bindingStartQuiz.playerName1
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon2,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player2win,
                            bindingStartQuiz.playerName2
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon4,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player4win,
                            bindingStartQuiz.playerName4
                        )
                    )
                }
            }

        } else if (random == 4) {
            avatarIconMap[userAvatarNameIs]
                ?.let { bindingStartQuiz.playerIcon4.setImageResource(it) }
            playerReadyIconMap["user"] = bindingStartQuiz.playerIcon4
            bindingStartQuiz.playerName4.text = userNameIs
            mainUserWindow = bindingStartQuiz.player4win

            for (i in 0..2) {
                if (i == 0) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName1.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon1,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player1win,
                            bindingStartQuiz.playerName1
                        )
                    )
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon2,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player2win,
                            bindingStartQuiz.playerName2
                        )
                    )
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerReadyIconMap[innerArrayNames[i]] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        avatarIconMap[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                    botAIList.add(
                        BotAI(
                            innerArrayNames[i],
                            bindingStartQuiz.playerIcon3,
                            (70..85).random(),
                            rightPlace,
                            bindingStartQuiz.player3win,
                            bindingStartQuiz.playerName3
                        )
                    )
                }
            }
        }


    }

    override fun onDestroy() {
        handlerStartQuiz?.sendEmptyMessage(10)
        playerReadyIconMap.clear()
        botAIList.clear()
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


    @SuppressLint("ResourceAsColor")
    fun setToUserGaveChooseColor(v: View?) {
        v?.setBackgroundResource(R.drawable.btn_purpur_custom)
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




