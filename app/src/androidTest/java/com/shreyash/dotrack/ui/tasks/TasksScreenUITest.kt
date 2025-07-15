package com.shreyash.dotrack.ui.tasks

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shreyash.dotrack.MainActivity
import com.shreyash.dotrack.domain.model.Priority
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TasksScreenUITest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testAddMultipleRandomTasks() {
        // Wait for the app to load
        composeTestRule.waitForIdle()
        
        // Number of random tasks to create
        val numberOfTasks = 15
        val createdTasks = mutableListOf<TestTask>()
        
        repeat(numberOfTasks) { index ->
            val testTask = generateRandomTask(index + 1)
            createdTasks.add(testTask)
            
            // Click the FloatingActionButton to add task
            composeTestRule.onNode(hasContentDescription("Add Task") or hasText("Add"))
                .assertExists()
                .performClick()
            
            // Wait for navigation to AddEditTaskScreen
            composeTestRule.waitForIdle()
            
            // Verify we're on AddEditTaskScreen by checking for title field
            composeTestRule.onNodeWithText("Title")
                .assertExists()
            
            // Fill in the task details
            fillTaskForm(testTask)

            // Save the task - look for Save button or FAB
            composeTestRule.onNode(hasText("Save") or hasContentDescription("Save"))
                .assertExists()
                .performClick()

            // Wait for navigation back to TasksScreen
            composeTestRule.waitForIdle()
            
            // Verify the task appears in the list
            composeTestRule.onNodeWithText(testTask.title)
                .assertExists()
                
            // Small delay between tasks to avoid overwhelming the UI
            Thread.sleep(100)
        }
        
        // Verify all tasks are displayed
        createdTasks.forEach { task ->
            composeTestRule.onNodeWithText(task.title)
                .assertExists()
        }
    }

    @Test
    fun testAddTaskWithAllPriorities() {
        val priorities = listOf(Priority.HIGH, Priority.MEDIUM, Priority.LOW)
        
        priorities.forEachIndexed { index, priority ->
            val testTask = TestTask(
                title = "Task with ${priority.name} Priority",
                description = "This task has ${priority.name.lowercase()} priority",
                priority = priority,
                dueDate = LocalDateTime.now().plusDays(index + 1L)
            )
            
            // Click add task button
            composeTestRule.onNodeWithContentDescription("Add Task")
                .performClick()
            
            composeTestRule.waitForIdle()
            
            // Fill form
            fillTaskForm(testTask)

            composeTestRule.onNodeWithContentDescription("Save Task")
                .assertExists()
                .assertIsDisplayed()
            // Save the task
            composeTestRule.onNodeWithContentDescription("Save Task")
                .performClick()
            
            composeTestRule.waitForIdle()
            
            // Verify task is created
            composeTestRule.onNodeWithText(testTask.title)
                .assertExists()
        }
    }

    @Test
    fun testAddTaskWithReminder() {
        val testTask = TestTask(
            title = "Task with Reminder",
            description = "This task has a reminder enabled",
            priority = Priority.HIGH,
            dueDate = LocalDateTime.now().plusHours(2),
            reminderEnabled = true
        )
        
        // Click add task button
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Fill form with reminder
        fillTaskForm(testTask)

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Verify task is created
        composeTestRule.onNodeWithText(testTask.title)
            .assertExists()
    }

    @Test
    fun testNavigationFlow() {
        // Start at TasksScreen
        composeTestRule.onNodeWithContentDescription("Add Task")
            .assertExists()
        
        // Navigate to AddEditTaskScreen
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Verify we're on AddEditTaskScreen
        composeTestRule.onNodeWithText("Title")
            .assertExists()
        composeTestRule.onNodeWithText("Description")
            .assertExists()
        
        // Navigate back without saving
        composeTestRule.onNodeWithContentDescription("Back")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Verify we're back on TasksScreen
        composeTestRule.onNodeWithContentDescription("Add Task")
            .assertExists()
    }

    @Test
    fun testFormValidation() {
        // Navigate to AddEditTaskScreen
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        composeTestRule.waitForIdle()


        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        // Should show error or stay on screen
        composeTestRule.onNodeWithText("Title")
            .assertExists()
        
        // Add title and try again
        composeTestRule.onNodeWithText("Title")
            .performTextInput("Valid Task Title")


        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Should navigate back and show the task
        composeTestRule.onNodeWithText("Valid Task Title")
            .assertExists()
    }

    private fun fillTaskForm(task: TestTask) {
        // Fill title - find the TextField with "Title" label
        composeTestRule.onNode(
            hasSetTextAction() and hasAnyAncestor(hasText("Title"))
        ).performTextInput(task.title)
        
        // Fill description - find the TextField with "Description" label
        composeTestRule.onNode(
            hasSetTextAction() and hasAnyAncestor(hasText("Description"))
        ).performTextInput(task.description)
        
        // Set priority
        setPriority(task.priority)
        
        // Set due date if provided
        task.dueDate?.let { dueDate ->
            setDueDate(dueDate)
        }
        
        // Enable reminder if needed
        if (task.reminderEnabled) {
            enableReminder()
        }
    }

    private fun setPriority(priority: Priority) {
        // Click on priority selector (adjust based on your UI)
        composeTestRule.onNodeWithText("Priority")
            .performClick()
        
        // Select the priority
        composeTestRule.onNodeWithText(priority.name)
            .performClick()
    }

    private fun setDueDate(dueDate: LocalDateTime) {
        // Click on date picker (adjust based on your UI)
        composeTestRule.onNodeWithText("Due Date")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // For simplicity, we'll just confirm the date picker
        // In a real test, you'd interact with the date picker
        composeTestRule.onNodeWithText("OK")
            .performClick()
    }

    private fun enableReminder() {
        // Toggle reminder switch (adjust based on your UI)
        composeTestRule.onNodeWithText("Reminder")
            .performClick()
    }

    private fun generateRandomTask(index: Int): TestTask {
        val priorities = listOf(Priority.HIGH, Priority.MEDIUM, Priority.LOW)
        val taskTypes = listOf(
            "Meeting", "Call", "Email", "Review", "Planning", 
            "Development", "Testing", "Documentation", "Research", "Training"
        )
        val descriptions = listOf(
            "Important task that needs attention",
            "Follow up on previous discussion",
            "Complete before deadline",
            "High priority item",
            "Regular maintenance task",
            "New feature implementation",
            "Bug fix required",
            "Code review needed",
            "Documentation update",
            "Team collaboration"
        )
        
        return TestTask(
            title = "${taskTypes.random()} Task #$index",
            description = descriptions.random(),
            priority = priorities.random(),
            dueDate = if (Random.nextBoolean()) {
                LocalDateTime.now().plusDays(Random.nextLong(1, 30))
            } else null,
            reminderEnabled = Random.nextBoolean()
        )
    }

    data class TestTask(
        val title: String,
        val description: String,
        val priority: Priority,
        val dueDate: LocalDateTime? = null,
        val reminderEnabled: Boolean = false
    )
}