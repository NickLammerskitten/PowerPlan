package de.powerplan.shareddomain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GoalSchemeTest {

    @Test
    fun `RPE GoalScheme contains only RPE`() {
        val rpe = GoalScheme.RPE(7.5)
        assertEquals(7.5, rpe.getRpe())
        assertNull(rpe.getMinRpe())
        assertNull(rpe.getMaxRpe())
        assertNull(rpe.getPercent1RM())
    }

    @Test
    fun `RPE GoalScheme cannot have RPE less than 3`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.RPE(2.5)
        }
    }

    @Test
    fun `RPE GoalScheme cannot have RPE greater than 10`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.RPE(10.5)
        }
    }

    @Test
    fun `RPERange GoalScheme contains min and max RPE`() {
        val rpeRange = GoalScheme.RPERange(5.0, 8.0)
        assertNull(rpeRange.getRpe())
        assertEquals(5.0, rpeRange.getMinRpe())
        assertEquals(8.0, rpeRange.getMaxRpe())
        assertNull(rpeRange.getPercent1RM())
    }

    @Test
    fun `RPERange GoalScheme cannot have min RPE less than 3`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.RPERange(2.5, 8.0)
        }
    }

    @Test
    fun `RPERange GoalScheme cannot have max RPE less than 3`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.RPERange(5.0, 2.5)
        }
    }

    @Test
    fun `RPERange GoalScheme cannot have min RPE greater than max RPE`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.RPERange(8.0, 5.0)
        }
    }

    @Test
    fun `PercentOfOneRM GoalScheme contains only percent of 1RM`() {
        val percentOfOneRM = GoalScheme.PercentOfOneRM(75.0)
        assertNull(percentOfOneRM.getRpe())
        assertNull(percentOfOneRM.getMinRpe())
        assertNull(percentOfOneRM.getMaxRpe())
        assertEquals(75.0, percentOfOneRM.getPercent1RM())
    }

    @Test
    fun `PercentOfOneRM GoalScheme cannot have percent less than 1`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.PercentOfOneRM(0.5)
        }
    }

    @Test
    fun `PercentOfOneRM GoalScheme cannot have percent greater than 100`() {
        assertThrows<IllegalArgumentException> {
            GoalScheme.PercentOfOneRM(101.0)
        }
    }
}
