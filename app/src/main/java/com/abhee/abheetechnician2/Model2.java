package com.abhee.abheetechnician2;

public class Model2 {
    private String Market;
    public Model2(String customerId) {
        this.Market=customerId;
    }

    public String getMarket() {
        return Market;
    }

    public void setMarket(String market) {
        Market = market;
    }
}
