package uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor;

public class SubCommandException extends Exception {
    
	private final String errorMessage;
    private final DualSubCommandInterface subCommand;
	
	public SubCommandException(String errorMessage, DualSubCommandInterface subCommand) {
		this.errorMessage = errorMessage;
        this.subCommand = subCommand;
	}
    
    public DualSubCommandInterface getSubCommand() {
        return subCommand;
    }
    
}
