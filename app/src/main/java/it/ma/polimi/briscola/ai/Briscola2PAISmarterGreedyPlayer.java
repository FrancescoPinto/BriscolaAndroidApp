package it.ma.polimi.briscola.ai;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Created by utente on 11/12/17.
 */

public class Briscola2PAISmarterGreedyPlayer  extends AbstractBriscola2PAIPlayer {

    //todo MIGLIORATO CON L'EURISTICA "SE NON C'E' BISOGNO DI BUTTARE UNA BRISCOLA PER EVITARE DI FAR FAR PUNTI ALL'AVVERSARIO, CONSERVALA PER DOPO"
    private static final int smallThreshold = 1; //parametro da calibrare
    private static final int acceptableMaxLoss = 2;
    private static final int noThreshold = 11;
    private static final float riskTrumpThreshold = 0.5f;
    private static final int trumpThresholdFirstCard = 4;

    public int chooseMove(Briscola2PFullMatchConfig configuration, int playerIndex){
        config = configuration;
        myHand = config.getHand(playerIndex);

        NeapolitanCardSuit briscolaSuit = NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit());
        if(config.getSurface().size() == 0){

            computeSuitProbabilities(playerIndex);
            NeapolitanCardSuit minAdversaryProbabilitySuit = getMinAdversaryProbabilitySuitInMyHand();
            if(getAdversaryProbabilityTrump(briscolaSuit) > riskTrumpThreshold){
                NeapolitanCard trumpSuitCard = getBestRankedCardOfSuit(briscolaSuit);
                if(trumpSuitCard != null && BriscolaCardPointsAndRankingRules.getPointValue(trumpSuitCard.getCardNumber()) >= trumpThresholdFirstCard){
                    return findCardPositionInHand(trumpSuitCard);
                }
            }
            if(minAdversaryProbabilitySuit != null) { //se c'è un minimo

                NeapolitanCard probabilisticChoice = getBestRankedCardOfSuit(minAdversaryProbabilitySuit);

                if(BriscolaCardPointsAndRankingRules.getPointValue(probabilisticChoice.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta a punteggio nullo e rango peggiore da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                    return findCardPositionInHand(getBestPointValuedCardOfSuit(minAdversaryProbabilitySuit)); //le altre bestpoint saranno tutte a valore nullo, però questo metodo ritorna quello con rank peggiore
                else
                    return findCardPositionInHand(probabilisticChoice);
            }

            //se non c'è un minimo, comportati da greedy
            //greedy STUPIDO: butta a terra la carta di massimo valore
            // todo, il greedy intelligente invece valuta se è meglio buttare una briscola o altro <- (ci vogliono considerazioni probabilistiche su quanto è probabile l'avversario abbia una briscola)
            NeapolitanCard bestRankedInHand = getBestRankedCard(myHand.getCardList()); //cerco quella con miglior rank
            if(BriscolaCardPointsAndRankingRules.getPointValue(bestRankedInHand.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta a punteggio nullo e rango peggiore da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                return findCardPositionInHand(getBestPointValuedCard(myHand.getCardList())); //le altre bestpoint saranno tutte a valore nullo, però questo metodo ritorna quello con rank peggiore
            else
                return findCardPositionInHand(bestRankedInHand); //se non è a punteggio nullo ALLORA il rank è il migliore in mano (se hai più carte con stesso rank in mano scegline una qualunque (l'algoritmo qui di default si sceglie quella che viene prima nella lista)


        }else if(config.getSurface().size() == 1){ //se sono il secondo giocatore
            NeapolitanCard cardOnSurface = config.getSurface().getCard(0);

            //todo UN ALGORITMO PIU' INTELLIGENTE DI UN PURO GREEDY ora FAREBBE UNA COSA: se l'avversario ha giocato una carta a valore 0 allora gioco carte di basso valore e cerco di vincere, conservandomi le carte ad alto valore per dopo (sperando di vincere)
            //todo if(BriscolaCardPointsAndRankingRules.getRank(cardOnSurface.getCardNumber())) ... ma sei proprio sicuro che sia meglio come approccio?


            int enemyPointValue = BriscolaCardPointsAndRankingRules.getPointValue(cardOnSurface.getCardNumber());

            if(enemyPointValue < smallThreshold){
                //todo se hai carte di valore 0 che non siano briscole, butta quella con rank più basso (non importa chi vince)
                //todo se hai carte di valore 0 ma briscole, prova a vincere senza buttare la briscola (cioè butta una non briscola, STESSO seme della prima carta, rank migliore)
                //todo se non riesci a vincere il match senza buttare briscole, butta la carta non briscola con punteggio e rank più basso che riesci <- in realtà dovresti fare una scelta probabilistica, ma per ora evita
                //todo se hai solo briscole in mano, butta la briscola con peggior rank e peggior valore

                    //sia nel caso in cui l'avversario ha buttato una briscola o meno
                NeapolitanCard zeroValuedNotTrump = getZeroValuedWorstRankedCardNotOfSuit(NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));

                if (zeroValuedNotTrump != null) //butta una carta che non vale niente, ma conserva le briscole
                    return findCardPositionInHand(zeroValuedNotTrump);
                else { //vinci usando la carta dello stesso seme con rank minimo che hai (no threshold, tanto vinci)
                    NeapolitanCard minimumRankSameSeedToWin = getMinimumRankSameSeedToWinWithThreshold(cardOnSurface, cardOnSurface.getCardSuitEnum(), noThreshold);//todo facendo così risparmi carte forti, e in futuro se l'avversario ne ha buttata con punti alti, dopo andrai a buttare una carta buona
                    if (minimumRankSameSeedToWin != null)
                        return findCardPositionInHand(minimumRankSameSeedToWin);
                    else { //accetto di perdere ma cerco di minimizzare i punti che regalo all'avversario <- POICHE' QUI DECIDO DI PERDERE NON DEVO GIOCARE UNA CARTA FORTE, ALTRIMENTI E' MEGLIO BUTTARE UNA BRISCOLA!
                    //todo -> questo branch abbassava le prestazioni, cancellapure questi commenti
                        //   NeapolitanCard minimumPointValueNotTrumpWithThreshold = getMinimumPointValueNotTrumpWithThreshold(briscolaSuit, acceptableMaxLoss);
                      //  if (minimumPointValueNotTrumpWithThreshold != null)
                      //      return findCardPositionInHand(minimumPointValueNotTrumpWithThreshold);
                     //   else { //se proprio hai solo briscole E carte che non soddisfano la threshold, prova a vincere usando una briscola (se la carta in superficie è briscola questo metodo non viene chiamato perché minimuRankSameSeed se ne occupa
                            NeapolitanCard worstTrumpToWin = getMinimumRankSameSeedToWinWithThreshold(cardOnSurface,briscolaSuit,noThreshold);
                            if (worstTrumpToWin != null)
                                return findCardPositionInHand(worstTrumpToWin);
                            else { //se hai solo carte forti, non trump, SEI COSTRETTO A PERDERE ... non puoi fare niente, solo scegliere la carta con meno punti possibile
                                NeapolitanCard worstPointsInHand = getWorstPointValuedCard(myHand.getCardList());
                                    return findCardPositionInHand(worstPointsInHand);
                            }
                        }
                    }


                  //  }
            //} else if (enemyPointValue <= smallThreshold){ <- REGOLATI IN BASE ALLE PRESTAZIONI (fai dei test), accorpa con il precedente blocco
                //todo, se il punteggio che avresti vincendo è basso, ma sei costretto a b
            } else if(enemyPointValue >= smallThreshold) {
                //todo ATTENTO, non è detto che applicare il puro greedy quando ci sono in ballo carte buone sia un bene

                //todo usa il greedy puro perché devi cercare di massimizzare i punti <-
                if (cardOnSurface.getCardSuit().equals(config.getBriscolaSuit())) {
                    //se il primo ha giocato una briscola
                    // NeapolitanCard card = getWorstBriscolaBetterThan(cardOnSurface); //todo un alternativo algoritmo sceglierebbe la peggiore briscola che ha in mano, per conservarsi la migliore nei successivi round, ma questa classe è greedy pura (i.e. massimizza il punteggio in questo turno)
                    // Se non ho nessuna briscola in grado di battere la carta a terra
                    //scegli la carta che minimizza la perdita (non puoi ribattere con una briscola, quindi perderai, quindi dai la carta con minimo punteggio che hai in mano
                    //todo un comportamento euristico direbbe "conservati le briscole in alcuni casi, anche a costo di regalare punti in più ora all'avversario, perché poi ne potresti guadagnare!"
                    // se ho solo briscole in mano, butto la briscola peggiore (da calcolare, ricorda che prima hai preso la peggiore MA COMUNQUE MIGLIORE DI cardOnSurface)
                    //todo in una versione migliore nella scelta della peggiore carta (non briscola o meno) bisogna anche considerare questioni probabilistiche! per ora è puro greedy di questo turno: tutte le carte a basso punteggio sono uguali (giusto cerca di non giocare le briscole proprio per non essere cretino)
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
                    //se posso vincere il match senza giocare una briscola, gioco la non briscola che mi fa vincere il match con massimo punteggio
                    //altrimenti gioco la briscola con massimo punteggio, e vinco io il match //todo un approccio migliore oculerebbe meglio questa scelta, e anche quella di prima
                    //se non posso vincere il match allora cerco di non far guadagnare punti all'avversario

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
