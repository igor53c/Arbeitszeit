package glavni.paket.arbeitszeit.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "day_table")
data class Day(
    var firstLogIn: Date? = null,
    var lastLogOut: Date? = null,
    var workDay: Boolean? = null,
    var workingTime: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}