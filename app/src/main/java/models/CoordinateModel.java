package models;

/**
 * Created by Michał on 2015-05-09.
 */
public class CoordinateModel {
    private double latitude;
    private double longitude;
    private String nameCity;

    public CoordinateModel(){

    }

    public CoordinateModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }
}


