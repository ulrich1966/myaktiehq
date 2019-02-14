package com.example.myaktiehq;

public class AktieModel {
    private String symbol;
    private String name;
    private String currency;
    private String exchange;
    private String price;
    private String date;
    private String time;
    private String change;
    private String percent;
    private String open;
    private String high;
    private String low;
    private String volume;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "AktieModel{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", exchange='" + exchange + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", change='" + change + '\'' +
                ", percent='" + percent + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
