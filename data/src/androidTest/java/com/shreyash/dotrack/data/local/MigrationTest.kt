package com.shreyash.dotrack.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val testDbName = "migration-test-db"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TaskDatabase::class.java
    )

    @Test
    @Throws(IOException::class)
    fun migrate3To4_keepsExistingDataAndCreatesIndexes() {
        helper.createDatabase(testDbName, 3).apply {
            execSQL(
                """
                INSERT INTO tasks
                    (id, title, description, isCompleted, dueDate, priority, reminderEnabled, categoryId, createdAt, updatedAt)
                VALUES
                    ('task-1', 'Existing task', 'Keep me', 0, '2026-08-01T10:00:00', 2, 1, NULL, '2026-07-01T09:00:00', '2026-07-01T09:00:00')
                """.trimIndent()
            )
            execSQL("INSERT INTO categories (id, name, color) VALUES ('cat-1', 'Work', 4278190080)")
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 4, true, TaskDatabase.MIGRATION_3_4)

        db.query("SELECT title, isCompleted, priority FROM tasks WHERE id = 'task-1'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Existing task", cursor.getString(cursor.getColumnIndexOrThrow("title")))
            assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")))
        }

        db.query("SELECT name FROM categories WHERE id = 'cat-1'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Work", cursor.getString(cursor.getColumnIndexOrThrow("name")))
        }

        db.query("SELECT name FROM sqlite_master WHERE type = 'index' AND tbl_name = 'tasks'").use { cursor ->
            val indexNames = mutableSetOf<String>()
            while (cursor.moveToNext()) {
                indexNames.add(cursor.getString(0))
            }
            assertTrue(indexNames.contains("index_tasks_isCompleted"))
            assertTrue(indexNames.contains("index_tasks_dueDate"))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To4_upgradesFreshDatabase() {
        helper.createDatabase(testDbName, 1).apply {
            execSQL(
                """
                INSERT INTO tasks
                    (id, title, description, isCompleted, dueDate, priority, reminderEnabled, createdAt, updatedAt)
                VALUES
                    ('task-1', 'Old task', 'Migrated', 0, NULL, 1, 0, '2026-07-01T09:00:00', '2026-07-01T09:00:00')
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(
            testDbName,
            4,
            true,
            TaskDatabase.MIGRATION_1_2,
            TaskDatabase.MIGRATION_2_3,
            TaskDatabase.MIGRATION_3_4
        )

        db.query("SELECT title, categoryId FROM tasks WHERE id = 'task-1'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Old task", cursor.getString(cursor.getColumnIndexOrThrow("title")))
            assertTrue(cursor.isNull(cursor.getColumnIndexOrThrow("categoryId")))
        }
    }
}
