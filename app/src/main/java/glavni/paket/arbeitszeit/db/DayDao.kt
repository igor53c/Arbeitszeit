package glavni.paket.arbeitszeit.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface DayDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDay(day: Day)

    @Delete
    suspend fun deleteDay(day: Day)

    @Update
    suspend fun updateDay(day: Day)

    @Query("SELECT * from day_table order by id DESC LIMIT 1")
    fun getLastDay(): LiveData<Day>

    @Query("DELETE FROM day_table")
    fun deleteAllDays()

    @Query("SELECT * from day_table order by id DESC")
    fun getAllDays(): LiveData<List<Day>>


}