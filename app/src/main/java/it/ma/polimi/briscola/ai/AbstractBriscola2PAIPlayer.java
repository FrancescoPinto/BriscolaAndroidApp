package it.ma.polimi.briscola.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Abstract class implementing common methods that a concrete AI could need
 */
public abstract class AbstractBriscola2PAIPlayer implements Briscola2PAIPlayer {
    /**
     * The My hand.
     */
    Briscola2PHand myHand;
    /**
     * The Config.
     */
    Briscola2PFullMatchConfig config;
    /**
     * The Suit probabilities.
     */
    float suitProbabilities[] = new float[] {0f,0f,0f,0f};
    /**
     * The Batons index.
     */
    int batonsIndex = 0, /**
     * The Cups index.
     */
    cupsIndex = 1, /**
     * The Golds index.
     */
    goldsIndex = 2, /**
     * The Swords index.
     */
    swordsIndex = 3;
    /**
     * The Cards per suit.
     */
    final int cardsPerSuit = 10;

    /**
     * Find card position in hand.
     *
     * @param c the card
     * @return the int representing the card position
     */
    public int findCardPositionInHand(NeapolitanCard c) {
        for (int i = 0; i < myHand.size(); i++)
            if (myHand.getCard(i).equalTo(c))
                return i;

        throw new IllegalArgumentException("The argument is not contained in hand");
    }

    /**
     * Compute suit probabilities (probability that the suit is in the adversary hand or in deck)
     *
     * @param playerIndex the player index
     */
    public void computeSuitProbabilities(int playerIndex){
        List<NeapolitanCard> cardsInPiles = new ArrayList(config.getPile(Briscola2PFullMatchConfig.PLAYER0).getCardList());
        cardsInPiles.addAll(new ArrayList<NeapolitanCard>(config.getPile(Briscola2PFullMatchConfig.PLAYER1).getCardList()));
        cardsInPiles.addAll(new ArrayList<NeapolitanCard>(config.getHand(playerIndex).getCardList()));

        int batons = 0;
        int cups = 0;
        int golds = 0;
        int swords = 0;

        for(NeapolitanCard c: cardsInPiles){
            switch (c.getCardSuitEnum()){
                case BATONS: batons++; break;
                case CUPS: cups++; break;
                case GOLDS: golds++; break;
                case SWORDS: swords++;break;
            }
        }

        suitProbabilities[batonsIndex] = (cardsPerSuit-batons)/cardsPerSuit;
        suitProbabilities[cupsIndex] = (cardsPerSuit-cups)/cardsPerSuit;
        suitProbabilities[goldsIndex] = (cardsPerSuit-golds)/cardsPerSuit;
        suitProbabilities[swordsIndex] = (cardsPerSuit-swords)/cardsPerSuit;

    }

    /**
     * Get the minimum probability among the probabilities that adversary (or in deck) have card of suit that is also in my hand
     *
     * @return the neapolitan card suit i have in hand and corresponding to the minimum probability
     */
    public NeapolitanCardSuit getMinAdversaryProbabilitySuitInMyHand(){
        Map<NeapolitanCardSuit, Float> probabilitiesInHand = new HashMap<>();
        for(NeapolitanCardSuit suit: getCardSuitInMyHand()){
            float probability;
            switch(suit){
                case BATONS: probability = suitProbabilities[batonsIndex]; break;
                case CUPS: probability = suitProbabilities[cupsIndex]; break;
                case GOLDS: probability = suitProbabilities[goldsIndex]; break;
                case SWORDS: probability = suitProbabilities[swordsIndex]; break;
                default: probability = 0;
            }
            probabilitiesInHand.put(suit,probability);
        }
        float min = Collections.min(probabilitiesInHand.values());
        for(NeapolitanCardSuit suit:probabilitiesInHand.keySet()){
            if(probabilitiesInHand.get(suit) == min)
                return suit;
        }

        return null;
    }

    /**
     * Get the probability that adversary (or deck) has a trump card.
     *
     * @param trumpSuit the trump suit
     * @return the probability
     */
    public float getAdversaryProbabilityTrump(NeapolitanCardSuit trumpSuit){
        float probability;
        switch(trumpSuit){
            case BATONS: probability = suitProbabilities[batonsIndex]; break;
            case CUPS: probability = suitProbabilities[cupsIndex]; break;
            case GOLDS: probability = suitProbabilities[goldsIndex]; break;
            case SWORDS: probability = suitProbabilities[swordsIndex]; break;
            default: probability = 0;
        }
        return probability;
    }

    /**
     * Get the set of card suits i have in my hand.
     *
     * @return the set of card suits
     */
    public Set<NeapolitanCardSuit> getCardSuitInMyHand(){
        Set<NeapolitanCardSuit> cardSuitsInHand = new HashSet<>();
        for(NeapolitanCard c: myHand.getCardList()){
            cardSuitsInHand.add(c.getCardSuitEnum());
        }
        return cardSuitsInHand;
    }

    /**
     * Gets best point valued card in list passed as argument
     *
     * @param cards the list of cards
     * @return the best point valued card in list
     */
//prende carta a miglior punteggio, a parità di punteggio prende quella con peggior rank
    public NeapolitanCard getBestPointValuedCard(List<NeapolitanCard> cards) { //a pari pointvalue butto quella con rank peggiore (più grande)

        if (cards.isEmpty())
            return null;
        else {

            NeapolitanCard bestCard = cards.get(0);
            for (NeapolitanCard c : cards)
                if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) > BriscolaCardPointsAndRankingRules.getPointValue(bestCard.getCardNumber()))
                    bestCard = c;
                else if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(bestCard.getCardNumber()) && //a parità di value torna rank peggiore
                        BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(bestCard.getCardNumber()))
                    bestCard = c;

            return bestCard;
        }
    }

    /**
     * Gets best ranked card in list
     *
     * @param cards the cards list
     * @return the best ranked card in list
     */
//prende carta con miglior rank, a parità di rank le carte sono equivalenti
    public NeapolitanCard getBestRankedCard(List<NeapolitanCard> cards) {
        if (cards.isEmpty())
            return null;
        else {
            NeapolitanCard bestCard = cards.get(0);
            for (NeapolitanCard c : cards)
                if (BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(bestCard.getCardNumber()))
                    bestCard = c;

            return bestCard;
        }
    }


    /**
     * Gets worst ranked card in list
     *
     * @param cards the cards list
     * @return the worst ranked card in list
     */
//prende carta con peggior rank
    public NeapolitanCard getWorstRankedCard(List<NeapolitanCard> cards) {
        if (cards.isEmpty())
            return null;
        else {
            NeapolitanCard worstCard = cards.get(0);
            for (NeapolitanCard c : cards)
                if (BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) >= BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()))
                    worstCard = c;

            return worstCard;
        }
    }

    /**
     * Get zero valued worst ranked card in hand not of suit passed as an argument.
     *
     * @param suit the suit
     * @return the zero valued worst ranked card in hand not of suit
     */
    public NeapolitanCard getZeroValuedWorstRankedCardNotOfSuit(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = getCardsNotOfSuitInHand(suit);
        if(cards.isEmpty())
            return null;

        List<NeapolitanCard> zeroValuedCandidates = new ArrayList<>();
        for(NeapolitanCard c: cards){
            if(BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) == 0)
                zeroValuedCandidates.add(c);
        }

        if (zeroValuedCandidates.isEmpty())
            return null;

        return getWorstRankedCard(zeroValuedCandidates);

    }

    /**
     * Get card  in hand with minimum rank same seed passed as argument to win against card passed as argument, with threshold that limits the max value of the card.
     *
     * @param card         the card
     * @param suit         the suit
     * @param maxThreshold the max threshold
     * @return the result
     */
    public NeapolitanCard getMinimumRankSameSeedToWinWithThreshold(NeapolitanCard card, NeapolitanCardSuit suit, int maxThreshold){
        List<NeapolitanCard> cards = getCardsOfSuitInHand(suit);
        if(cards.isEmpty())
            return null;

        List<NeapolitanCard> satisfyingThreshold = getCardsSatisfyingPointThreshold(cards,maxThreshold);

        if(satisfyingThreshold.isEmpty())
            return null;

        return getWorstRankedButBetterRankedThan(satisfyingThreshold, card);

    }

    /**
     * Get card  in hand with minimum point value not trump with threshold that limits the max value of the card.
     *
     * @param trumpSuit    the trump suit
     * @param maxThreshold the max threshold
     * @return the result
     */
    public NeapolitanCard getMinimumPointValueNotTrumpWithThreshold(NeapolitanCardSuit trumpSuit, int maxThreshold){
        List<NeapolitanCard> cards = getCardsNotOfSuitInHand(trumpSuit);
        if(cards.isEmpty())
            return null;

        List<NeapolitanCard> satisfyingThreshold = getCardsSatisfyingPointThreshold(cards,maxThreshold);

        if(satisfyingThreshold.isEmpty())
            return null;

        return getWorstPointValuedCard(satisfyingThreshold);
    }

    /**
     * Get cards satisfying point value max threshold as list.
     *
     * @param cards        the cards
     * @param maxThreshold the max threshold
     * @return the list
     */
    public List<NeapolitanCard> getCardsSatisfyingPointThreshold(List<NeapolitanCard> cards, int maxThreshold){
        List<NeapolitanCard> satisfyingThreshold = new ArrayList<>();
        for(NeapolitanCard c: cards){
            if(BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) <= maxThreshold)
                satisfyingThreshold.add(c);
        }
        return satisfyingThreshold;
    }

    /**
     * Get worst point valued card witihn the list of neapolitan cards passed by argument.
     *
     * @param cards the cards
     * @return the result
     */
    public NeapolitanCard getWorstPointValuedCard(List<NeapolitanCard> cards){
        NeapolitanCard worstCard = cards.get(0);
        for(NeapolitanCard c:cards)
            if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) < BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()))
                worstCard = c;
            else if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) && //a parità di value torna rank peggiore
                    BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()))
                worstCard = c;

        return worstCard;
    }

    /**
     * Get worst ranked card in list but better ranked than card passed by argument.
     *
     * @param list the list
     * @param card the card
     * @return the neapolitan card
     */
    public NeapolitanCard getWorstRankedButBetterRankedThan(List<NeapolitanCard> list, NeapolitanCard card){
        List<NeapolitanCard> betterRanked = new ArrayList<>();
        for(NeapolitanCard c: list){
            if(BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) == BriscolaCardPointsAndRankingRules.getRank(card.getCardNumber()))
                betterRanked.add(c);
        }

        if(betterRanked.isEmpty())
            return null;

        return getWorstRankedCard(betterRanked);
    }

    /**
     * Get worst ranked card in list but better ranked than card passed by argument with same suit.
     *
     * @param list the list
     * @param card the card
     * @return the worst ranked but better ranked than with same suit
     */
    public NeapolitanCard getWorstRankedButBetterRankedThanWithSameSuit(List<NeapolitanCard> list, NeapolitanCard card) {

        List<NeapolitanCard> sameSuit = new ArrayList<>();
        for(NeapolitanCard c: list){
            if(c.getCardSuitEnum() == card.getCardSuitEnum())
                sameSuit.add(c);
        }

        if(sameSuit.isEmpty())
            return null;

        return getWorstRankedButBetterRankedThan(sameSuit,card);
    }

    /**
     * Get best ranked card in hand of suit passed by argument
     *
     * @param suit the suit
     * @return the result
     */
    public NeapolitanCard getBestRankedCardOfSuit(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = getCardsOfSuitInHand(suit);
        return getBestRankedCard(cards);
    }

    /**
     * Get best point valued card in hand of suit passed by argument
     *
     * @param suit the suit
     * @return the result
     */
    public NeapolitanCard getBestPointValuedCardOfSuit(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = getCardsOfSuitInHand(suit);
        return getBestPointValuedCard(cards);

    }


    /**
     * Get list of cards in hand of suit passed by argument.
     *
     * @param suit the suit
     * @return the list
     */
    public List<NeapolitanCard> getCardsOfSuitInHand(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = new ArrayList<>();
        for (NeapolitanCard c : myHand.getCardList()) {
            if (c.getCardSuit().equals(suit.getSuit()))
            cards.add(c);
        }
        return cards;
    }

    /**
     * Get list of cards in hand not of suit passed by argument.
     *
     * @param suit the suit
     * @return the list
     */
    public List<NeapolitanCard> getCardsNotOfSuitInHand(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = new ArrayList<>();
        for (NeapolitanCard c : myHand.getCardList()) {
            if (!c.getCardSuit().equals(suit.getSuit()))
                cards.add(c);
        }
        return cards;
    }

    /**
     * Gets best ranked card of suit in hand better ranked than card passed by argument, same suit passed by argument
     *
     * @param enemyCard the enemy card
     * @param suit      the suit
     * @return the result
     */
    public NeapolitanCard getBestRankedCardOfSuitInHandBetterRankedThan(NeapolitanCard enemyCard, NeapolitanCardSuit suit) { //prende la migliore briscola dalla mano che ha rank migliore della carta nemica passata con argomento
        NeapolitanCard bestOfSuit = getBestRankedCardOfSuit(suit);
        if(bestOfSuit != null){
            if(BriscolaCardPointsAndRankingRules.getRank(bestOfSuit.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(enemyCard.getCardNumber()))
                return bestOfSuit;
            else
                return null;
        }else
            return null;

    }


    /**
     * Get worst ranked card in hand with same points and suit of card in hand passed as an argument.
     *
     * @param card the card in hand to be used for comparison
     * @return the neapolitan card worst than the card passed as an argument (same points and suit)
     */
    public NeapolitanCard getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(NeapolitanCard card){
        NeapolitanCard worstRanked = card;
        for(NeapolitanCard c: myHand.getCardList()){
            if(BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(worstRanked.getCardNumber())
                    && BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(worstRanked.getCardNumber())
                    && c.getCardSuit().equals(card.getCardSuit()))
                worstRanked = c;
        }
        return worstRanked;
    }

    /**
     * Get worst point valued card in hand.
     *
     * @return the neapolitan card with worst point value in my hand
     */
    public NeapolitanCard getWorstPointValuedCardInHand(){
        if(myHand.isEmpty()){
            throw new IllegalStateException("Empty hand");
        }
        NeapolitanCard worstCard = myHand.getCardList().get(0);
        for(NeapolitanCard c:myHand.getCardList())
            if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) < BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()))
                worstCard = c;
            else if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) && //a parità di value torna rank peggiore
                    BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()))
                worstCard = c;

        return worstCard;
    }


}
