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

    @Test
    fun getActiveAndCompletedStates_oneComplete_returnHundred(){
        val tasks = listOf<Task>(
                Task("title","desc",isCompleted = true)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertThat(result.completedTasksPercent, `is`(100f))
        assertThat(result.activeTasksPercent,`is`(0f))
    }

    @Test
    fun getActiveAndCompletedStates_twoCompleteThreeActive_returnForty(){
        val tasks = listOf<Task>(
                Task("title","desc",isCompleted = true),
        Task("title","desc",isCompleted = true),
        Task("title","desc",isCompleted = false),
        Task("title","desc",isCompleted = false),
        Task("title","desc",isCompleted = false)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent,`is`(60f))
    }

    @Test
    fun getActiveAndCompletedStates_empty_returnZero(){
//        val tasks = listOf<Task>(
//        )

        val result = getActiveAndCompletedStats(emptyList())

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent,`is`(0f))
    }

    @Test
    fun getActiveAndCompletedStates_error_returnZero(){
        //val tasks = null

        //val result = getActiveAndCompletedStats(tasks)
        val result = getActiveAndCompletedStats(null)

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent,`is`(0f))
    }
}