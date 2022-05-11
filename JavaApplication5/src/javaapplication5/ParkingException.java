package javaapplication5;

public class ParkingException extends Exception{
    public ParkingException(String msg) {
        super(msg);
    }

    ParkingException() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
