package glavni.paket.arbeitszeit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Period::class, Day::class, Week::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DayDatabase : RoomDatabase() {

    abstract fun getPeriodDao(): PeriodDao

    abstract fun getDayDao(): DayDao

    abstract fun getWeekDao(): WeekDao
}