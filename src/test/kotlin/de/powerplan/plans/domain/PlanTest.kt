package de.powerplan.plans.domain

import de.powerplan.shareddomain.DifficultyLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PlanTest {

    @Test
    fun `Can create a plan`() {
        val plan = Plan.initialize(
            name = "Test Plan",
            difficultyLevel = DifficultyLevel.BEGINNER,
            classifications = emptyList(),
            weeks = listOf(
                Week.initialize(
                    index = "-1",
                    trainingDays = emptyList()
                )
            )
        )

        assertNotNull(plan)
        assertEquals("Test Plan", plan.name)
        assertEquals(DifficultyLevel.BEGINNER, plan.difficultyLevel)
        assertEquals(1, plan.weeks.size)
        assertEquals(0, plan.weeks[0].trainingDays.size)
        assertTrue(plan.isTemplate)
    }

    @Test
    fun `Cannot create a plan with blank name`() {
        assertThrows<IllegalArgumentException> {
            Plan.initialize(
                name = "",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = listOf(
                    Week.initialize(
                        index = "-1",
                        trainingDays = emptyList()
                    )
                )
            )
        }
    }

    @Test
    fun `Cannot create a plan with more than 18 weeks`() {
        assertThrows<IllegalArgumentException> {
            Plan.initialize(
                name = "Test Plan",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = List(19) {
                    Week.initialize(
                        index = it.toString(),
                        trainingDays = emptyList()
                    )
                }
            )
        }
    }

    @Test
    fun `Cannot create a plan with less than 1 week`() {
        assertThrows<IllegalArgumentException> {
            Plan.initialize(
                name = "Test Plan",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = emptyList()
            )
        }
    }
}