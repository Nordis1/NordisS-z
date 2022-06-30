package nordis.nordisquiz

import android.widget.ImageView

class BotAI(
     var name: String?,
     var icon: ImageView?,
     var aI: Int?,
     var rightPlace: Int?
)
{

    var botBoolAnswer = false
    var botAnswer = 0


    fun botQuestResponse() {
        (0..100).random().let { if (it <= aI!!){
            botBoolAnswer = true
            botAnswer = rightPlace!!
        }else{
            while (botAnswer != rightPlace){
                botAnswer = (1..4).random()
            }
            botBoolAnswer = true
        }
        }
    }


}