package it.ma.polimi.briscola.ai;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * AI that implements a purely greedy player
 *
 * @author Francesco Pinto
 */
public class Briscola2PAIDumbGreedyPlayer extends AbstractBriscola2PAIPlayer {


    public int chooseMove(Briscola2PFullMatchConfig configuration, int playerIndex){
        config = configuration;
        myHand = config.getHand(playerIndex);


        if(config.getSurface().size() == 0){

            //greedy STUPIDO: butta a terra la carta di massimo valore
            NeapolitanCard bestRankedInHand = getBestRankedCard(myHand.getCardList()); //cerco quella con miglior rank
            if(BriscolaCardPointsAndRankingRules.getPointValue(bestRankedInHand.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta a punteggio nullo e rango peggiore da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                return findCardPositionInHand(getBestPointValuedCard(myHand.getCardList())); //le altre bestpoint saranno tutte a valore nullo, però questo metodo ritorna quello con rank peggiore
            else
                return findCardPositionInHand(bestRankedInHand); //se non è a punteggio nullo ALLORA il rank è il migliore in mano (se hai più carte con stesso rank in mano scegline una qualunque (l'algoritmo qui di default si sceglie quella che viene prima nella lista)


        }else if(config.getSurface().size() == 1){ //se sono il secondo giocatore
            NeapolitanCard cardOnSurface = config.getSurface().getCard(0);


            if(cardOnSurface.getCardSuit().equals(config.getBriscolaSuit())){
                //se il primo ha giocato una briscola
                // Se non ho nessuna briscola in grado di battere la carta a terra
                //scegli la carta che minimizza la perdita (non puoi ribattere con una briscola, quindi perderai, quindi dai la carta con minimo punteggio che hai in mano
                // se ho solo briscole in mano, butto la briscola peggiore (da calcolare, ricorda che prima hai preso la peggiore MA COMUNQUE MIGLIORE DI cardOnSurface)
                //per ora è puro greedy di questo turno: tutte le carte a basso punteggio sono uguali (giusto cerca di non giocare le briscole proprio per non essere cretino)
                //se puoi ribattere con una briscola migliore
                //gioca la briscola che ti dà massimo punteggio

                NeapolitanCard card = getBestRankedCardOfSuitInHandBetterRankedThan(cardOnSurface, NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));  //lo basi sul rank per decidere se puoi vincere il match, se rank migliore allora vinci il match
                if(card == null)
                    return findCardPositionInHand(getWorstPointValuedCardInHand()); //se non puoi vincere il match, cerca di far fare all'avversario meno punti, indipendentemente dal fatto che butti una briscola o meno
                else if(BriscolaCardPointsAndRankingRules.getPointValue(card.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta sempre briscola sempre migliore dell'avversario ma a punteggio nullo e rank minimo da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                    return findCardPositionInHand(getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(card)); //le altre bestpoint saranno tutte a valore nullo, però questo metodo ritorna quello con rank peggiore
                else
                    return findCardPositionInHand(card); //da quanto filtrato prima, la carta ha ranking e punteggio unici, quindi uso quella direttamente


            } else {
                //se non ha giocato una briscola
                //se posso vincere il match senza giocare una briscola, gioco la non briscola che mi fa vincere il match con massimo punteggio
                //altrimenti gioco la briscola con massimo punteggio, e vinco io il match
                //se non posso vincere il match allora cerco di non far guadagnare punti all'avversario

                NeapolitanCard bestSameSuit = getBestRankedCardOfSuitInHandBetterRankedThan(cardOnSurface,NeapolitanCardSuit.getCardSuit(cardOnSurface.getCardSuit())); //getBestSameSuitBetterRankedThan(cardOnSurface); //valuto se posso vincere con la stessa suit
                NeapolitanCard bestBriscola = getBestRankedCardOfSuit(NeapolitanCardSuit.getCardSuit(config.getBriscolaSuit()));// getBestRankedBriscolaInHand();

                if (bestSameSuit != null) {
                    if (BriscolaCardPointsAndRankingRules.getPointValue(bestSameSuit.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta sempre di questo seme sempre migliore dell'avversario ma a punteggio nullo e rank minimo da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                        return findCardPositionInHand(getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(bestSameSuit));
                    else
                        return findCardPositionInHand(bestSameSuit); //altrimenti il rank dice univocamente chi ha miglior punteggio
                }
                else if(bestSameSuit == null && bestBriscola != null) {
                    if (BriscolaCardPointsAndRankingRules.getPointValue(bestBriscola.getCardNumber()) == 0) //se ne ho presa una con punteggio nullo, invece di buttare questa cerco un'altra carta sempre briscola sempre migliore dell'avversario ma a punteggio nullo e rank minimo da buttare (ok, non è proprio greedy scemo scemo, ma è ok)
                        return findCardPositionInHand(getWorstRankedCardInHandWithSamePointsAndSuitOfCardInHand(bestBriscola));
                    else
                        return findCardPositionInHand(bestBriscola); //altrimenti il rank dice univocamente chi ha miglior punteggio
                }
                else//se non ho di meglio della stessa suite, e non ho neanche una briscola, scelgo la carta peggiore che mi possa
                    return findCardPositionInHand(getWorstPointValuedCardInHand());

            }

        }else
            throw new IllegalStateException("No choice can be done in the current state");


    }












}
