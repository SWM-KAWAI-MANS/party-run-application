package online.partyrun.partyrunapplication.core.datastore.datasource

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.datastore.UserPreferences
import online.partyrun.partyrunapplication.core.model.user.User
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    // 매핑을 통해 필요한 사용자 데이터 생성
    val userData = userPreferences.data
        .map { preferences ->
            User(
                id = preferences.id,
                nickName = preferences.name,
                profileImage = preferences.profile
            )
        }

    suspend fun setUserName(userName: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setName(userName)
                .build()
        }
    }

    suspend fun setUserProfile(userProfile: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setProfile(userProfile)
                .build()
        }
    }

    suspend fun setUserId(userId: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setId(userId)
                .build()
        }
    }

}
