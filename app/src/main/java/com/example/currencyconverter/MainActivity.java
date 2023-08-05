package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    TextView currencyTopTv,currencyBottomTv;
    TextView inputTv, outputTv;
    Boolean topSelected, bottomSelected;
    MaterialButton buttonCurrencyTop, buttonCurrencyBottom;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEqual;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC,buttonDot;
    double convertRateTopToBottom, convertRateBottomToTop;
    Map<String, Double> conversionRates;
    int MAX_INPUT_LENGTH = 54;
    String BASE_CURRENCY = "USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertRateTopToBottom = 1 / 3.6724;
        convertRateBottomToTop = 3.6724;

        conversionRates = getAllConversionRates();

        // Assign top and bottom text views
        currencyTopTv = findViewById(R.id.currency_top_tv);
        currencyBottomTv = findViewById(R.id.currency_bottom_tv);

        currencyTopTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean topTv = true;
                setInputTextView(topTv);
            }
        });

        currencyBottomTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean topTv = false;
                setInputTextView(topTv);
            }
        });

        // Assign currency changing buttons
        buttonCurrencyTop = findViewById(R.id.button_top_currency);
        buttonCurrencyBottom = findViewById(R.id.button_bottom_currency);
        buttonCurrencyTop.setOnClickListener(v -> {
            retrieveConversionRates();
        });
        buttonCurrencyBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveConversionRates();
            }
        });

        // Assign input buttons
        assignId(buttonC,R.id.button_c);
        assignId(buttonBrackOpen,R.id.button_open_bracket);
        assignId(buttonBrackClose,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonPlus,R.id.button_plus);
        assignId(buttonMinus,R.id.button_minus);
        assignId(buttonEqual,R.id.button_equal);
        assignId(button0,R.id.button_0);
        assignId(button1,R.id.button_1);
        assignId(button2,R.id.button_2);
        assignId(button3,R.id.button_3);
        assignId(button4,R.id.button_4);
        assignId(button5,R.id.button_5);
        assignId(button6,R.id.button_6);
        assignId(button7,R.id.button_7);
        assignId(button8,R.id.button_8);
        assignId(button9,R.id.button_9);
        assignId(buttonAC,R.id.button_ac);
        assignId(buttonDot,R.id.button_dot);

//        buttonC.setOnLongClickListener(view -> {
//            outputTv.setText(0);
//            inputTv.setText(0);
//            return true;
//        });

        setInputTextView(true);
    }

    /**
     * Assign a button to it's given id
     * @param btn: button object in main class
     * @param id: id from R.id.{id in xml file}
     */
    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    /**
     * Click handler, handles:
     *  - Changing to select top or bottom currency
     *  - Inputting numbers into text views
     *  - Clearing numbers from text views
     * @param view: button in the form of a view
     */
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String current_input = inputTv.getText().toString();

        // Change to top or bottom currency
        if (button.equals(buttonCurrencyTop) || button.equals(buttonCurrencyBottom)) {
            if (button.equals(buttonCurrencyTop)) {
                inputTv = currencyTopTv;
                outputTv = currencyBottomTv;
                topSelected = true;
                bottomSelected = false;
            } else {
                inputTv = currencyBottomTv;
                outputTv = currencyTopTv;
                topSelected = false;
                bottomSelected = true;
            }
//            inputTv.setTextColor(0xFF000000);
//            inputTv.setTextSize(50);
//            outputTv.setTextColor(0xFF575757);
//            outputTv.setTextSize(40);
        }
        // Output updating inputs
        else {
            // handle clears
            if (buttonText.equals("AC")) {
                // set top and bottom text views to '0'
                current_input = "0";
                currencyTopTv.setText("0");
                currencyBottomTv.setText("0");
            }
            else if (buttonText.equals("C")) {
                // remove 1 character from input text view
                if (current_input.length() == 1) {
                    current_input = "0";
                    inputTv.setText("0");
                } else {
                    current_input = current_input.substring(0, (current_input.length() - 1));
                    inputTv.setText(current_input);
                }
            }
            // Handle swap
            else if (buttonText.equals("S")) {
                // swap numbers but keep currently selected in same position
                if (bottomSelected) {
                    inputTv = currencyTopTv;
                    outputTv = currencyBottomTv;
                    topSelected = true;
                    bottomSelected = false;
                } else {
                    inputTv = currencyBottomTv;
                    outputTv = currencyTopTv;
                    topSelected = false;
                    bottomSelected = true;
                }
//                inputTv.setTextColor(0xFF000000);
//                inputTv.setTextSize(50);
//                outputTv.setTextColor(0xFF575757);
//                outputTv.setTextSize(40);
            } else {
                if (current_input.equals("0")) {
                    current_input = buttonText;
                    inputTv.setText(buttonText);
                } else {
                    current_input += buttonText;
                    inputTv.setText(current_input);
                }
            }

            // Arithmetic on current input
            String computed_input = current_input;

            // Update output tv

            if (topSelected) {
                outputTv.setText(convert(computed_input, convertRateTopToBottom));
            } else {
                outputTv.setText(convert(computed_input, convertRateBottomToTop));
            }
        }
    }

    /**
     * Sets the input to top text view if topTv == true, otherwise sets input
     * text view to bottom text view. Additionally sets new input text view to
     * size 50 font, black color, and output text view to size 40 font, and
     * grey color.
     */
    void setInputTextView(boolean topTv){
        if (topTv) {
            inputTv = currencyTopTv;
            outputTv = currencyBottomTv;
            topSelected = true;
            bottomSelected = false;
        } else {
            inputTv = currencyBottomTv;
            outputTv = currencyTopTv;
            topSelected = false;
            bottomSelected = true;
        }
        inputTv.setTextColor(0xFFFFFFFF);
//        inputTv.setTextSize(50);
        outputTv.setTextColor(0xFFBAB9B9);
//        outputTv.setTextSize(40);
    }

    /**
     * Read conversionRates.json and return a Map of the currency and the
     * conversion rate From USD -> Currency as a Double.
     *
     * @return
     */
    Map<String, Double> getAllConversionRates(){
        File file = new File("conversionRates.json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> map;
        try {
            map = mapper.readValue
                    (file, new TypeReference<Map<String, Double>>() {});
        } catch (Exception e) {
            System.out.println(e);
            map = new HashMap<String, Double>();
        }
        return map;
    }

    /**
     * TODO: Fully implement to store conversionRates
     * Currently retrieves conversion rates but does not store them.
     */
    void retrieveConversionRates(){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String baseUrl = "https://openexchangerates.org/api/latest.json?app_id=5bc0d18d478949309c7b5c9f550bbcce&base=" + BASE_CURRENCY;

        //Request a JSON object from baseUrl
        JsonObjectRequest jsonConversionRatesRequest = new JsonObjectRequest(baseUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Date dateRecvTime;
                        try {
                            Long timestamp = response.getLong("timestamp");
                            dateRecvTime = new Date(timestamp * 1000);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(MainActivity.this, "Updated Conversion Rates at: " + dateRecvTime.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error updating Conversion rates", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(jsonConversionRatesRequest);
    }

    /**
     * Convert an amount in 1 currency to new currency using rate given in
     * rate variable. Return is Converted amount as a numeric string with
     * up to 2 decimal places.
     *
     * @param AmountToConvert: amount to convert numeric string
     * @param rate: Conversion rate
     * @return String with converted amount and up to 2dp
     */
    String convert(String AmountToConvert, double rate) {
        // Currently assumes AmountToConvert string is numeric, will not work
        // on non numeric inputs
        // TODO: handle non numeric inputs
        BigDecimal amountInBD = new BigDecimal(AmountToConvert);
        BigDecimal result = amountInBD.multiply(BigDecimal.valueOf(rate));
        result.setScale(2, BigDecimal.ROUND_HALF_UP);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return df.format(result);
    }

    @Override
    public boolean onLongClick(View view) {
        currencyTopTv.setText("0");
        currencyBottomTv.setText("0");
        return true;
    }

//    String getResult(String data){
//        try{
//            Context context  = Context.enter();
//            context.setOptimizationLevel(-1);
//            Scriptable scriptable = context.initStandardObjects();
//            String finalResult =  context.evaluateString(scriptable,data,"Javascript",1,null).toString();
//            if(finalResult.endsWith(".0")){
//                finalResult = finalResult.replace(".0","");
//            }
//            return finalResult;
//        }catch (Exception e){
//            return "Err";
//        }
//    }
}
