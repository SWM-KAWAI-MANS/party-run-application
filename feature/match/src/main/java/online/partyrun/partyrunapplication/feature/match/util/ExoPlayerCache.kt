package online.partyrun.partyrunapplication.feature.match.util

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

// cache를 저장할 폴더명
const val cacheFolder = ".playerCache"

object ExoPlayerCache {
    private var cache: Cache? = null
    fun get(context: Context): Cache {
        if (cache == null) {
            val cacheSize: Long = 30 * 1024 * 1024 // LRU 방식으로 30MB 사용
            cache = SimpleCache(
                File(context.cacheDir, cacheFolder),
                LeastRecentlyUsedCacheEvictor(cacheSize),
                StandaloneDatabaseProvider(context.applicationContext)
            )
        }
        return cache as Cache
    }
}

fun getCacheDataSourceFactory(cache: Cache): DataSource.Factory {
    return CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
}
