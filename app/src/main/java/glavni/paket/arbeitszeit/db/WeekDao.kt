package glavni.paket.arbeitszeit.db

import androidx.room.*

@Dao
interface WeekDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeek(week: Week)

    @Delete
    suspend fun deleteWeek(week: Week)

    @Update
    suspend fun updateWeek(week: Week)
}