package com.shedeurapps.mycalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    StringBuilder highlightText = new StringBuilder();
    float resultText = 0;
    AppCompatTextView highlight;
    AppCompatTextView result;
    AppCompatTextView memory_status;
    boolean operandTracker = false;
    String operationMemory;
    String operandLeft;
    String operandRight;
    StringBuilder packing = new StringBuilder();
    String temp = "";
    String inMemory = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get view from layout - numbers and decimal
        highlight = findViewById(R.id.highlight);
        result  = findViewById(R.id.result);
        memory_status = findViewById(R.id.memory_status);
        AppCompatButton clear = findViewById(R.id.clear);
        AppCompatButton decimal = findViewById(R.id.decimal);
        AppCompatButton zero = findViewById(R.id.zero);
        AppCompatButton one = findViewById(R.id.one);
        AppCompatButton two = findViewById(R.id.two);
        AppCompatButton three = findViewById(R.id.three);
        AppCompatButton four = findViewById(R.id.four);
        AppCompatButton five = findViewById(R.id.five);
        AppCompatButton six = findViewById(R.id.six);
        AppCompatButton seven = findViewById(R.id.seven);
        AppCompatButton eight = findViewById(R.id.eight);
        AppCompatButton nine = findViewById(R.id.nine);

// operations
        AppCompatButton add_to_memory = findViewById(R.id.add_to_memory);
        AppCompatButton recall_memory = findViewById(R.id.recall_memory);
        AppCompatButton clear_memory = findViewById(R.id.clear_memory);
        AppCompatButton subtract = findViewById(R.id.subtract);
        AppCompatButton add = findViewById(R.id.add);
        AppCompatButton equal = findViewById(R.id.equal);
        AppCompatButton exponent = findViewById(R.id.exponent);
        AppCompatButton percent = findViewById(R.id.percent);
        AppCompatButton divide = findViewById(R.id.divide);
        AppCompatButton multiple = findViewById(R.id.multiply);
        AppCompatButton negate = findViewById(R.id.negate);
        AppCompatImageView delete = findViewById(R.id.delete);

// add listeners - numbers
        zero.setOnClickListener(v -> {
            showHighlightText(getString(R.string.zero));
            packing.append(0);
        });
        one.setOnClickListener(v -> {
            showHighlightText(getString(R.string.one));
            packing.append(1);
        });
        two.setOnClickListener(v -> {
            showHighlightText(getString(R.string.two));
            packing.append(2);
        });
        three.setOnClickListener(v -> {
            showHighlightText(getString(R.string.three));
            packing.append(3);
        });
        four.setOnClickListener(v -> {
            showHighlightText(getString(R.string.four));
            packing.append(4);
        });
        five.setOnClickListener(v -> {
            showHighlightText(getString(R.string.five));
            packing.append(5);
        });
        six.setOnClickListener(v -> {
            showHighlightText(getString(R.string.six));
            packing.append(6);
        });
        seven.setOnClickListener(v -> {
            showHighlightText(getString(R.string.seven));
            packing.append(7);
        });
        eight.setOnClickListener(v -> {
            showHighlightText(getString(R.string.eight));
            packing.append(8);
        });
        nine.setOnClickListener(v -> {
            showHighlightText(getString(R.string.nine));
            packing.append(9);
        });
        decimal.setOnClickListener(v -> {   // do nothing if part of string contains decimal already
            showHighlightText(getString(R.string.decimal));
            packing.append(".");
        });
        negate.setOnClickListener(v -> {   // negate a number if its the only one
            if(!operandTracker) {
                String tem = highlightText.toString();
                highlightText.delete(0, highlightText.length());        // clear display

                if(packing.length() > 0) {  // something was typed already
                    packing.insert(0, getString(R.string.subtract));    // push it in front
                    showHighlightText(getString(R.string.subtract) + tem);
                }
                else {  // there's nothing there
                    // check if there is results displayed that is not zero
                    if(temp.length() > 0) {
                        packing.delete(0, packing.length());        // clear the packing
                        if(Float.parseFloat(temp) < 0) {            // check if it is already a negative #
                            packing.append(temp);
                            showHighlightText(temp);
                        }
                        else if(Float.parseFloat(temp) == 0) {
                            packing.append(getString(R.string.subtract));
                            showHighlightText(getString(R.string.subtract));
                        }
                        else {
                            packing.append(getString(R.string.subtract));
                            packing.append(temp);
                            showHighlightText(getString(R.string.subtract) + temp);
                        }
                    }
                    else {
                        packing.append(getString(R.string.subtract));
                        showHighlightText(getString(R.string.subtract));
                    }
                }

            }
        });
        delete.setOnClickListener(v -> {
            if(packing.length() > 0) {
                packing.delete(packing.length() - 1, packing.length());
                deleteHighlightText();
                Log.v("MyCalc2", "one");
            }
            else if(operandTracker) {
                deleteHighlightText();
                Log.v("MyCalc2", "two");
                operandTracker = false;
            }
            else if(operandLeft != null) {
                if (operandLeft.length() > 0) {     // work on operand on the left
                    operandLeft = operandLeft.substring(0, operandLeft.length() - 1);
                    deleteHighlightText();
                    Log.v("MyCalc2", "three");
                }
            }
        });

        // listeners operations
        add_to_memory.setOnClickListener(v -> {
            if(temp.length() > 0) {
                inMemory = temp;
                memory_status.setText(getString(R.string.m));
            }
        });
        recall_memory.setOnClickListener(v -> {
            if(inMemory.length() > 0) {
                showHighlightText(inMemory);
                packing.append(inMemory);
            }
        });
        clear_memory.setOnClickListener(v -> {
            memory_status.setText("");
            inMemory = "";
        });

        // basic operations
        add.setOnClickListener(v -> operate(getString(R.string.add)));
        subtract.setOnClickListener(v -> operate(getString(R.string.subtract)));
        divide.setOnClickListener(v -> operate(getString(R.string.divide)));
        multiple.setOnClickListener(v -> operate(getString(R.string.multiply)));
        percent.setOnClickListener(v -> {
            if(packing.length() > 0) {
                operandTracker = true;      // skip checks and calculate
                operandLeft = packing.toString();
                operationMemory = getString(R.string.percent);
                operate(getString(R.string.percent));
            }
        });
        exponent.setOnClickListener(v -> operate(getString(R.string.caret)));
        equal.setOnClickListener(v -> {
            if(operandTracker)
                calculate();
        });
        clear.setOnClickListener(v -> {
            resultText = 0;     // result the result
            showResultText("0");
            operandTracker = false;
            packing.delete(0, packing.length());
            operandLeft = "";
            operandRight = "";
            temp = "";
            highlightText.delete(0, highlightText.length());
            clearPacking();
            highlightText.append(getString(R.string.highlight));    // default text
            highlight.setText(highlightText.toString());
        });
    }

    protected void showHighlightText(String value) {
        if(highlightText.toString().equalsIgnoreCase(getString(R.string.highlight))) {
            highlightText.delete(0, highlightText.length());    // clear it before showing anything else
        }
        highlightText.append(value);
        highlight.setText(highlightText.toString());
    }

    protected void deleteHighlightText() {
        if(!highlightText.toString().equalsIgnoreCase(getString(R.string.highlight)) &&
            highlightText.length() > 0) {
            String trimmed = highlightText.toString().trim();
            highlightText.delete(0, highlightText.length());

            highlightText.append(trimmed);
            highlightText.delete(highlightText.length() - 1, highlightText.length());    // clear one to the right
            highlight.setText(highlightText.toString());
        }
    }

    protected void showResultText(String text) {
        if(text.length() > 8)
            result.setTextSize(40f);
        else
            result.setTextSize(70f);
        result.setText(text);
    }

    protected void clearPacking() {
        packing.delete(0, packing.length());
    }

    protected void operate(String operation) {
        if(packing.length() > 0) {
            if(!operandTracker) {       // no operand yet - store left operand and add operation and wait for right operand
                operandLeft = packing.toString();     // store left operand
                clearPacking();
                showHighlightText(" " + operation + " ");
                operationMemory = operation;
                operandTracker = true;
            }
            else {  // an operation is pending
                if(packing.length() > 0) {  // right content is already provided
                    // here also an operation following a previous operation
                    calculate();        // run the pending and get ready for the next
                    highlightText.delete(0, highlightText.length());    // clear screen
                    highlightText.append(temp);
                    operationMemory = operation;
                    operandLeft = temp;
                    showHighlightText(" " + operation + " ");
                    operandTracker = true;
                }
            }
        }
        else if(temp != null && temp.length() > 0) { // if there is a temp from previous result
            operandLeft = temp;
            highlightText.delete(0, highlightText.length());    // clear screen
            highlightText.append(temp);
            operationMemory = operation;
            showHighlightText(" " + operation + " ");
            operandTracker = true;
        }
        else {      // probably switching operator
            if(operandLeft != null && operandLeft.length() > 0 && operationMemory != null &&
                    operationMemory.length() > 0) {
                highlightText.delete(0, highlightText.length());    // clear screen
                highlightText.append(operandLeft);
                operationMemory = operation;
                showHighlightText(" " + operation + " ");
                operandTracker = true;
            }
        }
    }

    protected void calculate() {
        if(operandLeft != null && operandLeft.length() > 0) {  // be both sides are to be provided
            if(packing.length() > 0) {      // second is provided
                operandLeft = operandLeft.trim();
                operandRight = packing.toString();
                operandRight = operandRight.trim();
                if(operandLeft.equalsIgnoreCase(getString(R.string.decimal)))
                    operandLeft = "0.0";
                if(operandRight.equalsIgnoreCase(getString(R.string.decimal)))
                    operandRight = "0.0";
                if (operationMemory.equalsIgnoreCase(getString(R.string.add))) {
                    resultText = Float.parseFloat(operandLeft) + Float.parseFloat(operandRight);
                } else if (operationMemory.equalsIgnoreCase(getString(R.string.subtract))) {
                    resultText = Float.parseFloat(operandLeft) - Float.parseFloat(operandRight);
                } else if (operationMemory.equalsIgnoreCase(getString(R.string.divide))) {
                    if (!operandRight.equalsIgnoreCase(getString(R.string.zero))) {
                        resultText = Float.parseFloat(operandLeft) / Float.parseFloat(operandRight);
                    } else {
                        highlightText.delete(0, highlightText.length());
                        showHighlightText("err: dividing by zero");
                    }
                } else if (operationMemory.equalsIgnoreCase(getString(R.string.multiply))) {
                    resultText = Float.parseFloat(operandLeft) * Float.parseFloat(operandRight);
                } else if (operationMemory.equalsIgnoreCase(getString(R.string.percent))) {
                    resultText = Float.parseFloat(operandLeft) / 100;
                } else if (operationMemory.equalsIgnoreCase(getString(R.string.caret))) {
                    resultText = (float) Math.pow(Float.parseFloat(operandLeft), Float.parseFloat(operandRight));
                }
            }
            // remove decimal point for whole numbers
            String[] explode = String.valueOf(resultText).split("\\.", 2);

            if(explode[1].equalsIgnoreCase(getString(R.string.zero))) {       // not a floating point number
                int res = (int)resultText;
                showResultText(String.valueOf(res));
                temp = String.valueOf(res);     // copy result into temp just in case an operation is used after computing
            }
            else {
                showResultText(String.valueOf(resultText));
                temp = String.valueOf(resultText);
            }

            // reset everything
            highlightText.delete(0, highlightText.length());
            clearPacking();
            resultText = 0;
            operandTracker = false;
        }
    }
}