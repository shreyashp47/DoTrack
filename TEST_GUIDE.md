# 🧪 UI Testing Guide for DoTrack

This guide explains how to run the comprehensive UI tests for task creation functionality.

## 📋 Test Files Overview

### 1. **QuickTaskTest.kt** - Basic functionality ⚡
- Single task creation test
- 5 random tasks creation
- Simple and fast execution

### 2. **SimpleTaskCreationTest.kt** - Core features 🎯
- Single task creation
- 10 sequential tasks
- Navigation flow testing
- Form validation
- 20 random tasks with generated data

### 3. **TasksScreenUITest.kt** - Advanced scenarios 🚀
- 15 random tasks with priorities
- All priority levels testing
- Reminder functionality
- Due date handling

### 4. **TaskPerformanceTest.kt** - Performance testing 📊
- Task creation speed measurement
- Stress testing with 15+ tasks
- Performance metrics logging

## 🚀 How to Run Tests

### Run All Tests
```bash
./gradlew connectedAndroidTest
```

### Run Specific Test Class
```bash
# Quick basic tests (recommended to start with)
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.QuickTaskTest

# Simple task creation tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.SimpleTaskCreationTest

# Advanced UI tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.TasksScreenUITest

# Performance tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.TaskPerformanceTest
```

### Run Specific Test Method
```bash
# Test creating 20 random tasks
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.SimpleTaskCreationTest -Pandroid.testInstrumentationRunnerArguments.method=testCreateTasksWithRandomData

# Test performance
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.TaskPerformanceTest -Pandroid.testInstrumentationRunnerArguments.method=testTaskCreationPerformance
```

## 📱 Test Scenarios Covered

### ✅ **Navigation Testing**
- TasksScreen → AddEditTaskScreen → TasksScreen
- Back button functionality
- FAB click handling

### ✅ **Form Testing**
- Title input (required field)
- Description input
- Priority selection
- Due date picker
- Reminder toggle

### ✅ **Data Persistence**
- Task creation verification
- Task list updates
- Database integration

### ✅ **Performance Testing**
- Task creation speed
- UI responsiveness
- Memory usage patterns

### ✅ **Stress Testing**
- Multiple task creation (10-20 tasks)
- Sequential operations
- UI stability under load

## 🎯 Test Data

The tests use realistic data including:

### Task Types
- Meeting, Call, Email, Review, Planning
- Development, Testing, Documentation
- Research, Training, Bug Fix, Feature

### Priorities
- HIGH, MEDIUM, LOW (all tested)

### Descriptions
- "High priority task requiring immediate attention"
- "Follow up on previous discussion with team"
- "Complete before end of sprint deadline"
- And more realistic scenarios...

## 📊 Expected Results

### Performance Benchmarks
- **Average task creation**: < 5 seconds
- **Maximum task creation**: < 10 seconds
- **20 tasks creation**: < 2 minutes

### Success Criteria
- ✅ All tasks appear in the list
- ✅ Navigation works smoothly
- ✅ Form validation functions
- ✅ Data persists correctly
- ✅ No crashes or ANRs

## 🔧 Troubleshooting

### Common Issues

1. **Test fails to find FAB**
   - Ensure the FloatingActionButton has `contentDescription = "Add Task"`
   - Check if the UI is fully loaded before test starts

2. **Text input fails**
   - Verify TextField components have proper labels
   - Check if keyboard is interfering with UI

3. **Navigation issues**
   - Ensure proper navigation setup in NavHost
   - Check if back button handling is implemented

4. **Performance issues**
   - Run tests on a physical device for accurate results
   - Ensure device has sufficient resources

### Debug Tips

1. **Add delays for debugging**
   ```kotlin
   Thread.sleep(2000) // Add 2-second delay to observe UI
   ```

2. **Print UI tree**
   ```kotlin
   composeTestRule.onRoot().printToLog("UI_TREE")
   ```

3. **Check specific elements**
   ```kotlin
   composeTestRule.onNodeWithText("Your Text")
       .assertExists()
       .assertIsDisplayed()
   ```

## 🎉 Running Your First Test

**Recommended starting point:**

```bash
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.QuickTaskTest -Pandroid.testInstrumentationRunnerArguments.method=testQuickTaskCreation
```

This will:
1. ✅ Open your app
2. ✅ Click the "Add Task" FAB
3. ✅ Fill in title and description
4. ✅ Save the task
5. ✅ Verify it appears in the list

**Total time: ~30 seconds**

---

## 📝 Notes

- Tests use **Hilt** for dependency injection
- **Clean Architecture** is fully supported
- Tests run on **real database** (not mocked)
- **Widget updates** are triggered during tests
- All tests are **deterministic** and **repeatable**

Happy Testing! 🚀