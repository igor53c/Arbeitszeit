package glavni.paket.arbeitszeit.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDay(day: Day)

    @Delete
    suspend fun deleteDay(day: Day)

    @Update
    suspend fun updateDay(day: Day)

    @Query("select exists(select * from day_table where firstLogIn between :start and :end)")
    fun isDayLogInExistBetweenTwoDate(start: Date, end: Date) : Boolean?

    @Query("select * from day_table where firstLogIn between :start and :end limit 1")
    fun getDayBetweenTwoDate(start: Date, end: Date) : Day?

    @Query("select * from day_table where firstLogIn between :start and :end order by firstLogIn desc, id desc")
    fun getAllDaysInWeek(start: Date, end: Date): LiveData<List<Day>>
}