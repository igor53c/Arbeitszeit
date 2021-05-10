package glavni.paket.arbeitszeit.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "week_table")
data class Week(
    var start: Date? = null,
    var end: Date? = null,
    var workingTime: Long? = null,
    var balance: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}