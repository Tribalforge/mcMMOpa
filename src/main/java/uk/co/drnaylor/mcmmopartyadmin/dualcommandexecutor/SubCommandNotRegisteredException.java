package uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor;

public class SubCommandNotRegisteredException extends Exception {

    private final String errorMessage;

    public SubCommandNotRegisteredException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
