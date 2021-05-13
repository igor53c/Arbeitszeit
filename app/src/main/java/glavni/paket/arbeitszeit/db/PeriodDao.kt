package glavni.paket.arbeitszeit.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface PeriodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPeriod(period: Period)

    @Delete
    fun deletePeriod(period: Period)

    @Update
    fun updatePeriod(period: Period)

    @Query("select * from period_table order by timeLogIn desc, id desc limit 1")
    fun getLastPeriodLive(): LiveData<Period>

    @Query("select * from period_table order by timeLogIn desc, id desc limit 1")
    fun getLastPeriod(): Period?

    @Query("select * from period_table where timeLogIn between :start and :end order by timeLogIn desc, id desc")
    fun getAllPeriodsBetweenTwoDateLive(start: Date, end: Date): LiveData<List<Period>>

    @Query("select * from period_table where timeLogIn between :start and :end order by timeLogIn desc, id desc")
    fun getAllPeriodsBetweenTwoDate(start: Date, end: Date): List<Period>?

    @Query("select exists(select * from period_table where (timeLogIn > :start and timeLogIn < :end) or (timeLogIn < :start and timeLogIn > :end) limit 1)")
    fun isLogInExistBetweenTwoDate(start: Date, end: Date) : LiveData<Boolean>

    @Query("select exists(select * from period_table where (timeLogOut > :start and timeLogOut < :end) or (timeLogOut < :start and timeLogOut > :end) limit 1)")
    fun isLogOutExistBetweenTwoDate(start: Date, end: Date) : LiveData<Boolean>

    @Query("select exists(select * from period_table where (:start between timeLogIn and timeLogOut) and (:end between timeLogIn and timeLogOut) limit 1)")
    fun isTwoDateExistBetweenLogInAndLogOut(start: Date, end: Date) : LiveData<Boolean>

    @Query("select count(*) from period_table where timeLogIn between :start and :end")
    fun numberPeriodBetweenTwoDate(start: Date, end: Date): Int?

    @Query("select count(*) from period_table")
    fun numberPeriodsInTable(): Int?
}