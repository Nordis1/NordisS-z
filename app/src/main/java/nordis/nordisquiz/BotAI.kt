package nordis.nordisquiz

import android.widget.ImageView
import android.widget.TextView

class BotAI(
     var name: String?,
     var icon: ImageView?,
     var aI: Int?,
     var rightPlace: Int?,
     var botResponseTV: TextView?,
     var botNameTV: TextView?
)
{
    var botWinCounter = 0
    var botBoolAnswer = false
    var botAnswer = rightPlace!!


    fun botQuestResponse() {
        (0..100).random().let { if (it <= aI!!){
            botBoolAnswer = true
        }else{
            while (botAnswer == rightPlace){
                botAnswer = (1..4).random()
            }
            botBoolAnswer = false
        }
        }
    }


}