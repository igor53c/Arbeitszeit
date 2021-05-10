package glavni.paket.arbeitszeit.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import glavni.paket.arbeitszeit.db.DayDatabase
import glavni.paket.arbeitszeit.other.Constants.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, DayDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providePeriodDao(db: DayDatabase) = db.getPeriodDao()

    @Singleton
    @Provides
    fun provideDayDao(db: DayDatabase) = db.getDayDao()

    @Singleton
    @Provides
    fun provideWeekDao(db: DayDatabase) = db.getWeekDao()
}