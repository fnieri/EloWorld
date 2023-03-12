package src; /**
* @file fileEloCalculator.cpp
* @author Francesco Nieri
* @version 0.1
* @since 18-12-2022
 */

import java.lang.Math;

public class ELOCalculator{

    private final float[] startingELOs;
    private final float[] transformedScores = new float[2];
    private final float[] expectedScores = new float[2];
    private final int[] finalELOs = new int[2];
    private final boolean[] hasWon;

    private final int KFactor = 32;
    private final int NoOfPlayers = 2;

    public ELOCalculator(float[] startingELOs, boolean[] hasWon) throws Exception {
        int ELOLength = startingELOs.length;
        int hasWonLength = hasWon.length;
        // Both arrays must be of size two
        assert ELOLength == 2; assert hasWonLength == 2;
        
        this.startingELOs = startingELOs;
        this.hasWon = hasWon;
    }
    
    private void calculateTransformedScore() {
        float currentTransformedScore;
        for (int player = 0; player < NoOfPlayers; player++) {
            currentTransformedScore = (float) Math.pow(10, ( (int) startingELOs[player] / 400.0));
            transformedScores[player] = currentTransformedScore;         
        }
    }
    
    private void calculateExpectedScore() {
        float expectedDenumerator = transformedScores[0] + transformedScores[1];
        float currentExpectedScore;
        for (int player = 0; player < NoOfPlayers; player++) {
            currentExpectedScore = transformedScores[player] / expectedDenumerator;
            expectedScores[player] = currentExpectedScore;
        }
    } 
    
    private void calculateFinalELOs() {
        float currentFinalELO;
        for (int player = 0; player < NoOfPlayers; player++) {
            int playerHasWon = hasWon[player] ? 1 : 0;
            float KminusSpEp = KFactor * ((playerHasWon - expectedScores[player]));
            currentFinalELO = startingELOs[player] + KminusSpEp;
            finalELOs[player] = (int) currentFinalELO;
        }
    }
        
    public int[] calculateELOs() {
        calculateTransformedScore();
        calculateExpectedScore();
        calculateFinalELOs();
        return finalELOs;
    }
} 
