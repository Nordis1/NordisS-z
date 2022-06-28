package nordis.nordisquiz

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import nordis.nordisquiz.databinding.ActivityStartQuizBinding
import java.util.HashSet
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val TAG = "StartQuiz"
private val executorStartQuiz = Executors.newCachedThreadPool();

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
                Log.d(TAG, "onClick: Variant 1 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 1: Ответ не правильный")
            }
            2 -> if (v == bindingStartQuiz.btnResponse2) {
                setToGreenColor(bindingStartQuiz.btnResponse2)
                Log.d(TAG, "onClick: Variant 2 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 2: Ответ не правильный")
            }
            3 -> if (v == bindingStartQuiz.btnResponse3) {
                setToGreenColor(bindingStartQuiz.btnResponse3)
                Log.d(TAG, "onClick: Variant 3 : Ответ правильный")
            } else if (v != null) {
                setToRedColor(v)
                Log.d(TAG, "onClick: Variant 3: Ответ не правильный")
            }
            4 -> if (v == bindingStartQuiz.btnResponse4) {
                setToGreenColor(bindingStartQuiz.btnResponse4)
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

    fun playerCreating(){

        val innerList1 = HashSet<String>()
        while (innerList1.size != 3){
            val name: String = nanesList[(0..nanesList.size).random()]
            innerList1.add(name)
        }
        val random = (1..4).random()
        for (item in innerList1){
            if (random == 1){
                map[userAvatarNameIs]
                    ?.let { bindingStartQuiz.playerIcon1.setBackgroundResource(it) }
                bindingStartQuiz.playerName1.text = userNameIs
                //bindingStartQuiz.playerIcon2.setBackgroundResource()
                bindingStartQuiz.playerName2.text = item
            }else if (random == 2){
                map[userAvatarNameIs]
                    ?.let { bindingStartQuiz.playerIcon2.setBackgroundResource(it) }
                bindingStartQuiz.playerName2.text = userNameIs
            }else if (random == 3){
                map[userAvatarNameIs]
                    ?.let { bindingStartQuiz.playerIcon3.setBackgroundResource(it) }
                bindingStartQuiz.playerName3.text = userNameIs
            }else if (random == 4){
                map[userAvatarNameIs]
                    ?.let { bindingStartQuiz.playerIcon4.setBackgroundResource(it) }
                bindingStartQuiz.playerName4.text = userNameIs
            }
            Log.d(TAG, "playerCreating: Players come get Names $item")
/*            if (bindingStartQuiz.userName2TV.text.equals(getString(R.string.PlayerNames))){
                bindingStartQuiz.userName2TV.text = item
            }else if (bindingStartQuiz.userName3TV.text.equals(getString(R.string.PlayerNames))){
                bindingStartQuiz.userName3TV.text = item
            }else if (bindingStartQuiz.userName4TV.text.equals(getString(R.string.PlayerNames))){
                bindingStartQuiz.userName4TV.text = item
            }*/
        }
        //var name: String = nanesList.get((0..nanesList.size).random())
        //bindingStartQuiz.userNameTV.setText(name)
    }

    private fun handlerCreating() {
        handler1 = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> bindingStartQuiz.timer.setText(timeCounter.toString())
                    2 -> {
                        val dialog = DialogClass(
                            bindingStartQuiz.root.context, "Время вышло", "Вы не ответили на вопрос во время!", null, null
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