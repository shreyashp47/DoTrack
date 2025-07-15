package com.shreyash.dotrack.ui.tasks

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shreyash.dotrack.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskPerformanceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testTaskCreationPerformance() {
        composeTestRule.waitForIdle()
        
        val numberOfTasks = 20
        val taskCreationTimes = mutableListOf<Long>()
        
        repeat(numberOfTasks) { index ->
            val taskTitle = "Performance Test Task #${index + 1}"
            
            val creationTime = measureTimeMillis {
                // Click add button
                composeTestRule.onNodeWithContentDescription("Add Task")
                    .performClick()
                
                composeTestRule.waitForIdle()
                
                // Fill form
                composeTestRule.onAllNodesWithText("Title")
                    .filterToOne(hasSetTextAction())
                    .performTextInput(taskTitle)
                
                composeTestRule.onAllNodesWithText("Description")
                    .filterToOne(hasSetTextAction())
                    .performTextInput("Performance test description for task ${index + 1}")

                composeTestRule.onNodeWithContentDescription("Save Task")
                    .assertExists()
                    .assertIsDisplayed()
                // Save the task
                composeTestRule.onNodeWithContentDescription("Save Task")
                    .performClick()
                
                composeTestRule.waitForIdle()
                
                // Verify task was created
                composeTestRule.onNodeWithText(taskTitle)
                    .assertExists()
            }
            
            taskCreationTimes.add(creationTime)
            println("Task ${index + 1} creation time: ${creationTime}ms")
        }
        
        // Calculate performance metrics
        val averageTime = taskCreationTimes.average()
        val maxTime = taskCreationTimes.maxOrNull() ?: 0L
        val minTime = taskCreationTimes.minOrNull() ?: 0L
        
        println("=== Task Creation Performance Results ===")
        println("Total tasks created: $numberOfTasks")
        println("Average creation time: ${averageTime.toInt()}ms")
        println("Maximum creation time: ${maxTime}ms")
        println("Minimum creation time: ${minTime}ms")
        println("Total time: ${taskCreationTimes.sum()}ms")
        
        // Assert reasonable performance (adjust thresholds as needed)
        assert(averageTime < 5000) { "Average task creation time too slow: ${averageTime}ms" }
        assert(maxTime < 10000) { "Maximum task creation time too slow: ${maxTime}ms" }
    }

    @Test
    fun testBulkTaskCreationStressTest() {
        composeTestRule.waitForIdle()
        
        val taskBatches = listOf(
            "Quick Task" to "Simple task description",
            "Meeting Prep" to "Prepare materials for the upcoming team meeting",
            "Code Review" to "Review pull request #123 for the new feature implementation",
            "Bug Investigation" to "Investigate and fix the reported issue in the login system",
            "Documentation" to "Update API documentation with the latest changes",
            "Testing" to "Run comprehensive tests on the new payment integration",
            "Deployment" to "Deploy version 2.1.0 to production environment",
            "Client Call" to "Schedule and conduct client feedback session",
            "Database Cleanup" to "Clean up old records and optimize database performance",
            "Security Audit" to "Perform security audit on the authentication system",
            "Performance Optimization" to "Optimize app performance and reduce loading times",
            "UI/UX Review" to "Review and improve user interface design",
            "Data Migration" to "Migrate legacy data to the new database schema",
            "Backup Setup" to "Set up automated backup system for critical data",
            "Monitoring Setup" to "Configure application monitoring and alerting"
        )
        
        val totalTime = measureTimeMillis {
            taskBatches.forEachIndexed { index, (title, description) ->
                // Click add
                composeTestRule.onNodeWithContentDescription("Add Task")
                    .performClick()
                
                composeTestRule.waitForIdle()
                
                // Fill form
                composeTestRule.onAllNodesWithText("Title")
                    .filterToOne(hasSetTextAction())
                    .performTextInput(title)
                
                composeTestRule.onAllNodesWithText("Description")
                    .filterToOne(hasSetTextAction())
                    .performTextInput(description)

                composeTestRule.onNodeWithContentDescription("Save Task")
                    .assertExists()
                    .assertIsDisplayed()
                // Save the task
                composeTestRule.onNodeWithContentDescription("Save Task")
                    .performClick()
                
                composeTestRule.waitForIdle()
                
                // Quick verification
                composeTestRule.onNodeWithText(title)
                    .assertExists()
                
                println("Created task ${index + 1}/${taskBatches.size}: $title")
            }
        }
        
        println("=== Bulk Task Creation Results ===")
        println("Total tasks: ${taskBatches.size}")
        println("Total time: ${totalTime}ms")
        println("Average per task: ${totalTime / taskBatches.size}ms")
        
        // Verify all tasks exist
        taskBatches.forEach { (title, _) ->
            composeTestRule.onNodeWithText(title)
                .assertExists()
        }
    }
}