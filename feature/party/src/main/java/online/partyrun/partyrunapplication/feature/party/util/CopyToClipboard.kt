package online.partyrun.partyrunapplication.feature.party.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("파티 번호 복사", text)
    clipboard.setPrimaryClip(clip)
}
