package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

class StatisticsUtilsTest{
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero(){
        val tasks = listOf<Task>(
                Task("title","desc",isCompleted = false)
        )

        val result = getActiveAndCompletedStats(tasks)

//        assertEquals(result.completedTasksPercent, 0f)
//        assertEquals(result.activeTasksPercent,100f)

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent,`is`(100f))
    }
}