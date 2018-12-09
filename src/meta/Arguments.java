package meta;

import java.util.Properties;

/**
 * Process command line arguments of the form "--argument=value".
 * Store arguments as in meta.Arguments object (derived from Properties).
 *
 */

public class Arguments extends Properties {
    public void setDefault(String arg, String value) {
        this.setProperty(arg, value);
    }
    public void loadArguments(String args[]) throws IllegalArgumentException {
        for(String argument : args) {
            if(!argument.startsWith("--")) {
                    throw new IllegalArgumentException("Argument does not start with \"--\"");
            }
			
            String[] keyValue = argument.substring(2).split("=", 2);
			
            if(keyValue.length != 2 || keyValue[1].length() < 1) {
                throw new IllegalArgumentException("Argument \"" +  keyValue[0] + "\"%s needs a value");
            }
            this.setProperty(keyValue[0], keyValue[1]);		
        }
    }

    public String get(String arg) {
        return this.getProperty(arg);
    }
}

