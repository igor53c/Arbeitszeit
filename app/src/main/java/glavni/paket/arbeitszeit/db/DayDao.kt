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

    @Query("select * from day_table order by timeLogIn desc, id desc limit 1")
    fun getLastDay(): LiveData<Day>

    @Query("select * from day_table where timeLogIn between :start and :end order by timeLogIn desc, id desc")
    fun getAllDayInWeek(start: Date, end: Date): LiveData<List<Day>>

    @Query("select exists(select * from day_table where (timeLogIn > :start and timeLogIn < :end) or (timeLogIn < :start and timeLogIn > :end) limit 1)")
    fun isLogInExistBetweenTwoDate(start: Date, end: Date) : LiveData<Boolean>

    @Query("select exists(select * from day_table where (timeLogOut > :start and timeLogOut < :end) or (timeLogOut < :start and timeLogOut > :end) limit 1)")
    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) : LiveData<Boolean>
}