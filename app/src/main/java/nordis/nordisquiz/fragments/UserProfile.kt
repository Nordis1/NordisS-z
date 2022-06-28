package nordis.nordisquiz.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nordis.nordisquiz.*
import nordis.nordisquiz.databinding.FragmentUserProfileBinding


@SuppressLint("StaticFieldLeak")
private lateinit var bindingUserProfile: FragmentUserProfileBinding
private const val TAG = "UserProfile"

class UserProfile : Fragment(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingUserProfile = FragmentUserProfileBinding.inflate(layoutInflater)
        bindingUserProfile.btnAccept.setOnClickListener(this)
        bindingUserProfile.UserMainAvatar.setOnClickListener(this)
        userNameInitialize()
        userIconInitialize()
        return bindingUserProfile.root
    }

    fun userNameInitialize(){
        Log.d(TAG, "userNameInitialize: Началась инициализация имени")
        activity?.getSharedPreferences("USERNAME", Context.MODE_PRIVATE)?.getString("userName","")?.let {
            Log.d(TAG, "userNameInitialize: it: $it")
            if (it.isEmpty()){
                Log.d(TAG, "userNameInitialize: Имя пустое, загружаем стандартное")
                userNameIs = getString(R.string.UserStandardName)
                bindingUserProfile.userEditNameText.setText(userNameIs)
                activity?.getSharedPreferences("USERNAME", Context.MODE_PRIVATE)?.edit()?.putString("userName",
                    userNameIs)?.apply()

            }else{
                Log.d(TAG, "userNameInitialize: Загрузка Имени")
                bindingUserProfile.userEditNameText.setText(it)

            }
        }
    }

    fun userIconInitialize(){
        Log.d(TAG, "userIconInitialize: Началась инициализация Иконки")
        userAvatarNameIs = activity?.getSharedPreferences("AVATAR", Context.MODE_PRIVATE)
            ?.getString("avatarName", "")
        if (userAvatarNameIs == null || userAvatarNameIs.equals("") ) {
            //Если инфы нет тогда загружаем обычную
            bindingUserProfile.UserMainAvatar.setBackgroundResource(R.mipmap.user_profile2)
            activity?.getSharedPreferences("AVATAR", Context.MODE_PRIVATE)?.edit()?.putString("avatarName","st")?.apply()
        }else{
            map[userAvatarNameIs]
                ?.let { bindingUserProfile.UserMainAvatar.setBackgroundResource(it) }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //Принять настройки
            //Сохраняем тут только имя, так как Картинка автоматически сохранятеться в выборе Аватарки
            bindingUserProfile.btnAccept.id -> {
                if (bindingUserProfile.userEditNameText.text.isNotEmpty() &&
                        bindingUserProfile.userEditNameText.text.toString() != (R.string.UserStandardName.toString()) ) {
                    userNewNameSave(true)
                    activity?.onBackPressed()
                } else{
                    userNewNameSave(false)
                    Log.d(TAG, "onClick: editText was empty!")
                    activity?.onBackPressed()
                }

            }
            bindingUserProfile.UserMainAvatar.id -> {
                //перед загрузкой нового Аватара проверяем было ли изменено имя.
                if (bindingUserProfile.userEditNameText.text.isNotEmpty() &&
                    bindingUserProfile.userEditNameText.text.toString() != (R.string.UserStandardName.toString()) ) {
                    userNewNameSave(true)
                } else{
                    userNewNameSave(false)
                }

                //Далее запускаем выбиралку Аватарки.
                Log.d(TAG, "onClick: Transactions has Starts...")
                (activity as FragmentTransactionActivity)
                    .supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, ChooseAva()).setReorderingAllowed(true)
                    .commit()
            }
        }
    }
    fun userNewNameSave(boolean: Boolean){
        if (boolean) {
            Log.d(TAG, "userNewNameSave: Сохраняем новое имя пользователя!")

            userNameIs = bindingUserProfile.userEditNameText.text.toString()
            activity?.getSharedPreferences("USERNAME", Context.MODE_PRIVATE)?.edit()?.putString(
                "userName",
                userNameIs
            )?.apply()
            Log.d(TAG, "onClick: edit text is not empty, so userNameIs : $userNameIs")
        }else{
            userNameIs = getString(R.string.UserStandardName)
            activity?.getSharedPreferences("USERNAME", Context.MODE_PRIVATE)?.edit()?.putString(
                "userName",
                userNameIs
            )?.apply()
        }
    }
}


