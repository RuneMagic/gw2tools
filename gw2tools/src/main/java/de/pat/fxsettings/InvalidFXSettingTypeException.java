package de.pat.fxsettings;

public class InvalidFXSettingTypeException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = -2989473852506017727L;

    public InvalidFXSettingTypeException() {}

    public InvalidFXSettingTypeException(String message)
    {
        super(message);
    }
}
