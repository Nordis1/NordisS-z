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
        bindingChooseAva.btnMan1.setOnClickListener(this)
        bindingChooseAva.btnMan2.setOnClickListener(this)
        bindingChooseAva.btnMan3.setOnClickListener(this)
        bindingChooseAva.btnMan4.setOnClickListener(this)
        bindingChooseAva.btnMan5.setOnClickListener(this)
        bindingChooseAva.btnMan6.setOnClickListener(this)
        bindingChooseAva.btnWoman1.setOnClickListener(this)
        bindingChooseAva.btnWoman2.setOnClickListener(this)
        bindingChooseAva.btnWoman3.setOnClickListener(this)
        bindingChooseAva.btnWoman4.setOnClickListener(this)
        bindingChooseAva.btnWoman5.setOnClickListener(this)
        bindingChooseAva.btnWoman6.setOnClickListener(this)
        bindingChooseAva.btnAcceptFragmentChoose.setOnClickListener(this)
        bindingChooseAva.btnCancelFragmentChoose.setOnClickListener(this)
        return bindingChooseAva.root
    }

    override fun onClick(v: View?) {
        when (v?.tag) {
            bindingChooseAva.btnMan1.tag -> { creatingRaamka(bindingChooseAva.btnMan1) }
            bindingChooseAva.btnMan2.tag -> {creatingRaamka(bindingChooseAva.btnMan2)}
            bindingChooseAva.btnMan3.tag -> {creatingRaamka(bindingChooseAva.btnMan3)}
            bindingChooseAva.btnMan4.tag -> {creatingRaamka(bindingChooseAva.btnMan4)}
            bindingChooseAva.btnMan5.tag -> {creatingRaamka(bindingChooseAva.btnMan5)}
            bindingChooseAva.btnMan6.tag -> {creatingRaamka(bindingChooseAva.btnMan6)}
            bindingChooseAva.btnWoman1.tag -> {creatingRaamka(bindingChooseAva.btnWoman1)}
            bindingChooseAva.btnWoman2.tag -> {creatingRaamka(bindingChooseAva.btnWoman2)}
            bindingChooseAva.btnWoman3.tag -> {creatingRaamka(bindingChooseAva.btnWoman3)}
            bindingChooseAva.btnWoman4.tag -> {creatingRaamka(bindingChooseAva.btnWoman4)}
            bindingChooseAva.btnWoman5.tag -> {creatingRaamka(bindingChooseAva.btnWoman5)}
            bindingChooseAva.btnWoman6.tag -> {creatingRaamka(bindingChooseAva.btnWoman6)}
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