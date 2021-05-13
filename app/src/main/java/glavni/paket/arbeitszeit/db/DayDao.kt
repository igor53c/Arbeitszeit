package glavni.paket.arbeitszeit.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDay(day: Day)

    @Delete
    fun deleteDay(day: Day)

    @Update
    fun updateDay(day: Day)

    @Query("select exists(select * from day_table where firstLogIn between :start and :end)")
    fun isDayLogInExistBetweenTwoDate(start: Date, end: Date) : Boolean?

    @Query("select * from day_table where firstLogIn between :start and :end limit 1")
    fun getDayBetweenTwoDate(start: Date, end: Date) : Day?

    @Query("select * from day_table where firstLogIn between :start and :end order by firstLogIn desc, id desc")
    fun getAllDaysInWeekLive(start: Date, end: Date): LiveData<List<Day>>

    @Query("select * from day_table where firstLogIn between :start and :end order by firstLogIn desc, id desc")
    fun getAllDaysInWeek(start: Date, end: Date): List<Day>?

    @Query("select * from day_table")
    fun getAllDays(): List<Day>?
}