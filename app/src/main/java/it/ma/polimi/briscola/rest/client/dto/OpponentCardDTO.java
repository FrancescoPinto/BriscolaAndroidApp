package it.ma.polimi.briscola.rest.client.dto;

/**
 * Created by utente on 09/12/17.
 */

public class OpponentCardDTO {
    private String opponent;
    private String card;

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
