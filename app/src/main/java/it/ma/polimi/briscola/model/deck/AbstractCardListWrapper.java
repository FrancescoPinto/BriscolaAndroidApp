package it.ma.polimi.briscola.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class providing a default implementation of CardListWrapper interface. It wraps a cardList and provides some convenience mehods to manipulate it.
 * Descendants of this class can either have a maximum number of allowed cards or can contain any number of cards. This is resolved at runtime invoking the getMaxNumCardsAllowedInList overridden by its descendents. By default returns null (i.e. number of cards not limited)
 * Concrete Descendants must provide a buildCardInstance method implementation, so as to allow this class to properly instantiate CARD objects.
 *
 * @param <CARD> the type parameter, extends Card
 * @author Francesco Pinto
 */
public abstract class AbstractCardListWrapper <CARD extends Card> implements CardListWrapper<CARD>{


    //identifiers of error messages
    private final String illegalConstructorArgumentErrorMessage_TOO_LONG = "illegalConstructorArgumentErrorMessage_TOO_LONG", noMoreCardsCanBeAddedErrorMessage= "noMoreCardsCanBeAddedErrorMessage",
    operationNotPermittedErrorMessage_EMPTY_LIST = "operationNotPermittedErrorMessage_EMPTY_LIST", indexListSizeExceededErrorMessage = "indexListSizeExceededErrorMessage", indexListMaximumExceededErrorMessage = "indexListMaximumExceededErrorMessage", nonNullArgument = "nonNullArgument",
            tooLongArgument = "tooLongArgument";

    /**
     * The Card list.
     */
    private List<CARD> cardList;

    /**
     * Instantiates a new Abstract card list wrapper, only implemented to allow descendants to have their own default constructor.
     */
    public AbstractCardListWrapper(){
        cardList = new ArrayList<>();
    }

    /**
     * Instantiates a new Abstract card list wrapper from the string passed as an argument.
     *
     *  @param list The string representing the card list with the convention that each card is represented by a card number followed by the card suit. The leftmost card represents the lowest-indexed card in the list.
     *  @throws IllegalArgumentException If the number of cards contained in the argument is greater than the max number of allowed cards
     */
    public AbstractCardListWrapper(String list){

        if(getMaxNumCardsAllowedInList() != null && list.length() > getMaxNumCardsAllowedInList()*2) //if the descendant specified a maxNumCardsAllowedInList then check if the argument satisfies that boundary
            throw new IllegalArgumentException(getErrorMessage(illegalConstructorArgumentErrorMessage_TOO_LONG)); //if not, notify the system with an exception

        List<CARD> temp = new ArrayList<>(); //else, build the list

        for(int i = 0; i <= list.length()-1; i = i+2){ //parse the string, each pair of chars represents a card
            String num = ""+list.charAt(i);
            String suit = ""+list.charAt(i+1);
            temp.add(this.buildCardInstance(num,suit)); //build the card, and append it to the list
        }

        this.cardList = temp;
    }

    /**
     * Instantiates a new Abstract card list wrapper from the list of cards passed as an argument.
     *
     * @param cards The list of cards to be wrapped.
     * @throws IllegalArgumentException If the number of cards contained in the argument is greater than the max number of allowed cards
     */
    public AbstractCardListWrapper(List<CARD> cards){

        if(getMaxNumCardsAllowedInList() != null &&  cards.size() > getMaxNumCardsAllowedInList()) //if the descendant specified a maxNumCardsAllowedInList then check if the argument satisfies that boundary
            throw new IllegalArgumentException(getErrorMessage(illegalConstructorArgumentErrorMessage_TOO_LONG));//if not, notify the system with an exception
        this.cardList = cards;
    }

    @Override
    public boolean equalTo(CardListWrapper<CARD> clw){
        if(this.getCardList().isEmpty() && clw.getCardList().size() == 0) //if both are empty, return true
            return true;
        else if(this.getCardList().isEmpty() && !(clw.getCardList().size() == 0))
            return false;
        else if(!this.getCardList().isEmpty() && (clw.getCardList().size() == 0))
            return false;
        else if(this.size() != clw.size())
            return false;
        else {
            for (int i = 0; i < getCardList().size() && i < clw.getCardList().size(); i++) {
                CARD c1 = cardList.get(i);
                CARD c2 = clw.getCardList().get(i);
                if (!c1.equalTo(c2))
                    return false;
            }
            return true;

        }
    }


    @Override
    public String toString(){
        String temp = "";
        if(isEmpty())
            return temp;

        for(CARD c: getCardList()){

            temp += c;
        }
        return temp;
    }

    @Override
    public int size(){
        return getCardList().size();
    }


    @Override
    public boolean isEmpty(){
        return getCardList().isEmpty();
    }


    @Override
    public boolean containsCard(CARD card){
        for(CARD c : cardList)
            if(c.equalTo(card))
                return true;

        return false;
    }

    @Override
    public CARD getCard(int i){
        if(isEmpty())
            throw new IllegalArgumentException(getErrorMessage(operationNotPermittedErrorMessage_EMPTY_LIST));

        if(getMaxNumCardsAllowedInList() != null && i > getMaxNumCardsAllowedInList()-1)
            throw new IndexOutOfBoundsException(getErrorMessage(indexListMaximumExceededErrorMessage));

        if(i >= size() || i < 0 )
            throw new IndexOutOfBoundsException(getErrorMessage(indexListSizeExceededErrorMessage));


        return getCardList().get(i);
    }

    @Override
    public void appendCard(CARD card){
        if(card == null)
            throw new IllegalArgumentException(nonNullArgument);

        if(getMaxNumCardsAllowedInList() != null && this.size() >= getMaxNumCardsAllowedInList())
            throw new IllegalStateException(getErrorMessage(noMoreCardsCanBeAddedErrorMessage));
        getCardList().add(card);
    }

    @Override
    public CARD removeCard(int i){
        if(isEmpty())
            throw new IllegalArgumentException(getErrorMessage(operationNotPermittedErrorMessage_EMPTY_LIST));

        if(getMaxNumCardsAllowedInList() != null && i > getMaxNumCardsAllowedInList()-1)
            throw new IndexOutOfBoundsException(getErrorMessage(indexListMaximumExceededErrorMessage));

        if(i >= size() || i < 0 )
            throw new IndexOutOfBoundsException(getErrorMessage(indexListSizeExceededErrorMessage));


        CARD temp = getCardList().get(i);
        getCardList().remove(i);
        return temp;
    }

    @Override
    public void appendAll(List<CARD> cards){
        if(getMaxNumCardsAllowedInList() != null && this.size() + cards.size() > getMaxNumCardsAllowedInList())
            throw new IllegalArgumentException(tooLongArgument);
        getCardList().addAll(cards);}

    @Override
    public List<CARD> getCardList(){
        return cardList;
    }
    @Override
    public void setCardList(List<CARD> cards){
        if(getMaxNumCardsAllowedInList() != null && cards.size() > getMaxNumCardsAllowedInList())
            throw new IllegalArgumentException(tooLongArgument);
        this.cardList = new ArrayList<>(cards);
    }

    @Override
    public List<CARD> clearCardList(){
        List<CARD> temp = new ArrayList<>(cardList);
        cardList.clear();
        return temp;
    }


    @Override
    public Integer getMaxNumCardsAllowedInList() {
        return null;
    }

    /**
     * Abstract method used to get an instance of the generic type CARD based on two Strings representing the number and the suit (respectively) of the desired card
     *
     * @param num  String representing the number of the card, its value must be among the cardNumber values in the CARD associated CardNumber enum (name convention CARDCardNumber, e.g. NeapolitanCardNumber)
     * @param suit String representing the suit of the card, its value must be among the cardSuit values in the CARD associated CardSuit enum (name convention CARDCardSuit, e.g. NeapolitanCardSuit)
     * @return A CARD object having the desired number and suit
     */
    public abstract CARD buildCardInstance(String num, String suit);

    //Method created because if the message errors were initialized as members, it wouldn't have been possible to get the subclass name to complete the message.
    private String getErrorMessage(String errorName){
        switch(errorName){
            case "illegalConstructorArgumentErrorMessage_TOO_LONG": return this.getClass().getSimpleName() + ": the number of cards in the argument of the constructor should be between 0 and "+getMaxNumCardsAllowedInList();
            case "noMoreCardsCanBeAddedErrorMessage" : return this.getClass().getSimpleName() + " already contains "+getMaxNumCardsAllowedInList()+"cards. No other cards can be added";
            case "operationNotPermittedErrorMessage_EMPTY_LIST" : return this.getClass().getSimpleName() + " is empty, operation not permitted";
            case "indexListSizeExceededErrorMessage" : return this.getClass().getSimpleName() + ": index can not exceed the current bounds of the list";
            case "indexListMaximumExceededErrorMessage" : return this.getClass().getSimpleName() +": index can not exceed the maximum of "+ (getMaxNumCardsAllowedInList() - 1);
            case "nonNullArgument": return this.getClass().getSimpleName() + ": non null argument should be used";
            case "tooLongArgument": return this.getClass().getSimpleName() + ": adding all those cards to the list will make it overflow! maximum size exceeded!";
            default: return null;
        }

    }
}
