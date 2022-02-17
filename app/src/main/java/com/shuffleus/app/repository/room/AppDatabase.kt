package com.shuffleus.app.repository.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.data.User
import com.shuffleus.app.utils.Converters
import com.shuffleus.app.utils.ioThread

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
                        ioThread {
                            getInstance(context).userDao().insertAll(*USER_DATA.toTypedArray())
                            getInstance(context).lectureDao().insertAll(*LECTURE_DATA.toTypedArray())
                            getInstance(context).groupNamesDao().insertAll(*GROUP_NAMES_DATA.toTypedArray())
                        }
                    }
                })
                .allowMainThreadQueries()
                .build()

        val LECTURE_DATA = listOf(
            Lecture(0,720,810,"s1","l1",true,"S4","NSWI150")
        )
        val USER_DATA = listOf(

            User(name="Me", surname = "Myself & I", isActive = true ),
        /*
            User(name="Jan", surname = "Kleprlík", isActive = true ),
            User(name="Michal", surname = "Fuleky", isActive = true),
            User(name="Marek", surname = "Majer", isActive = true),
            User(name="Michal", surname = "Ivicic", isActive = false),
            User(name="Ondřej", surname = "Müller", isActive = false),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Pavel", surname = "Zajíc", isActive = false),
            User(name="Jan", surname = "Novák", isActive = false),
            User(name="Martin", surname = "Mareš", isActive = true),
            User(name="Pavel", surname = "Parizek", isActive = true),
        */
        )
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