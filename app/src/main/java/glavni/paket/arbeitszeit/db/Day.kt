package glavni.paket.arbeitszeit.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.*

@Entity(tableName = "day_table")
data class Day (
    var timeLogIn: Date? = null,
    var timeLogOut: Date? = null
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null
    }