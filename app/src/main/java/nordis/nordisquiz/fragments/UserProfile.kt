package nordis.nordisquiz.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nordis.nordisquiz.FragmentTransactionActivity
import nordis.nordisquiz.R
import nordis.nordisquiz.databinding.FragmentUserProfileBinding
import nordis.nordisquiz.userAvatarNameIs
import nordis.nordisquiz.userNameIs


@SuppressLint("StaticFieldLeak")
private lateinit var bindingUserProfile: FragmentUserProfileBinding
private const val TAG = "UserProfile"
val map = HashMap<String,Int>()

class UserProfile : Fragment(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imageMapCreating()
        bindingUserProfile = FragmentUserProfileBinding.inflate(layoutInflater)
        bindingUserProfile.btnAccept.setOnClickListener(this)
        bindingUserProfile.btnCancel.setOnClickListener(this)
        bindingUserProfile.btnGetAvatar.setOnClickListener(this)
        userAvatarNameIs = activity?.getSharedPreferences("AVATAR", Context.MODE_PRIVATE)
            ?.getString("avatarName", "")
        if (userAvatarNameIs == null || userAvatarNameIs.equals("") ) {
            bindingUserProfile.UserMainAvatar.setBackgroundResource(R.mipmap.user_profile2)
        }else{
            map.get(userAvatarNameIs)
                ?.let { bindingUserProfile.UserMainAvatar.setBackgroundResource(it) }
        }
        return bindingUserProfile.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            bindingUserProfile.btnAccept.id -> {
                if (bindingUserProfile.userEditNameText.text.isNotEmpty()) {
                    userNameIs = bindingUserProfile.userEditNameText.text.toString()
                    Log.d(TAG, "onClick: edit text is not empty, so userNameIs : $userNameIs")
                } else Log.d(TAG, "onClick: editText was empty!")

                if (bindingUserProfile.UserMainAvatar.tag.toString().equals("userMainAvatar")) {
                    Log.d(TAG, "onClick: Tag is math")
                } else {
                    Log.d(TAG, "onClick: Tag doesn't math")
                }

            }
            bindingUserProfile.btnGetAvatar.id -> {
                Log.d(TAG, "onClick: Transactions has Starts...")
                (activity as FragmentTransactionActivity)
                    .supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, ChooseAva()).setReorderingAllowed(true)
                    .commit()
                /*          val transaction = childFragmentManager.beginTransaction()
                          transaction.replace(R.id.fragmentContainerView,ChooseAva()).setReorderingAllowed(true)
                          transaction.commit()*/
            }
        }
    }

    fun imageMapCreating(){
        map["m1"] = R.mipmap.m1
        map["m2"] = R.mipmap.m2
        map["m3"] = R.mipmap.m3
        map["m4"] = R.mipmap.m4
        map["m5"] = R.mipmap.m5
        map["m6"] = R.mipmap.m6
        map["w1"] = R.mipmap.w1
        map["w2"] = R.mipmap.w2
        map["w3"] = R.mipmap.w3
        map["w4"] = R.mipmap.w4
        map["w5"] = R.mipmap.w5
        map["w6"] = R.mipmap.w6
    }
}