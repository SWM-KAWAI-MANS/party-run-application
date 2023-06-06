package online.partyrun.partyrunapplication.utils.extension

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun <T> Context.setIntentActivity(
    it: Class<T>,
    extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}