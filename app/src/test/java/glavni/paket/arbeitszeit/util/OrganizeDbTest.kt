package glavni.paket.arbeitszeit.util

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class OrganizeDbTest {

    @Test
    fun isResultCorrectWithoutRounding_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 55)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date1 = cal.time
        cal.set(Calendar.HOUR_OF_DAY, 15)
        cal.set(Calendar.MINUTE, 10)

        val result = resultDay(date1, cal.time, 1000L * 60 * 60 * 8, false, 0, 30,
            45)

        assertEquals(result, 1000L * 60 * 60 * 8)
    }

    @Test
    fun isResultCorrectWithRounding_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 55)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date1 = cal.time
        cal.set(Calendar.HOUR_OF_DAY, 15)
        cal.set(Calendar.MINUTE, 10)

        val result = resultDay(date1, cal.time, 1000L * 60 * 60 * 8, true, 0, 30,
            45)

        assertEquals(result, 1000L * 60 * 60 * 8 - 5 * 60 * 1000L - 10 * 60 * 1000L)
    }

    @Test
    fun returnTotalWithTimeLogOutIsNull_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 55)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val result = resultDay(cal.time, null, 1000L * 60 * 60 * 8, false, 0, 30,
            45)

        assertEquals(result, 1000L * 60 * 60 * 8)
    }

    @Test
    fun isResultCorrectWithTimeLogOutIsNullAndRounding_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 55)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val result = resultDay(cal.time, null, 1000L * 60 * 60 * 8, true, 0, 30,
            45)

        assertEquals(result, 1000L * 60 * 60 * 8 - 5 * 60 * 1000L)
    }

    @Test
    fun isResultCorrectWithBreakBelow6_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 6)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date1 = cal.time
        cal.set(Calendar.HOUR_OF_DAY, 11)
        cal.set(Calendar.MINUTE, 0)

        val result = resultDay(date1, cal.time, 1000L * 60 * 60 * 5, false, 11, 30,
            45)

        assertEquals(result, 1000L * 60 * 60 * 5 - 11 * 60 * 1000L)
    }

    @Test
    fun isResultCorrectWithBreak6And9_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 6)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date1 = cal.time
        cal.set(Calendar.HOUR_OF_DAY, 14)
        cal.set(Calendar.MINUTE, 0)

        val result = resultDay(date1, cal.time, 1000L * 60 * 60 * 8, false, 11, 22,
            45)

        assertEquals(result, 1000L * 60 * 60 * 8 - 22 * 60 * 1000L)
    }

    @Test
    fun isResultCorrectWithBreakOver9_returnsTrue() {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.set(Calendar.HOUR_OF_DAY, 6)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date1 = cal.time
        cal.set(Calendar.HOUR_OF_DAY, 16)
        cal.set(Calendar.MINUTE, 0)

        val result = resultDay(date1, cal.time, 1000L * 60 * 60 * 10, false, 11, 22,
            45)

        assertEquals(result, 1000L * 60 * 60 * 10 - 45 * 60 * 1000L)
    }
}