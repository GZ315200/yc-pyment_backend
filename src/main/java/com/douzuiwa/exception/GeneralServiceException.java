package com.douzuiwa.exception;

/**
 * Created by Gyges on 2017/6/4.
 */
public class GeneralServiceException  extends RuntimeException{
    private String message;

    public GeneralServiceException(String message){
        this.message = message;
    }

    public GeneralServiceException(String message,Throwable s){
        this.message = message;
        s.getCause();
    }

    public GeneralServiceException(Throwable e){
        e.printStackTrace();
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
