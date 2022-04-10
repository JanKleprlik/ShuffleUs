package com.shuffleus.app.repository.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.data.User
import com.shuffleus.app.utils.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Lecture::class,User::class, GroupNames::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupNamesDao() : GroupNamesDao
    abstract fun lectureDao() : LectureDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "Sample.db")
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // insert the data on the IO Thread
                        CoroutineScope(SupervisorJob()).launch {
                            getInstance(context).groupNamesDao().insertAll(*GROUP_NAMES_DATA.toTypedArray())
                        }
                    }
                })
                .build()

        val GROUP_NAMES_DATA = listOf(
            GroupNames(name="Greek", names= listOf(
                "Alpha",
                "Beta",
                "Gamma",
                "Delta",
                "Epsilon",
                "Zeta",
                "Eta",
                "Theta",
            )),
            GroupNames(name="Food", names= listOf(
                "Cake",
                "Orange",
                "Tiramisu",
                "Pizza",
                "Lasagne",
                "Tortilla",
                "Potato",
                "Steak",
            )),
            GroupNames(name="Drinks", names= listOf(
                "Whiskey",
                "Rum",
                "Margarita",
                "Vodka",
                "Tequila",
                "Beer",
                "Wine",
                "Liqueur",
            ))
        )
    }
}