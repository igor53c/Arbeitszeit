package glavni.paket.arbeitszeit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Day::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class DayDatabase : RoomDatabase() {

    abstract fun getDayDao(): DayDao
}