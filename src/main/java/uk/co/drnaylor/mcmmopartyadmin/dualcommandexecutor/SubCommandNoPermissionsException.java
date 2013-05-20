package uk.co.drnaylor.mcmmopartyadmin.dualcommandexecutor;

import java.util.List;

public class SubCommandNoPermissionsException extends Exception {
    
	final String errorMessage; 
    final List<String> permissions;
	
	public SubCommandNoPermissionsException(String errorMessage, List<String> permissions) {
		this.errorMessage = errorMessage;
        this.permissions = permissions;
	}
    
    public List<String> getRequiredPermissions() {
        return permissions;
    }
    
}
