package com.example.useful_photo_album.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.useful_photo_album.data.remote.UnsplashPhoto
import com.example.useful_photo_album.tester.model.SubunsplashRemoteKey
import com.example.useful_photo_album.tester.model.SubunsplashRemoteKeyDao
import com.example.useful_photo_album.tester.model.UnsplashPhotoDao
import com.example.useful_photo_album.tester.model.UnsplashRandomPhoto
import com.example.useful_photo_album.utilities.DATABASE_NAME
import com.example.useful_photo_album.workers.SeedDatabaseWorker

@Database(entities = [PhotoWrite::class, PhotoTheme::class, UnsplashRandomPhoto::class, SubunsplashRemoteKey::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoWriteDao(): PhotoWriteDao
    abstract fun photoThemeDao(): PhotoThemeDao
    abstract fun posts(): UnsplashPhotoDao
    abstract fun remoteKeys(): SubunsplashRemoteKeyDao


    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                        object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                                WorkManager.getInstance(context).enqueue(request)
                            }
                        }
                )
                .build()
        }
    }
}
