package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

@Entity(tableName = "conversion_rate_table")
public class ConversionRate {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private Date date;

    @NonNull
    private String base;

    @NonNull
    private String rates;

    public ConversionRate(@NonNull Date date, @NonNull String rates) {
        this.date = date;
        this.rates = rates;
        this.base = "USD";
    }

    public ConversionRate(@NonNull Date date, @NonNull String rates, @NonNull String base) {
        this.date = date;
        this.rates = rates;
        this.base = base;
    }

    /**
     * takes rate attribute and turns it into a JSONObject before returning it.
     * @return JSONObject of this.rates
     */
    public JSONObject getRates() {
        JSONObject jsonRates = new JSONObject();
        try {
            jsonRates = new JSONObject(this.rates);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonRates;
    }

    public Date getDate() { return this.date; }

    public String getBase() { return this.base; }

    public int getId() { return this.id; }
}
