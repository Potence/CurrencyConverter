package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Scriptable;


import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView currencyTopTv,currencyBottomTv;
    TextView inputTv, outputTv;
    Boolean topSelected, bottomSelected;
    MaterialButton buttonCurrencyTop, buttonCurrencyBottom;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonSwap;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC,buttonDot;

    double convertRateTopToBottom, getConvertRateBottomToTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertRateTopToBottom = 1 / 3.6724;
        getConvertRateBottomToTop = 3.6724;

        // Assign top and bottom text views
        currencyTopTv = findViewById(R.id.currency_top_tv);
        currencyBottomTv = findViewById(R.id.currency_bottom_tv);

        inputTv = currencyTopTv;
        outputTv = currencyBottomTv;
        topSelected = true;
        bottomSelected = false;

        // Assign currency select buttons
        assignId(buttonCurrencyTop, R.id.button_top_currency);
        assignId(buttonCurrencyBottom, R.id.button_bottom_currency);

        // Assign input buttons
        assignId(buttonC,R.id.button_c);
        assignId(buttonBrackOpen,R.id.button_open_bracket);
        assignId(buttonBrackClose,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonPlus,R.id.button_plus);
        assignId(buttonMinus,R.id.button_minus);
        assignId(buttonSwap,R.id.button_swap);
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

    }

    /**
     * Assign a button to it's given id
     * @param btn: button object in main class
     * @param id: id from R.id.{id in xml file}
     */
    void assignId(MaterialButton btn,int id){
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
        MaterialButton button =(MaterialButton) view;
        String buttonText = button.getText().toString();
        String current_input = inputTv.getText().toString();

        // Change to top or bottom currency
        if (button.equals(buttonCurrencyTop) || button.equals(buttonCurrencyBottom)) {
            if (button.equals(buttonCurrencyTop)){
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
        }
        // handle clears
        else if (buttonText.equals("AC")){
            // set top and bottom text views to '0'
            current_input = "0";
            currencyTopTv.setText("0");
            currencyBottomTv.setText("0");
        }
        else if (buttonText.equals("C")){
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
        else if (buttonText.equals("S")){
            // swap numbers but keep currently selected in same position

        }
        else {
            if (current_input.equals("0")){
                current_input = buttonText;
                inputTv.setText(buttonText);
            } else {
                current_input += buttonText;
                inputTv.setText(current_input);
            }
        }

        if (topSelected) {
            outputTv.setText(convert(current_input, convertRateTopToBottom));
        } else {
            outputTv.setText(convert(current_input, getConvertRateBottomToTop));
        }
    }

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