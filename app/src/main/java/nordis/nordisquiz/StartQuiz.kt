package nordis.nordisquiz


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import nordis.nordisquiz.databinding.ActivityStartQuizBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


private const val TAG = "StartQuiz"
private val executorStartQuiz = Executors.newCachedThreadPool();
val playerMap = HashMap<String, ImageView>()

@SuppressLint("StaticFieldLeak")
private lateinit var bindingStartQuiz: ActivityStartQuizBinding

private var handler1: Handler? = null
private var questNumber: Int = 0
private var rightPlace: Int = 0
private var timeCounter: Int = 10

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
        gameStarting()
        playerCreating()
        //startTimer()


    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View?) {

        when (rightPlace) {
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
        }

    }


    fun gameStarting() {
        if (questResponseList.size != 0) {
            rightPlace = (1..4).random()
            questNumber = (0 until questResponseList.size).random()

            bindingStartQuiz.questionPlace.setText(questResponseList.get(questNumber).question)
            if (rightPlace == 1) {
                bindingStartQuiz.btnResponse1.setText(questResponseList.get(questNumber).rightResponse)
                bindingStartQuiz.btnResponse2.setText(questResponseList.get(questNumber).response2)
                bindingStartQuiz.btnResponse3.setText(questResponseList.get(questNumber).response3)
                bindingStartQuiz.btnResponse4.setText(questResponseList.get(questNumber).response4)
            } else if (rightPlace == 2) {
                bindingStartQuiz.btnResponse1.setText(questResponseList.get(questNumber).response2)
                bindingStartQuiz.btnResponse2.setText(questResponseList.get(questNumber).rightResponse)
                bindingStartQuiz.btnResponse3.setText(questResponseList.get(questNumber).response3)
                bindingStartQuiz.btnResponse4.setText(questResponseList.get(questNumber).response4)
            } else if (rightPlace == 3) {
                bindingStartQuiz.btnResponse1.setText(questResponseList.get(questNumber).response2)
                bindingStartQuiz.btnResponse2.setText(questResponseList.get(questNumber).response3)
                bindingStartQuiz.btnResponse3.setText(questResponseList.get(questNumber).rightResponse)
                bindingStartQuiz.btnResponse4.setText(questResponseList.get(questNumber).response4)
            } else if (rightPlace == 4) {
                bindingStartQuiz.btnResponse1.setText(questResponseList.get(questNumber).response2)
                bindingStartQuiz.btnResponse2.setText(questResponseList.get(questNumber).response3)
                bindingStartQuiz.btnResponse3.setText(questResponseList.get(questNumber).response4)
                bindingStartQuiz.btnResponse4.setText(questResponseList.get(questNumber).rightResponse)
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
                    playerMap["bot1"] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerMap["bot2"] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerMap["bot3"] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
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
                    playerMap["bot1"] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerMap["bot2"] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerMap["bot3"] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
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
                    playerMap["bot1"] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerMap["bot2"] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName4.text = it }
                    playerMap["bot3"] = bindingStartQuiz.playerIcon4
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon4.setImageResource(
                                it1
                            )
                        }
                    }
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
                    playerMap["bot1"] = bindingStartQuiz.playerIcon1
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon1.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 1) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName2.text = it }
                    playerMap["bot2"] = bindingStartQuiz.playerIcon2
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon2.setImageResource(
                                it1
                            )
                        }
                    }
                } else if (i == 2) {
                    innerArrayNames[i].let { bindingStartQuiz.playerName3.text = it }
                    playerMap["bot3"] = bindingStartQuiz.playerIcon3
                    innerArrayImages[i].let {
                        map[it]?.let { it1 ->
                            bindingStartQuiz.playerIcon3.setImageResource(
                                it1
                            )
                        }
                    }
                }
            }
        }


    }

    private fun handlerCreating() {
        handler1 = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> bindingStartQuiz.timer.setText(timeCounter.toString())
                    2 -> {
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


    fun startTimer() {
        executorStartQuiz.execute(Runnable {
            while (timeCounter != 0) {
                TimeUnit.SECONDS.sleep(1)
                timeCounter--
                handler1?.sendEmptyMessage(1)
            }
            handler1?.sendEmptyMessage(2)
        })
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

}