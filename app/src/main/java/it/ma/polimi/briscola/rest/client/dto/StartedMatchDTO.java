package it.ma.polimi.briscola.rest.client.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utente on 09/12/17.
 */

public class StartedMatchDTO {
    private String game;
    @SerializedName("last_card")
    private String lastCard;
    private String cards;
    @SerializedName("your_turn")
    private boolean yourTurn;
    private String url;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getLastCard() {
        return lastCard;
    }

    public void setLastCard(String lastCard) {
        this.lastCard = lastCard;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
