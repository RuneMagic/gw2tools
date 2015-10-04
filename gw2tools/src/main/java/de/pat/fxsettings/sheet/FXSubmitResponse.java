package de.pat.fxsettings.sheet;

public class FXSubmitResponse
{

    private final boolean success;
    private final String failResponse;

    public FXSubmitResponse(boolean success)
    {
        this.success = success;
        this.failResponse = null;
    }

    public FXSubmitResponse(boolean success, String failResponse)
    {
        this.success = success;
        this.failResponse = failResponse;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getFailResponse()
    {
        return failResponse;
    }

}
