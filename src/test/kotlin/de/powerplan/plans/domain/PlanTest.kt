package de.powerplan.plans.domain

import de.powerplan.shareddomain.DifficultyLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertFalse

class PlanTest {

    @Test
    fun `Can create a plan`() {
        val plan = createMinimalPlan()

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
            Plan.create(
                id = UUID.randomUUID(),
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
            Plan.create(
                id = UUID.randomUUID(),
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
            Plan.create(
                id = UUID.randomUUID(),
                name = "Test Plan",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = emptyList()
            )
        }
    }

    @Test
    fun `A template plan must not have a status`() {
        assertThrows<IllegalArgumentException> {
            Plan.create(
                id = UUID.randomUUID(),
                name = "Test Plan",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = listOf(
                    Week.initialize(
                        index = "-1",
                        trainingDays = emptyList()
                    )
                ),
                isTemplate = true,
                planStatus = PlanStatus.ACTIVE
            )
        }
    }

    @Test
    fun `A non-template plan must have a status`() {
        assertThrows<IllegalArgumentException> {
            Plan.create(
                id = UUID.randomUUID(),
                name = "Test Plan",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = listOf(
                    Week.initialize(
                        index = "-1",
                        trainingDays = emptyList()
                    )
                ),
                isTemplate = false,
                planStatus = null
            )
        }
    }

    @Test
    fun `can start a new plan`() {
        val plan = createMinimalPlan()
        val newPlan = plan.startNew()

        assertNotNull(newPlan)
        assertEquals("Test Plan", newPlan.name)
        assertEquals(DifficultyLevel.BEGINNER, newPlan.difficultyLevel)
        assertEquals(1, newPlan.weeks.size)
        assertEquals(0, newPlan.weeks[0].trainingDays.size)
        assertFalse(newPlan.isTemplate)
        assertEquals(PlanStatus.ACTIVE, newPlan.planStatus)
    }

    @Test
    fun `can finish a plan`() {
        val plan = createMinimalPlan()
        val newPlan = plan.startNew()

        newPlan.finish()

        assertEquals(PlanStatus.FINISHED, newPlan.planStatus)
    }

    companion object {
        fun createMinimalPlan(): Plan {
            return Plan.create(
                id = UUID.randomUUID(),
                name = "Test Plan",
                difficultyLevel = DifficultyLevel.BEGINNER,
                classifications = emptyList(),
                weeks = listOf(
                    Week.initialize(
                        index = "-1",
                        trainingDays = emptyList()
                    )
                ),
                isTemplate = true,
                planStatus = null
            )
        }
    }
}