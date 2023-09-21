package online.partyrun.partyrunapplication.feature.party

enum class KmState(val imageRes: Int) {
    KM_1(R.drawable.party_track_1km),
    KM_3(R.drawable.party_track_3km),
    KM_5(R.drawable.party_track_5km),
    KM_10(R.drawable.party_track_10km);
}


fun KmState.toDistance(): Int = when (this) {
    KmState.KM_1 -> 1000
    KmState.KM_3 -> 3000
    KmState.KM_5 -> 5000
    KmState.KM_10 -> 10000
}
