package glavni.paket.arbeitszeit.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import glavni.paket.arbeitszeit.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PeriodDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DayDatabase
    private lateinit var dao: PeriodDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DayDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getPeriodDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertPeriod() = runBlockingTest {
        val period = Period(null, null, null, null)
        dao.insertPeriod(period)

        val lastPeriod = dao.getLastPeriodLive().getOrAwaitValue()

        assertThat(lastPeriod).isEqualTo(period)
    }

    @Test
    fun deletePeriod() = runBlockingTest {
        val period = Period(null, null, null, null)
        period.id = 1
        dao.insertPeriod(period)
        dao.deletePeriod(period)

        val lastPeriod = dao.getLastPeriod()

        assertThat(lastPeriod).isNotEqualTo(period)
    }

    @Test
    fun updatePeriod() = runBlockingTest {
        val period = Period(null, null, null, 55L)
        period.id = 1
        dao.insertPeriod(period)
        period.workingTime = 10L
        dao.updatePeriod(period)

        val lastPeriod = dao.getLastPeriod()

        assertThat(lastPeriod?.workingTime).isEqualTo(10L)
    }

    @Test
    fun numberPeriodsInTable() = runBlockingTest {
        dao.insertPeriod(Period(null, null, null, 55L))
        dao.insertPeriod(Period(null, null, null, 10L))
        dao.insertPeriod(Period(null, null, null, 15L))

        val numberPeriods = dao.numberPeriodsInTable()

        assertThat(numberPeriods).isEqualTo(3)
    }
}