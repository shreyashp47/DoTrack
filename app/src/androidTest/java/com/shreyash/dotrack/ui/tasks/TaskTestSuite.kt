package com.shreyash.dotrack.ui.tasks

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite for all task-related UI tests
 * 
 * Run this to execute all task tests:
 * ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.TaskTestSuite
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    SimpleTaskCreationTest::class,
    TasksScreenUITest::class,
    TaskPerformanceTest::class
)
class TaskTestSuite