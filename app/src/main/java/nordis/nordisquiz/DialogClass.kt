package nordis.nordisquiz

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class DialogClass {
    var context: Context? = null
    var constructorTitle: String? = null
    var constructorMessage: String? = null
    var constructorBtnPossitive: String? = null
    var constructorBtnNegative: String? = null

    constructor(
        context: Context?,
        constructorTitle: String?,
        constructorMessage: String?,
        constructorBtnPossitive: String?,
        constructorBtnNegative: String?
    ) {
        this.context = context
        this.constructorTitle = constructorTitle
        this.constructorMessage = constructorMessage
        this.constructorBtnPossitive = constructorBtnPossitive
        this.constructorBtnNegative = constructorBtnNegative
    }

    fun standartDialogMessage() {
        val dialBuilder = AlertDialog.Builder(context)
        dialBuilder.setTitle(constructorTitle)
        dialBuilder.setMessage(constructorMessage)
        /*  dialBuilder.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
              dialogInterface.cancel()
          }*/
        dialBuilder.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        dialBuilder.create().show()



    }
}