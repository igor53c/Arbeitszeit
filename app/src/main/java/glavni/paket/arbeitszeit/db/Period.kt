package glavni.paket.arbeitszeit.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "period_table")
data class Period (
    var timeLogIn: Date? = null,
    var timeLogOut: Date? = null,
    var workDay: Boolean? = null,
    var workingTime: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}