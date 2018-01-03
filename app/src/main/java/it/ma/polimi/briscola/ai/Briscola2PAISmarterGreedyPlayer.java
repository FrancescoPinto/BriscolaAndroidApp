package it.ma.polimi.briscola.ai;

import java.util.ArrayList;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Greedy player with some euristic improvements (see inline code comments to see what)
 */

public class Briscola2PAISmarterGreedyPlayer  extends AbstractBriscola2PAIPlayer {

    //MIGLIORATO CON L'EURISTICA "SE NON C'E' BISOGNO DI BUTTARE UNA BRISCOLA PER EVITARE DI FAR FAR PUNTI ALL'AVVERSARIO, CONSERVALA PER DOPO"
    private static final int smallThreshold = 1; //parametro da calibrare
    private static final int noThreshold = 11; //value that represents no tresholds are posed (is just to avoid "magic numbers" in code)
    private static final float riskTrumpThreshold = 0.9f; //probability that the adversary has a card of a certain suit must be greater thant this threshold, in order to risk and play a trump as first card
    private static final int trumpThresholdFirstCard = 4; //minimum value a trump mast have to be played as first card

    public int chooseMove(Briscola2PFullMatchConfig configuration, int playerIndex){
        config = configuration;
        myHand = config.getHand(playerIndex);

        NeapolitanCardSuit briscolaSuit = NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit());
        if(config.getSurface().size() == 0){

            computeSuitProbabilities(playerIndex); //calcola la probabilità (sono semplici probabilità che l'avversario (o il deck) abbiano carte di una certa suit
            NeapolitanCardSuit minAdversaryProbabilitySuit = getMinAdversaryProbabilitySuitInMyHand(); //prendi la suit che ha meno probabilità di essere nella mano avversaria (o nel deck)
            if(getAdversaryProbabilityTrump(briscolaSuit) > riskTrumpThreshold){ //fai la scelta solo se la probabilità è abbastanza alta
                NeapolitanCard trumpSuitCard = getBestRankedCardOfSuit(briscolaSuit); //butta una briscola se rischi che l'avversario abbia una carta con stessa suit delle carte che hai in mano
                if(trumpSuitCard != null && BriscolaCardPointsAndRankingRules.getPointValue(trumpSuitCard.getCardNumber()) >= trumpThresholdFirstCard){ //non serve a niente buttare una briscola a terra come prima carta se non vale molti punti (e quindi se non è probabile che vinca contro molte delle carte che è probabile l'avversario abbia in mano)
                    return findCardPositionInHand(trumpSuitCard);
                }
            }
            if(minAdversaryProbabilitySuit != null) { //se decido di non giocare la briscola e c'è un minimo

                //vai in difensiva e butta la carta peggiore che hai in mano
                NeapolitanCard probabilisticChoice = getWorstPointValuedCard(getCardsOfSuitInHand(minAdversaryProbabilitySuit));//prendi la peggior carta di quella suit

                if(probabilisticChoice != null)
                    return findCardPositionInHand(probabilisticChoice);
            }

            //se non c'è un minimo, comportati da greedy
            //greedy STUPIDO: butta a terra la carta di massimo valore
            NeapolitanCard bestRankedInHand = getBestRankedCard(myHand.getCardList()); //cerco quella con miglior rank
            if(bestRankedInHand != null && BriscolaCardPointsAndRankingRules.getPointValue(bestRankedInHand.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta a punteggio nullo e rango peggiore da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                return findCardPositionInHand(getBestPointValuedCard(myHand.getCardList())); //le altre bestpoint saranno tutte a valore nullo, però questo metodo ritorna quello con rank peggiore
            else
                return findCardPositionInHand(bestRankedInHand); //se non è a punteggio nullo ALLORA il rank è il migliore in mano (se hai più carte con stesso rank in mano scegline una qualunque (l'algoritmo qui di default si sceglie quella che viene prima nella lista)


        }else if(config.getSurface().size() == 1){ //se sono il secondo giocatore
            NeapolitanCard cardOnSurface = config.getSurface().getCard(0);

            int enemyPointValue = BriscolaCardPointsAndRankingRules.getPointValue(cardOnSurface.getCardNumber());

            if(enemyPointValue < smallThreshold){
                //se hai carte di valore 0 che non siano briscole, butta quella con rank più basso (non importa chi vince)
                //se hai carte di valore 0 ma briscole, prova a vincere senza buttare la briscola (cioè butta una non briscola, STESSO seme della prima carta, rank migliore)
                //se non riesci a vincere il match senza buttare briscole, butta la carta non briscola con punteggio e rank più basso che riesci
                //se hai solo briscole in mano, butta la briscola con peggior rank e peggior valore

                    //sia nel caso in cui l'avversario ha buttato una briscola o meno
                NeapolitanCard zeroValuedNotTrump = getZeroValuedWorstRankedCardNotOfSuit(NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));

                if (zeroValuedNotTrump != null) //butta una carta che non vale niente, ma conserva le briscole
                    return findCardPositionInHand(zeroValuedNotTrump);
                else { //vinci usando la carta dello stesso seme con rank minimo che hai (no threshold, tanto vinci)
                    NeapolitanCard minimumRankSameSeedToWin = getMinimumRankSameSeedToWinWithThreshold(cardOnSurface, cardOnSurface.getCardSuitEnum(), noThreshold);//todo facendo così risparmi carte forti, e in futuro se l'avversario ne ha buttata con punti alti, dopo andrai a buttare una carta buona
                    if (minimumRankSameSeedToWin != null)
                        return findCardPositionInHand(minimumRankSameSeedToWin);
                    else {
                                        //accetto di perdere ma cerco di minimizzare i punti che regalo all'avversario <- POICHE' QUI DECIDO DI PERDERE NON DEVO GIOCARE UNA CARTA FORTE, ALTRIMENTI E' MEGLIO BUTTARE UNA BRISCOLA!
                                            //questo branch abbassava le prestazioni, è stato commentato via
                                            //   NeapolitanCard minimumPointValueNotTrumpWithThreshold = getMinimumPointValueNotTrumpWithThreshold(briscolaSuit, acceptableMaxLoss);
                                            //  if (minimumPointValueNotTrumpWithThreshold != null)
                                            //      return findCardPositionInHand(minimumPointValueNotTrumpWithThreshold);
                                            //   else {

                        //se proprio hai solo briscole E carte che non soddisfano la threshold, prova a vincere usando una briscola (se la carta in superficie è briscola questo metodo non viene chiamato perché minimuRankSameSeed se ne occupa
                            NeapolitanCard worstTrumpToWin = getMinimumRankSameSeedToWinWithThreshold(cardOnSurface,briscolaSuit,noThreshold);
                            if (worstTrumpToWin != null)
                                return findCardPositionInHand(worstTrumpToWin);
                            else { //se hai solo carte forti, non trump, SEI COSTRETTO A PERDERE ... non puoi fare niente, solo scegliere la carta con meno punti possibile
                                NeapolitanCard worstPointsInHand = getWorstPointValuedCard(myHand.getCardList());
                                    return findCardPositionInHand(worstPointsInHand);
                            }
                        }
                    }

            } else if(enemyPointValue >= smallThreshold) {

                if (cardOnSurface.getCardSuit().equals(config.getBriscolaSuit())) {
                    //se il primo ha giocato una briscola
                    // NeapolitanCard card = getWorstBriscolaBetterThan(cardOnSurface);
                    // Se non ho nessuna briscola in grado di battere la carta a terra
                    //scegli la carta che minimizza la perdita (non puoi ribattere con una briscola, quindi perderai, quindi dai la carta con minimo punteggio che hai in mano
                    // se ho solo briscole in mano, butto la briscola peggiore (da calcolare, ricorda che prima hai preso la peggiore MA COMUNQUE MIGLIORE DI cardOnSurface)
                    //se puoi ribattere con una briscola migliore
                    //gioca la briscola che ti dà massimo punteggio

                    NeapolitanCard card = getBestRankedCardOfSuitInHandBetterRankedThan(cardOnSurface, briscolaSuit);  //lo basi sul rank per decidere se puoi vincere il match, se rank migliore allora vinci il match //todo <- qui prende la best ranked ma dovresti considerare anche i punti???
                    if (card == null)
                        return findCardPositionInHand(getWorstPointValuedCardInHand()); //se non puoi vincere il match, cerca di far fare all'avversario meno punti, indipendentemente dal fatto che butti una briscola o meno //todo <- cerca di preservare le briscole per dopo
                    else if (BriscolaCardPointsAndRankingRules.getPointValue(card.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta sempre briscola sempre migliore dell'avversario ma a punteggio nullo e rank minimo da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                        return findCardPositionInHand(getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(card)); //le altre bestpoint saranno tutte a valore nullo, però questo metodo ritorna quello con rank peggiore
                    else
                        return findCardPositionInHand(card); //da quanto filtrato prima, la carta ha ranking e punteggio unici, quindi uso quella direttamente


                } else {
                    //se non ha giocato una briscola
                    //cerco la carta peggiore con cui posso vincere il match, se non ce l'ho
                    //se posso vincere il match senza giocare una briscola, gioco la non briscola che mi fa vincere il match con massimo punteggio
                    //altrimenti gioco la briscola con massimo punteggio, e vinco io il match
                    //se non posso vincere il match allora cerco di non far guadagnare punti all'avversario

                    NeapolitanCard worstToWin = getWorstRankedButBetterRankedThanWithSameSuit(myHand.getCardList(),cardOnSurface);
                    if(worstToWin != null)
                        return findCardPositionInHand(worstToWin); //cerca di vincere con la peggior carta che hai in mano

                    NeapolitanCard bestSameSuit = getBestRankedCardOfSuitInHandBetterRankedThan(cardOnSurface, NeapolitanCardSuit.getCardSuit(cardOnSurface.getCardSuit())); //getBestSameSuitBetterRankedThan(cardOnSurface); //valuto se posso vincere con la stessa suit
                    NeapolitanCard bestBriscola = getBestRankedCardOfSuit(NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));// getBestRankedBriscolaInHand();

                    if (bestSameSuit != null) {
                        if (BriscolaCardPointsAndRankingRules.getPointValue(bestSameSuit.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta sempre di questo seme sempre migliore dell'avversario ma a punteggio nullo e rank minimo da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                            return findCardPositionInHand(getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(bestSameSuit));
                        else
                            return findCardPositionInHand(bestSameSuit); //altrimenti il rank dice univocamente chi ha miglior punteggio
                    } else if (bestSameSuit == null && bestBriscola != null) {
                        if (BriscolaCardPointsAndRankingRules.getPointValue(bestBriscola.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta sempre briscola sempre migliore dell'avversario ma a punteggio nullo e rank minimo da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                            return findCardPositionInHand(getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(bestBriscola));
                        else
                            return findCardPositionInHand(bestBriscola); //altrimenti il rank dice univocamente chi ha miglior punteggio
                    } else//se non ho di meglio della stessa suite, e non ho neanche una briscola, scelgo la carta peggiore che mi possa
                        return findCardPositionInHand(getWorstPointValuedCardInHand());

                }
            }

        }else
            throw new IllegalStateException("No choice can be done in the current state");
        return 0;
    }
}
