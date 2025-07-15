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

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SimpleTaskCreationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testCreateSingleTask() {
        // Wait for app to load
        composeTestRule.waitForIdle()
        
        // Find and click the add button (FAB)
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        // Wait for navigation
        composeTestRule.waitForIdle()
        
        // Fill in title
        composeTestRule.onAllNodesWithText("Title")
            .filterToOne(hasSetTextAction())
            .performTextInput("Test Task 1")
        
        // Fill in description
        composeTestRule.onAllNodesWithText("Description")
            .filterToOne(hasSetTextAction())
            .performTextInput("This is a test task description")

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        // Wait for navigation back
        composeTestRule.waitForIdle()
        
        // Verify task was created
        composeTestRule.onNodeWithText("Test Task 1")
            .assertExists()
    }

    @Test
    fun testCreateMultipleTasksSequentially() {
        val taskTitles = listOf(
            "Morning Meeting",
            "Code Review",
            "Documentation Update",
            "Team Standup",
            "Bug Fix #123",
            "Feature Planning",
            "Database Migration",
            "UI Testing",
            "Performance Review",
            "Client Call"
        )
        
        taskTitles.forEachIndexed { index, title ->
            // Click add button
            composeTestRule.onNodeWithContentDescription("Add Task")
                .performClick()
            
            composeTestRule.waitForIdle()
            
            // Fill title
            composeTestRule.onAllNodesWithText("Title")
                .filterToOne(hasSetTextAction())
                .performTextInput(title)
            
            // Fill description
            composeTestRule.onAllNodesWithText("Description")
                .filterToOne(hasSetTextAction())
                .performTextInput("Description for task ${index + 1}")

            composeTestRule.onNodeWithContentDescription("Save Task")
                .assertExists()
                .assertIsDisplayed()
            // Save the task
            composeTestRule.onNodeWithContentDescription("Save Task")
                .performClick()
            
            composeTestRule.waitForIdle()
            
            // Verify task exists
            composeTestRule.onNodeWithText(title)
                .assertExists()
        }
        
        // Verify all tasks are still visible
        taskTitles.forEach { title ->
            composeTestRule.onNodeWithText(title)
                .assertExists()
        }
    }

    @Test
    fun testNavigationFlow() {
        // Start on tasks screen
        composeTestRule.waitForIdle()
        
        // Click add button
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Verify we're on add/edit screen
        composeTestRule.onNodeWithText("Title")
            .assertExists()
        composeTestRule.onNodeWithText("Description")
            .assertExists()
        
        // Go back without saving
        composeTestRule.onNodeWithContentDescription("Back")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Should be back on tasks screen
        composeTestRule.onNodeWithContentDescription("Add Task")
            .assertExists()
    }

    @Test
    fun testFormValidation() {
        // Navigate to add screen
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        // Should still be on the form (title field exists)
        composeTestRule.onNodeWithText("Title")
            .assertExists()
        
        // Add title and save
        composeTestRule.onAllNodesWithText("Title")
            .filterToOne(hasSetTextAction())
            .performTextInput("Valid Task")

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        composeTestRule.waitForIdle()
        
        // Should navigate back and show task
        composeTestRule.onNodeWithText("Valid Task")
            .assertExists()
    }

    @Test
    fun testCreateTasksWithRandomData() {
        val taskData = generateRandomTaskData(20)
        
        taskData.forEach { (title, description) ->
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
            
            // Verify
            composeTestRule.onNodeWithText(title)
                .assertExists()
        }
    }

    private fun generateRandomTaskData(count: Int): List<Pair<String, String>> {
        val taskTypes = listOf(
            "Meeting", "Call", "Email", "Review", "Planning", 
            "Development", "Testing", "Documentation", "Research", "Training",
            "Bug Fix", "Feature", "Deployment", "Analysis", "Design"
        )
        
        val adjectives = listOf(
            "Important", "Urgent", "Quick", "Detailed", "Complex",
            "Simple", "Critical", "Optional", "Scheduled", "Immediate"
        )
        
        val descriptions = listOf(
            "High priority task requiring immediate attention",
            "Follow up on previous discussion with team",
            "Complete before end of sprint deadline",
            "Regular maintenance and cleanup task",
            "New feature implementation and testing",
            "Bug investigation and resolution",
            "Code review and quality assurance",
            "Documentation update and maintenance",
            "Team collaboration and planning session",
            "Client communication and feedback"
        )
        
        return (1..count).map { index ->
            val title = "${adjectives.random()} ${taskTypes.random()} #$index"
            val description = descriptions.random()
            title to description
        }
    }
}