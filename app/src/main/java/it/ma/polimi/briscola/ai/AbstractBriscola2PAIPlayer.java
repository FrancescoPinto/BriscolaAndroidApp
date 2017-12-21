package it.ma.polimi.briscola.ai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Created by utente on 07/11/17.
 */

public abstract class AbstractBriscola2PAIPlayer implements Briscola2PAIPlayer {
    Briscola2PHand myHand;
    Briscola2PMatchConfig config;
    float suitProbabilities[] = new float[] {0f,0f,0f,0f};
    int batonsIndex = 0, cupsIndex = 1, goldsIndex = 2, swordsIndex = 3;
    final int cardsPerSuit = 10;

    public int findCardPositionInHand(NeapolitanCard c) {
        for (int i = 0; i < myHand.size(); i++)
            if (myHand.getCard(i).equalTo(c))
                return i;

        throw new IllegalArgumentException("The argument is not contained in hand");
    }

    public void computeSuitProbabilities(int playerIndex){
        List<NeapolitanCard> cardsInPiles = config.getPile(Briscola2PMatchConfig.PLAYER0).getCardList();
        cardsInPiles.addAll(config.getPile(Briscola2PMatchConfig.PLAYER1).getCardList());
        cardsInPiles.addAll(config.getHand(playerIndex).getCardList());

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

    public Set<NeapolitanCardSuit> getCardSuitInMyHand(){
        Set<NeapolitanCardSuit> cardSuitsInHand = new HashSet<>();
        for(NeapolitanCard c: myHand.getCardList()){
            cardSuitsInHand.add(c.getCardSuitEnum());
        }
        return cardSuitsInHand;
    }
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

    public NeapolitanCard getZeroValuedWorstRankedCardNotOfSuit(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = getCardsNotOfSuitInHand(suit); //non briscola!
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

    public NeapolitanCard getMinimumRankSameSeedToWinWithThreshold(NeapolitanCard card, NeapolitanCardSuit suit, int maxThreshold){
        List<NeapolitanCard> cards = getCardsOfSuitInHand(suit);
        if(cards.isEmpty())
            return null;

        List<NeapolitanCard> satisfyingThreshold = getCardsSatisfyingPointThreshold(cards,maxThreshold);

        if(satisfyingThreshold.isEmpty())
            return null;

        return getWorstRankedButBetterRankedThan(satisfyingThreshold, card);

    }

    public NeapolitanCard getMinimumPointValueNotTrumpWithThreshold(NeapolitanCardSuit trumpSuit, int maxThreshold){
        List<NeapolitanCard> cards = getCardsNotOfSuitInHand(trumpSuit);
        if(cards.isEmpty())
            return null;

        List<NeapolitanCard> satisfyingThreshold = getCardsSatisfyingPointThreshold(cards,maxThreshold);

        if(satisfyingThreshold.isEmpty())
            return null;

        return getWorstPointValuedCard(satisfyingThreshold);
    }

    public List<NeapolitanCard> getCardsSatisfyingPointThreshold(List<NeapolitanCard> cards, int maxThreshold){
        List<NeapolitanCard> satisfyingThreshold = new ArrayList<>();
        for(NeapolitanCard c: cards){
            if(BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) <= maxThreshold)
                satisfyingThreshold.add(c);
        }
        return satisfyingThreshold;
    }

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

    public NeapolitanCard getBestRankedCardOfSuit(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = getCardsOfSuitInHand(suit);
        return getBestRankedCard(cards);
    }

    public NeapolitanCard getBestPointValuedCardOfSuit(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = getCardsOfSuitInHand(suit);
        return getBestPointValuedCard(cards);

    }



    public List<NeapolitanCard> getCardsOfSuitInHand(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = new ArrayList<>();
        for (NeapolitanCard c : myHand.getCardList()) {
            if (c.getCardSuit().equals(suit.getSuit()))
            cards.add(c);
        }
        return cards;
    }

    public List<NeapolitanCard> getCardsNotOfSuitInHand(NeapolitanCardSuit suit){
        List<NeapolitanCard> cards = new ArrayList<>();
        for (NeapolitanCard c : myHand.getCardList()) {
            if (!c.getCardSuit().equals(suit.getSuit()))
                cards.add(c);
        }
        return cards;
    }

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
  /*
    public List<NeapolitanCard> getBriscolasInHand() {
        List<NeapolitanCard> briscolaInHand = new ArrayList<>();

        for (NeapolitanCard c : myHand.getHand()) {
            if (c.getCardSuit().equals(config.getBriscolaString()))
                briscolaInHand.add(c);
        }
        return briscolaInHand;
    }

    public List<NeapolitanCard> getNonBriscolasInHand() {
        List<NeapolitanCard> nonBriscolaInHand = new ArrayList<>();

        for (NeapolitanCard c : myHand.getHand()) {
            if (!c.getCardSuit().equals(config.getBriscolaString()))
                nonBriscolaInHand.add(c);
        }
        return nonBriscolaInHand;
    }
    */
/*
    public NeapolitanCard getWorstPointValuedCardInHand(){ //la carta con punteggio peggiore (indipendente da se briscola o meno), a parità butto quella con rank peggiore
        NeapolitanCard worstCard = myHand.getHand().get(0);
        for(NeapolitanCard c:myHand.getHand())
            if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) < BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()))
                worstCard = c;
            else if (BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) && //a parità di value torna rank peggiore
                    BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()))
                worstCard = c;

        return worstCard;

        //todo l'implementazione qui sotto è utile nel caso in cui tu voglia fare una scelta speciale tra briscole e non briscole nel ritorno del valore, considerala per il cardCounter! ma per il Dumb è troppo articolata
        /*NeapolitanCard worstBriscola = getWorstBriscolaInHand();
        NeapolitanCard worstNonBriscola = getWorstNonBriscolaCardInHand();

        //QUI scelgo di buttare quella con MINIMO punteggio, indipendentemente da se è briscola o meno //todo, vale quanto su detto, questa non per forza è una scelta ottima
        if(worstNonBriscola != null && worstBriscola != null) {
            if (BriscolaCardPointsAndRankingRules.getPointValue(worstNonBriscola.getCardNumber()) > BriscolaCardPointsAndRankingRules.getPointValue(worstBriscola.getCardNumber()))
                return worstNonBriscola;
            else if (BriscolaCardPointsAndRankingRules.getPointValue(worstNonBriscola.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(worstBriscola.getCardNumber()) && //a parità di value torna rank peggiore
                    BriscolaCardPointsAndRankingRules.getRank(worstNonBriscola.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(worstBriscola.getCardNumber()))
                return worstNonBriscola;
            else
                return worstBriscola;
        }
        else if(worstBriscola != null)
            return worstBriscola;
        else if(worstNonBriscola != null)
            return worstNonBriscola;
        else
            throw new IllegalStateException();
            */
   /* }

    public NeapolitanCard getWorstPointValuedBriscolaCardInHand() { //a parità di punti, quella con peggior rank viene buttata

        List<NeapolitanCard> briscolaInHand = getBriscolasInHand();

        if (briscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard worstCard = briscolaInHand.get(0);
            for (NeapolitanCard c : briscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) > BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()))
                    worstCard = c;
                else if(BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) &&
                        BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    worstCard = c;
            }

            return worstCard;
        }
    }



    public List<NeapolitanCard> getSameSuitInHand(NeapolitanCard card){
        List<NeapolitanCard> sameSuitInHand = new ArrayList<>();

        for(NeapolitanCard c : myHand.getHand())
        {
            if(c.getCardSuit().equals(card.getCardSuit()))
                sameSuitInHand.add(c);
        }
        return sameSuitInHand;
    }
    public NeapolitanCard getBestBriscolaBetterRankedThan(NeapolitanCard enemyCard) { //prende la migliore briscola dalla mano che ha rank migliore della carta nemica passata con argomento
        //Todo potresti semplificare entrambi prendendo prima la migliore/peggiore briscola dalla mano e poi comparandolo con la carta nemica!


        if (briscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard maxCard = enemyCard;
            for (NeapolitanCard c : briscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getRank(maxCard.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    maxCard = c;
            }
            if (maxCard == enemyCard)   //se non hai una briscola migliore (ma hai delle briscole)
                return null;
            else
                return maxCard;
        }
    }

    public NeapolitanCard getBestSameSuitBetterRankedThan(NeapolitanCard enemyCard){


        List<NeapolitanCard>  sameSuitInHand = getSameSuitInHand(enemyCard);

        if (sameSuitInHand.isEmpty())
            return null;
        else {
            NeapolitanCard maxCard = enemyCard;
            for (NeapolitanCard c : sameSuitInHand) {
                if (BriscolaCardPointsAndRankingRules.getRank(maxCard.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    maxCard = c;
            }
            if (maxCard == enemyCard)   //se non hai una briscola migliore (ma hai delle briscole)
                return null;
            else
                return maxCard;
        }
    }

    public NeapolitanCard getWorstBriscolaBetterThan(NeapolitanCard enemyCard){ //prende la peggiore briscola dalla mano che ha rank migliore della carta nemica passata con argomento

        List<NeapolitanCard> briscolaInHand = getBriscolasInHand();


        if(briscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard minBetterCard = enemyCard;
            for (NeapolitanCard c : briscolaInHand) {
                if (minBetterCard != enemyCard) {
                    if (BriscolaCardPointsAndRankingRules.getRank(minBetterCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()) &&
                            BriscolaCardPointsAndRankingRules.getRank(enemyCard.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                        minBetterCard = c;
                }
            }


            if(minBetterCard == enemyCard)   //se non hai una briscola migliore (ma hai delle briscole)
                return null;
            else
                return minBetterCard;
        }


    } //todo ATTENTO questo non puoi semplificarlo (perché la worst briscola se è peggiore di enemy non implica che tu non abbia in mano una briscola migliore di enemy ma "minima"

    public NeapolitanCard getWorstRankedNonBriscolaCardInHand(){
        List<NeapolitanCard> nonBriscolaInHand = getNonBriscolasInHand();

        if(nonBriscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard worstCard = nonBriscolaInHand.get(0);
            for (NeapolitanCard c : nonBriscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    worstCard = c;
            }

            return worstCard;
        }
    }

    public NeapolitanCard getBestRankedBriscolaInHand(){
        List<NeapolitanCard> briscolaInHand = getNonBriscolasInHand();

        if(briscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard worstCard = briscolaInHand.get(0);
            for (NeapolitanCard c : briscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()) > BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    worstCard = c;
            }

            return worstCard;
        }
    }

    public NeapolitanCard getWorstRankedBriscolaInHand(){
        List<NeapolitanCard> briscolaInHand = getBriscolasInHand();

        if(briscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard worstCard = briscolaInHand.get(0);
            for (NeapolitanCard c : briscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    worstCard = c;
            }

            return worstCard;
        }
    }


    public NeapolitanCard getWorstRankedCardInHand(){
        NeapolitanCard worstBriscola = getWorstBriscolaInHand();
        NeapolitanCard worstNonBriscola = getWorstNonBriscolaCardInHand();
        //todo un algoritmo intelligente baserebbe la scelta tra chi è peggiore anche su aspetti probabilistici
        //todo un algoritmo intelligente baserebbe la sua scelta anche sul rank (magari può convenire giocare una carta con rank peggiore ma briscola, soprattutto se ci sono alte probabilità che dopo ...)
        //todo un algoritmo intelligente potrebbe anche decidere di giocare la briscola qualora la carta non briscola sia di alto punteggio, e quindi regaleresti punti!
        //QUI euristicamente decido di non giocare la briscola se c'è qualcosa non briscola
        if(worstNonBriscola != null)
            return worstNonBriscola;
        else if(worstBriscola != null)
            return worstBriscola;
        else
            throw new IllegalStateException();
    }


    public NeapolitanCard getWorstPointValuedNonBriscolaCardInHand() { //a parità di punti, quella con peggior rank viene buttata

        List<NeapolitanCard> nonBriscolaInHand = getNonBriscolasInHand();

        if (nonBriscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard worstCard = nonBriscolaInHand.get(0);
            for (NeapolitanCard c : nonBriscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) > BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()))
                    worstCard = c;
                else if(BriscolaCardPointsAndRankingRules.getPointValue(worstCard.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) &&
                        BriscolaCardPointsAndRankingRules.getRank(worstCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber()))
                    worstCard = c;
            }

            return worstCard;
        }
    }

    public NeapolitanCard getBestPointValuedBriscolaCardInHand() { //a parità di punti, quella con peggior rank viene buttata

        List<NeapolitanCard> briscolaInHand = getBriscolasInHand();

        if (briscolaInHand.isEmpty())
            return null;
        else {
            NeapolitanCard bestCard = briscolaInHand.get(0);
            for (NeapolitanCard c : briscolaInHand) {
                if (BriscolaCardPointsAndRankingRules.getPointValue(bestCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()))
                    bestCard = c;
                else if(BriscolaCardPointsAndRankingRules.getPointValue(bestCard.getCardNumber()) == BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber()) &&
                        BriscolaCardPointsAndRankingRules.getRank(bestCard.getCardNumber()) < BriscolaCardPointsAndRankingRules.getRank(c.getCardNumber())) //todo, questa cosa non ha senso se ti limiti a considerare solo le briscole ... (ma lo ha se consideri anche le non briscole)
                    bestCard = c;
            }

            return bestCard;
        }
    }*/