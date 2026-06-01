package com.okul.util;

import com.okul.model.TranscriptRow;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeCalculatorTest {

    @Test
    void scoreToLetter_dogru_harf_dondurur() {
        assertEquals("AA", GradeCalculator.scoreToLetter(95));
        assertEquals("BA", GradeCalculator.scoreToLetter(85));
        assertEquals("BB", GradeCalculator.scoreToLetter(72));
        assertEquals("CC", GradeCalculator.scoreToLetter(60));
        assertEquals("FF", GradeCalculator.scoreToLetter(30));
    }

    @Test
    void letterToGpa_dogru_katsayi_dondurur() {
        assertEquals(4.0, GradeCalculator.letterToGpa("AA"));
        assertEquals(2.0, GradeCalculator.letterToGpa("CC"));
        assertEquals(0.0, GradeCalculator.letterToGpa("FF"));
    }

    @Test
    void weightedGpa_kredi_agirlikli_ortalama_hesaplar() {

        TranscriptRow r1 = new TranscriptRow("BIL101", "Programlama", 3, 95, "AA", 4.0);
        TranscriptRow r2 = new TranscriptRow("MAT101", "Matematik", 4, 75, "BB", 3.0);
        double gpa = GradeCalculator.weightedGpa(Arrays.asList(r1, r2));
        assertEquals(3.43, gpa, 0.001);
    }

    @Test
    void weightedGpa_bos_listede_sifir_dondurur() {
        assertEquals(0.0, GradeCalculator.weightedGpa(List.of()));
    }
}
