package src; /**
* @file fileEloCalculator.cpp
* @author Francesco Nieri
* @version 0.1
* @since 18-12-2022
 */

import java.lang.Math;

public class ELOCalculator{

    private final double[] startingELOs;
    private final double[] transformedScores = new double[2];
    private final double[] expectedScores = new double[2];
    private final double[] finalELOs = new double[2];
    private final boolean[] hasWon;

    private final int NoOfPlayers = 2;

    public ELOCalculator(double[] startingELOs, boolean[] hasWon) throws Exception {
        int ELOLength = startingELOs.length;
        int hasWonLength = hasWon.length;
        // Both arrays must be of size two
        assert ELOLength == 2; assert hasWonLength == 2;
        
        this.startingELOs = startingELOs;
        this.hasWon = hasWon;
    }
    
    private void calculateTransformedScore() {
        double currentTransformedScore;
        for (int player = 0; player < NoOfPlayers; player++) {
            currentTransformedScore = (float) Math.pow(10, ( (int) startingELOs[player] / 400.0));
            transformedScores[player] = currentTransformedScore;         
        }
    }
    
    private void calculateExpectedScore() {
        double expectedDenumerator = transformedScores[0] + transformedScores[1];
        double currentExpectedScore;
        for (int player = 0; player < NoOfPlayers; player++) {
            currentExpectedScore = transformedScores[player] / expectedDenumerator;
            expectedScores[player] = currentExpectedScore;
        }
    } 
    
    private void calculateFinalELOs() {
        double currentFinalELO;
        for (int player = 0; player < NoOfPlayers; player++) {
            int playerHasWon = hasWon[player] ? 1 : 0;
            int KFactor = 32;
            double KminusSpEp = KFactor * ((playerHasWon - expectedScores[player]));
            currentFinalELO = startingELOs[player] + KminusSpEp;
            finalELOs[player] = (int) currentFinalELO;
        }
    }
        
    public double[] calculateELOs() {
        calculateTransformedScore();
        calculateExpectedScore();
        calculateFinalELOs();
        return finalELOs;
    }
} 
