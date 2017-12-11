package it.ma.polimi.briscola.rest.client.dto;

/**
 * Created by utente on 09/12/17.
 */

public class ErrorDTO {
    private String error;
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
