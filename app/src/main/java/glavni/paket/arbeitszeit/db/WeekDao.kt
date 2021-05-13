package glavni.paket.arbeitszeit.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface WeekDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeek(week: Week)

    @Delete
    fun deleteWeek(week: Week)

    @Update
    fun updateWeek(week: Week)

    @Query("select exists(select * from week_table where start = :start)")
    fun isWeekExist(start: Date) : Boolean?

    @Query("select * from week_table where start = :start limit 1")
    fun getWeek(start: Date) : Week?

    @Query("select * from week_table where start = :start limit 1")
    fun getWeekLive(start: Date) : LiveData<Week>

    @Query("select * from week_table order by start desc")
    fun getAllWeeksLive() : LiveData<List<Week>?>

    @Query("select * from week_table")
    fun getAllWeeks() : List<Week>?

    @Query("select sum(balance) from week_table")
    fun getSumAllWeeks() : LiveData<Long?>
}