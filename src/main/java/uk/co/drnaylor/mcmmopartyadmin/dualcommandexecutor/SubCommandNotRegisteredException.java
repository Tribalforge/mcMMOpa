package uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor;

public class SubCommandNotRegisteredException extends Exception {

    final String errorMessage;

    public SubCommandNotRegisteredException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
