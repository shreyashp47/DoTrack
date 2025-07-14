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
class QuickTaskTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testQuickTaskCreation() {
        // Wait for app to load
        composeTestRule.waitForIdle()
        
        // Verify FAB exists with correct content description
        composeTestRule.onNodeWithContentDescription("Add Task")
            .assertExists()
            .assertIsDisplayed()
        
        // Click the FAB
        composeTestRule.onNodeWithContentDescription("Add Task")
            .performClick()
        
        // Wait for navigation
        composeTestRule.waitForIdle()
        
        // Verify we're on AddEditTaskScreen
        composeTestRule.onNodeWithText("Title")
            .assertExists()
        
        // Fill in basic task info
        composeTestRule.onAllNodesWithText("Title")
            .filterToOne(hasSetTextAction())
            .performTextInput("Quick Test Task")
        
        composeTestRule.onAllNodesWithText("Description")
            .filterToOne(hasSetTextAction())
            .performTextInput("This is a quick test")

        composeTestRule.onNodeWithContentDescription("Save Task")
            .assertExists()
            .assertIsDisplayed()
        // Save the task
        composeTestRule.onNodeWithContentDescription("Save Task")
            .performClick()
        
        // Wait for navigation back
        composeTestRule.waitForIdle()
        
        // Verify task was created and appears in list
        composeTestRule.onNodeWithText("Quick Test Task")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testCreate5RandomTasks() {
        val tasks = listOf(
            "Morning Standup" to "Daily team meeting",
            "Code Review" to "Review PR #456",
            "Bug Fix" to "Fix login issue",
            "Documentation" to "Update API docs",
            "Testing" to "Run integration tests"
        )
        
        tasks.forEach { (title, description) ->
            // Click FAB
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


            // Save the task
            composeTestRule.onNodeWithContentDescription("Save Task")
                .performClick()
            
            composeTestRule.waitForIdle()
            
            // Verify task exists
            composeTestRule.onNodeWithText(title)
                .assertExists()
        }
        
        // Verify all tasks are still visible
        tasks.forEach { (title, _) ->
            composeTestRule.onNodeWithText(title)
                .assertExists()
        }
    }
}