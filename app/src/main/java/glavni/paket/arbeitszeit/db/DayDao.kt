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

    @Query("SELECT * from day_table order by timeLogIn DESC, id DESC LIMIT 1")
    fun getLastDay(): LiveData<Day>

    @Query("DELETE FROM day_table")
    fun deleteAllDays()

    @Query("SELECT * from day_table order by timeLogIn DESC")
    fun getAllDays(): LiveData<List<Day>>

    @Query("SELECT * FROM day_table WHERE timeLogIn BETWEEN :start AND :end order by timeLogIn DESC, id DESC")
    fun getAllDayInWeek(start: Date, end: Date): LiveData<List<Day>>

    @Query("SELECT EXISTS(SELECT * FROM day_table WHERE timeLogIn BETWEEN :start AND :end LIMIT 1)")
    fun isLogInExistBetweenTwoDate(start: Date, end: Date) : LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM day_table WHERE timeLogOut BETWEEN :start AND :end LIMIT 1)")
    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) : LiveData<Boolean>

}