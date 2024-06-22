package com.app.geradordesenhaaleatorio.extention

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.core.util.rangeTo
import com.app.geradordesenhaaleatorio.ultis.kindPass.caracteresFortes
import com.app.geradordesenhaaleatorio.ultis.kindPass.caracteresFracos
import com.app.geradordesenhaaleatorio.ultis.kindPass.caracteresMedios

import kotlin.random.Random

fun geradorKey(tamanho: Int, nivel: String): String {
    val senha = StringBuilder()
    val caracteresPermitidos = when (nivel) {
        "forte" -> caracteresFortes
        "media" -> caracteresMedios
        "fraca" -> caracteresFracos

        else -> caracteresMedios
    }
    for (i in 0 until tamanho) {
        val indice = Random.nextInt(caracteresPermitidos.length)
        senha.append(caracteresPermitidos[indice])
    }

    return senha.toString()
}

fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    return dialog
}
fun copyText(string: String, context: Context){
    val  clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipdata = ClipData.newPlainText("Text copied", string)
    clipboardManager.setPrimaryClip(clipdata)
}


