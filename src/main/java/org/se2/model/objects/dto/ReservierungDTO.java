package org.se2.model.objects.dto;

public class ReservierungDTO extends AbstractDTO{

    private int user_id;
    private int car_id;


    public ReservierungDTO(){}


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }
}
