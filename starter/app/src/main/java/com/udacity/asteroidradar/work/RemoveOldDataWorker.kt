package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getLastDayOfWeek
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NasaRepository
import retrofit2.HttpException
import java.lang.Exception

class RemoveOldDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RemoveOldDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = NasaRepository(database)
        return try {
            repository.deleteOldAsteroid(getTodayDate())
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}