package it.ma.polimi.briscola.ai;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Created by utente on 11/12/17.
 */

public class Briscola2PAISmarterGreedyPlayer  extends AbstractBriscola2PAIPlayer {

    //todo MIGLIORATO CON L'EURISTICA "SE NON C'E' BISOGNO DI BUTTARE UNA BRISCOLA PER EVITARE DI FAR FAR PUNTI ALL'AVVERSARIO, CONSERVALA PER DOPO"
    //todo ANCORA NON CI SONO RAGIONAMENTI PROBABILISTICI!
    private static final int smallThreshold = 1; //parametro da calibrare

    public int chooseMove(Briscola2PMatchConfig configuration){
        config = configuration;
        myHand = config.getHand(config.PLAYER1);


        if(config.getSurface().size() == 0){

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


            int enemyPointValue = BriscolaCardPointsAndRankingRules.getPointValue(cardOnSurface.toString());

            if(enemyPointValue == 0){
                //todo se hai carte di valore 0 che non siano briscole, butta quella con rank più basso (non importa chi vince)
                //todo se hai carte di valore 0 ma briscole, prova a vincere senza buttare la briscola (cioè butta una non briscola, STESSO seme della prima carta, rank migliore)
                //todo se non riesci a vincere il match senza buttare briscole, butta la carta non briscola con punteggio e rank più basso che riesci <- in realtà dovresti fare una scelta probabilistica, ma per ora evita
                //todo se hai solo briscole in mano, butta la briscola con peggior rank e peggior valore

                NeapolitanCard zeroValuedNotTrump = getZeroValuedWorstRankedCardNotOfSuit(NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));

                if(zeroValuedNotTrump != null)
                    return findCardPositionInHand(zeroValuedNotTrump);
                else{
                    NeapolitanCard minimumRankSameSeedToWinWithThreshold = getMinimumRankSameSeedToWinWithThreshold(cardOnSurface,cardOnSurface.getCardSuitEnum(),smallThreshold);//todo facendo così risparmi carte forti, e in futuro se l'avversario ne ha buttata con punti alti, dopo andrai a buttare una carta buona
                    if(minimumRankSameSeedToWinWithThreshold != null)
                        return findCardPositionInHand(minimumRankSameSeedToWinWithThreshold);
                    else{ //accetto di perdere ma cerco di minimizzare i punti che regalo all'avversario
                        NeapolitanCard minimumPointValueNotTrumpWithThreshold = getMinimumPointValueNotTrumpWithThreshold(NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()), smallThreshold);
        /*                        if(minimumPointValueNotTrumpWithThreshold != null) <- ATTENTO, io su questo metterei una threshold! perché è capace di buttare carte forti se non la limiti!
                                    return findCardPositionInHand(minimumPointValueNotTrumpWithThreshold);
                                else { //se proprio
                                    NeapolitanCard worstTrumpWithThreshold
                                            if(worstTrumpWithThreshold != null)
                                                return  findCardPositionInHand(worstTrump);
                                            else { //se proprio HAI SOLO CARTE FORTI IN MANO (superano le threshold), itera il ragionamento MA SENZA THRESHOLD, cioe
                                                NeapolitanCard minimumRankSameSeedToWin
                                                        if(minimumRankSameSeedToWin)
                                                            return;
                                                        else{ //todo: MOLTA ATTENZIONE, se ho solo carte forti, NON voglio perdere punti, quindi NON provo a perdere buttando una carta non di seme
                                                            NeapolitanCard worstTrumpToWin
                                                                    if(worstTrumpToWin)
                                                                        return;
                                                                    else{ //se sono costretto a perdere, minimizza i danni
                                                                        NeapolitanCard worstRankedAnySeed <- se devo perdere
                                                                                return;
                                                                    }
                                                        }
                                            }
                                }
                        //se hai solo
                        ATTENTO: io testerei questo prima contro la Dumb, e poi semplicemente aggiungerei un semplice metodo probabilistico per la scelta della FirstCard, e stop, non impazzire
                    }
             */
                        return 0; //todo, rimuovi questa riga (è solo per evitare errori
                  //  }
            //} else if (enemyPointValue <= smallThreshold){ <- REGOLATI IN BASE ALLE PRESTAZIONI (fai dei test), accorpa con il precedente blocco
                //todo, se il punteggio che avresti vincendo è basso, ma sei costretto a b
            }}} else if(enemyPointValue >= smallThreshold) {
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

                    NeapolitanCard card = getBestRankedCardOfSuitInHandBetterRankedThan(cardOnSurface, NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));  //lo basi sul rank per decidere se puoi vincere il match, se rank migliore allora vinci il match //todo <- qui prende la best ranked ma dovresti considerare anche i punti???
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

        return 0; //todo, rimuovila, è solo per evitare gli errori
    }








}
