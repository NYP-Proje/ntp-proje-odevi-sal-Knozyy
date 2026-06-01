package com.okul.util;

import com.okul.model.TranscriptRow;

import java.util.List;

public class GradeCalculator {

    private GradeCalculator() {
    }

    public static String scoreToLetter(double score) {
        if (score >= 90) return "AA";
        if (score >= 80) return "BA";
        if (score >= 70) return "BB";
        if (score >= 65) return "CB";
        if (score >= 60) return "CC";
        if (score >= 55) return "DC";
        if (score >= 50) return "DD";
        return "FF";
    }

    public static double letterToGpa(String letter) {
        switch (letter) {
            case "AA": return 4.0;
            case "BA": return 3.5;
            case "BB": return 3.0;
            case "CB": return 2.5;
            case "CC": return 2.0;
            case "DC": return 1.5;
            case "DD": return 1.0;
            default:   return 0.0;
        }
    }

    public static double weightedGpa(List<TranscriptRow> rows) {
        double totalPoints = 0;
        int totalCredits = 0;
        for (TranscriptRow row : rows) {
            totalPoints += row.getGpaValue() * row.getCredit();
            totalCredits += row.getCredit();
        }
        if (totalCredits == 0) {
            return 0.0;
        }
        double gpa = totalPoints / totalCredits;

        return Math.round(gpa * 100.0) / 100.0;
    }
}
