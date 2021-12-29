//package com.example.useful_photo_album.data.worker
//
//import android.content.Context
//import android.util.Log
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.google.gson.stream.JsonReader
//import kotlinx.coroutines.coroutineScope
//
//class SeedDatabaseWorker(
//    context: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result = coroutineScope {
//        try {
//            applicationContext.assets.open(PHOTO_DATA_FILENAME).use { inputStream ->
//                JsonReader(inputStream.reader()).use { jsonReader ->
//                    val themeType = object : TypeToken<List<PhotoTheme>>() {}.type
//                    val themeList: List<PhotoTheme> = Gson().fromJson(jsonReader, themeType)
//
//                    val database = AppDatabase.getInstance(applicationContext)
//                    database.photoThemeDao().insertAll(themeList)
//
//                    Result.success()
//                }
//            }
//        } catch (ex: Exception) {
//            Log.e(TAG, "Error seeding database", ex)
//            Result.failure()
//        }
//    }
//
//    companion object {
//        private const val TAG = "SeedDatabaseWorker"
//    }
//}