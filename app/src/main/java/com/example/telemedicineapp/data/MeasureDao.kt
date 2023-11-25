package com.example.telemedicineapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureDao {

    @Upsert
    suspend fun upsertMeasure(measure: Measure)

    @Delete
    suspend fun deleteMeasure(measure: Measure)

    @Query("SELECT * FROM Measure")
    fun getMeasures(): Flow<List<Measure>>

    @Query("SELECT * FROM Measure WHERE id = :id")
    fun getMeasure(id: Int): Flow<Measure>

    @Query("UPDATE Measure SET synced = 1 WHERE id = :measureId")
    suspend fun setSynced(measureId: Int)
}