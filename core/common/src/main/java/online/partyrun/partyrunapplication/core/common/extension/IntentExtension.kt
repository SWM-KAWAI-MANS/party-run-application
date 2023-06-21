package online.partyrun.partyrunapplication.core.common.extension

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun <T> Context.setIntentActivity(
    it: Class<T>,
    flags: Int? = null,
    extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    flags?.let { intent.flags = it }
    startActivity(intent)
}