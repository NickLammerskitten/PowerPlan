package de.powerplan.shareddomain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RepetitionSchemeTest {
    @Test
    fun `Fixed Repetitionscheme contains only fixed reps`() {
        val fixed = RepetitionScheme.Fixed(5)
        assertEquals(5, fixed.getFixedReps())
        assertNull(fixed.getMinReps())
        assertNull(fixed.getMaxReps())
    }

    @Test
    fun `Fixed Repetitionscheme cannot be negative`() {
        assertThrows<IllegalArgumentException> {
            RepetitionScheme.Fixed(-1)
        }
    }

    @Test
    fun `Range Repetitionscheme contains min and max reps`() {
        val range =
            RepetitionScheme.Range(
                min = 5,
                max = 10,
            )
        assertNull(range.getFixedReps())
        assertEquals(5, range.getMinReps())
        assertEquals(10, range.getMaxReps())
    }

    @Test
    fun `Range Repetitionscheme cannot have min greater than max`() {
        assertThrows<IllegalArgumentException> {
            RepetitionScheme.Range(
                min = 10,
                max = 5,
            )
        }
    }

    @Test
    fun `AMRAP Repetitionscheme contains no reps`() {
        val amrap = RepetitionScheme.AMRAP
        assertNull(amrap.getFixedReps())
        assertNull(amrap.getMinReps())
        assertNull(amrap.getMaxReps())
    }
}
