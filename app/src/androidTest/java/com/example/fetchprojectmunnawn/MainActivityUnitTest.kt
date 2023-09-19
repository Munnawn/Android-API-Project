package com.example.fetchprojectmunnawn

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testFilteringBlankNames() {
        // List contains 2 valid entries and 2 invalid entries ("" or null name").
        val userDataList = listOf(
            UserData(1, 1, "Joe"),
            UserData(2, 2, ""),
            UserData(3, 3, null),
            UserData(4, 4, "Annie"),
        )

        // Wait for the activity
        scenario.onActivity { mainActivity ->
            // Call filterBlankNames()
            val filteredData = mainActivity.filterBlankNames(userDataList)

            // Entries with blank or null names should be filtered out.
            assertEquals(2, filteredData.size) // Expecting only "Joe" and "Annie" to remain
            assertEquals("Joe", filteredData[0].name)
            assertEquals("Annie", filteredData[1].name)
        }
    }
}
