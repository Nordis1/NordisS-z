package nordis.nordisquiz.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import nordis.nordisquiz.FragmentTransactionActivity
import nordis.nordisquiz.R
import nordis.nordisquiz.databinding.FragmentChooseAvaBinding
import nordis.nordisquiz.userAvatarNameIs


@SuppressLint("StaticFieldLeak")
private lateinit var bindingChooseAva: FragmentChooseAvaBinding
var previousList = ArrayList<ImageButton>()
private const val TAG = "ChooseAva"
@SuppressLint("StaticFieldLeak")
var previousBtn: ImageButton? = null

class ChooseAva : Fragment(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingChooseAva = FragmentChooseAvaBinding.inflate(layoutInflater)
        bindingChooseAva.btn1.setOnClickListener(this)
        bindingChooseAva.btn2.setOnClickListener(this)
        bindingChooseAva.btn3.setOnClickListener(this)
        bindingChooseAva.btn4.setOnClickListener(this)
        bindingChooseAva.btn5.setOnClickListener(this)
        bindingChooseAva.btn6.setOnClickListener(this)
        bindingChooseAva.btn7.setOnClickListener(this)
        bindingChooseAva.btn8.setOnClickListener(this)
        bindingChooseAva.btn9.setOnClickListener(this)
        bindingChooseAva.btn10.setOnClickListener(this)
        bindingChooseAva.btn11.setOnClickListener(this)
        bindingChooseAva.btn12.setOnClickListener(this)
        bindingChooseAva.btn13.setOnClickListener(this)
        bindingChooseAva.btn14.setOnClickListener(this)
        bindingChooseAva.btn15.setOnClickListener(this)
        bindingChooseAva.btn16.setOnClickListener(this)
        bindingChooseAva.btn17.setOnClickListener(this)
        bindingChooseAva.btn18.setOnClickListener(this)
        bindingChooseAva.btn19.setOnClickListener(this)
        bindingChooseAva.btn20.setOnClickListener(this)
        bindingChooseAva.btn21.setOnClickListener(this)
        bindingChooseAva.btn22.setOnClickListener(this)
        bindingChooseAva.btn23.setOnClickListener(this)
        bindingChooseAva.btn24.setOnClickListener(this)
        bindingChooseAva.btn25.setOnClickListener(this)
        bindingChooseAva.btn26.setOnClickListener(this)
        bindingChooseAva.btn27.setOnClickListener(this)
        bindingChooseAva.btn28.setOnClickListener(this)
        bindingChooseAva.btn29.setOnClickListener(this)
        bindingChooseAva.btn30.setOnClickListener(this)
        bindingChooseAva.btn31.setOnClickListener(this)
        bindingChooseAva.btn32.setOnClickListener(this)
        bindingChooseAva.btn33.setOnClickListener(this)
        bindingChooseAva.btn34.setOnClickListener(this)
        bindingChooseAva.btn35.setOnClickListener(this)
        bindingChooseAva.btn36.setOnClickListener(this)
        bindingChooseAva.btn37.setOnClickListener(this)
        bindingChooseAva.btn39.setOnClickListener(this)
        bindingChooseAva.btn40.setOnClickListener(this)
        bindingChooseAva.btn41.setOnClickListener(this)
        bindingChooseAva.btn42.setOnClickListener(this)
        bindingChooseAva.btn43.setOnClickListener(this)
        bindingChooseAva.btn44.setOnClickListener(this)
        bindingChooseAva.btn45.setOnClickListener(this)
        bindingChooseAva.btn46.setOnClickListener(this)
        bindingChooseAva.btn47.setOnClickListener(this)
        bindingChooseAva.btn48.setOnClickListener(this)
        bindingChooseAva.btn49.setOnClickListener(this)
        bindingChooseAva.btn50.setOnClickListener(this)
        bindingChooseAva.btnAcceptFragmentChoose.setOnClickListener(this)
        bindingChooseAva.btnCancelFragmentChoose.setOnClickListener(this)
        return bindingChooseAva.root
    }

    override fun onClick(v: View?) {
        when (v?.tag) {
            bindingChooseAva.btn1.tag -> { creatingRaamka(v as ImageButton) }
            bindingChooseAva.btn2.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn3.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn4.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn5.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn6.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn7.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn8.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn9.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn10.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn11.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn12.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn13.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn14.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn15.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn16.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn17.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn18.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn19.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn20.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn21.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn22.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn23.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn24.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn25.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn26.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn27.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn28.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn29.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn30.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn31.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn32.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn33.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn34.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn35.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn36.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn37.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn38.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn39.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn40.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn41.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn42.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn43.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn44.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn45.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn46.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn47.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn48.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn49.tag -> {creatingRaamka(v as ImageButton)}
            bindingChooseAva.btn50.tag -> {creatingRaamka(v as ImageButton)}
        }
        when(v?.id){
            bindingChooseAva.btnAcceptFragmentChoose.id -> {
                val sPref = activity?.getSharedPreferences("AVATAR",Context.MODE_PRIVATE)
                sPref?.edit()?.putString("avatarName", userAvatarNameIs)?.apply()
                (activity as FragmentTransactionActivity)
                    .supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, UserProfile()).setReorderingAllowed(true)
                    .commit()
            }
            bindingChooseAva.btnCancelFragmentChoose.id ->{
                (activity as FragmentTransactionActivity)
                    .supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, UserProfile()).setReorderingAllowed(true)
                    .commit()
            }
        }
    }

    fun creatingRaamka(imageButton: ImageButton){
        if (previousList.isNotEmpty()){
                Log.d(TAG, "creatingRaamka: two")
            Log.d(TAG, "creatingRaamka: ${previousList[0].tag}")
            Log.d(TAG, "creatingRaamka: ${imageButton.tag}")
            if (previousList[0].tag.toString() != imageButton.tag.toString()) {
                Log.d(TAG, "creatingRaamka: three")
                previousList[0]
                previousList[0].setBackgroundResource(R.drawable.btn_invisible)
                imageButton.setBackgroundResource(R.drawable.shadow)
                previousList[0] = imageButton
                userAvatarNameIs = imageButton.tag.toString()
            }
        }else{
            Log.d(TAG, "creatingRaamka: one")
            previousList.add(imageButton)
            userAvatarNameIs = imageButton.tag.toString()
            imageButton.setBackgroundResource(R.drawable.shadow)
        }
    }

}