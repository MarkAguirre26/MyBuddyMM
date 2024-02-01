package com.virtual.software.mybuddymm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean isImage1 = true;
    AppCompatImageButton btnReset;
    private Handler handler;
    ListItemMoneymanagement moneyManagement;
    boolean isDisabled = false;
    AppCompatButton btnWin, btnLose;
    HorizontalScrollView winAndLossResultHorizontalSccrollView;
    SharedPreferences preferences;
    List<ListItemMoneymanagement> MoneyManagementItemList = new ArrayList<>();
    private static final String PREF_NAME = "your_preference_name";
    private static final String LAST_CLEAR_DATE_KEY = "last_clear_date";
    private static final String GAME = "Game";
    private static final String Round = "Round";
    private static final String PROFIT = "Profit";
    private static final String WIN = "Win";
    private static final String LOSE = "Lose";

    private int game = 0;
    private int round = 0;
    private String profit = "0";
    private int win = 0;
    private int lose = 0;


    LinearLayout winAndLossLinearLayout;
    List<Double> betsList;
    List<String> winLoseList;
    TextView txtTitle, txtGame, txtRound, txtProfit, txtWinPercentage, txtWin, txtLose, txtNextBet, txtUnitOfBet, txtMessage;
    String selectedMM = "";
    double baseBetAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winAndLossLinearLayout = findViewById(R.id.winAndLossLinearLayout);
        winAndLossResultHorizontalSccrollView = findViewById(R.id.winAndLossResultHorizontalSccrollView);
        handler = new Handler(Looper.getMainLooper());

        txtTitle = findViewById(R.id.txtTitle);
        txtGame = findViewById(R.id.txtGame);
        txtRound = findViewById(R.id.txtRound);
        txtProfit = findViewById(R.id.txtProfit);
        txtWinPercentage = findViewById(R.id.txtWinPercentage);
        txtWin = findViewById(R.id.txtWin);
        txtLose = findViewById(R.id.txtLose);
        txtNextBet = findViewById(R.id.txtNextBet);
        txtUnitOfBet = findViewById(R.id.txtUnitOfBet);
        txtMessage = findViewById(R.id.txtMessage);
        btnWin = findViewById(R.id.btnWin);
        btnLose = findViewById(R.id.btnLose);
        btnReset = findViewById(R.id.btnReset);

        clearSharedPreferencesIfNeeded(this);

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String BaseUnitAmount = preferences.getString("BaseUnit", String.valueOf(baseBetAmount));
        selectedMM = preferences.getString("MoneyMangementName", MoneyManagement.OSCAR);
        game = preferences.getInt(GAME, 0);
        round = preferences.getInt(Round, 0);
        profit = preferences.getString(PROFIT, "0");
        win = preferences.getInt(WIN, 0);
        lose = preferences.getInt(LOSE, 0);

        baseBetAmount = Double.parseDouble(BaseUnitAmount);

        txtGame.setText(String.valueOf(game));
        txtRound.setText(String.valueOf(round));
        txtProfit.setText(profit);
        txtWin.setText(String.valueOf(win));
        txtLose.setText(String.valueOf(lose));


        proceedToReset();
    }

    private void setMoneyManagementTitle(String title) {
        txtTitle.setText(title);
    }

    public void Win_CLicked(View view) {
        setWinLoseList("W");
    }

    private void setWinLoseList(String r) {

        if (!isDisabled) {


            winLoseList.add(r);
            String lastResult = getLastString(winLoseList);


            if (lastResult.equals("W")) {

                // double newBetAmount = Double.parseDouble(String.valueOf(lastBetAmount).replace("-", "")) + baseBetAmount;

                betsList.add(roundToOneDecimalPlace(Double.parseDouble(txtNextBet.getText().toString())));

                double nextBetAmount = getLastDouble(betsList) + baseBetAmount;

                txtNextBet.setText(String.valueOf(roundToOneDecimalPlace(nextBetAmount)).replace("-", ""));
                createWinAndLossView();


            } else {

                betsList.add(Double.parseDouble("-" + txtNextBet.getText()));

                double lastBetAmount = getLastDouble(betsList);

                double refinedLastbetAmount = lastBetAmount;
                if (refinedLastbetAmount > 0) {
                    refinedLastbetAmount = -1 * lastBetAmount;
                }
                System.out.println("refinedLastbetAmount " + refinedLastbetAmount);

                txtNextBet.setText(String.valueOf(roundToOneDecimalPlace(refinedLastbetAmount)).replace("-", ""));
                createWinAndLossView();
            }

            //set the new value to views


            double unitOfBet = Double.parseDouble(txtNextBet.getText().toString()) / baseBetAmount;
            int intValue = (int) Math.round(unitOfBet);
            txtUnitOfBet.setText(String.valueOf(intValue));

            double total = sumOfDoubles(betsList);
            System.out.println("Total: " + total);
            if (total >= 0) {

                txtMessage.setText("You are now in " + roundToOneDecimalPlace(total) + " profit, I suggest to press the reset");
                txtMessage.setTextColor(getColor(R.color.win));
                setDisableButtons(true);

                win++;
                String p = txtProfit.getText().toString();
                double newProfit = (Double.parseDouble(p) + total);
                preferences.edit().putString(PROFIT, String.valueOf(roundToOneDecimalPlace(newProfit))).apply();
                txtProfit.setText(String.valueOf(roundToOneDecimalPlace(newProfit)));
                preferences.edit().putInt(WIN, win).apply();
                txtWin.setText(String.valueOf(win));


            } else {

                txtMessage.setText("You are in " + roundToOneDecimalPlace(total) + " as of the moment");
                txtMessage.setTextColor(getColor(R.color.Lose));
            }
            game++;
            txtGame.setText(String.valueOf(game));
            txtRound.setText(String.valueOf(round));
            preferences.edit().putInt(GAME, game).apply();
            preferences.edit().putInt(Round, round).apply();

        }
    }

    private void setDisableButtons(boolean b) {
        if (!b) {
            isDisabled = false;
            stopBlinking();
            btnWin.setBackgroundResource(R.drawable.button_win);
            btnLose.setBackgroundResource(R.drawable.button_lose);
        } else {
            startBlinking();
            isDisabled = true;
            btnWin.setBackgroundColor(getColor(R.color.disabled));
            btnLose.setBackgroundColor(getColor(R.color.disabled));

        }
    }

    private void resetAll() {
        double total = sumOfDoubles(betsList);

        if (winAndLossLinearLayout.getChildCount() > 0 && total <= -0.1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("You are still have " + roundToOneDecimalPlace(total) + " profit, Continue?");

            // Positive button (Yes)
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "Yes" button click
                    // You can add your code here to perform an action when "Yes" is clicked
                    dialog.dismiss(); // Close the dialog

                    lose++;
                    String p = txtProfit.getText().toString();
                    double total = sumOfDoubles(betsList);
                    double newProfit = Double.parseDouble(p) - Double.parseDouble(String.valueOf(total).replace("-", ""));
                    preferences.edit().putString(PROFIT, String.valueOf(roundToOneDecimalPlace(newProfit))).apply();
                    txtProfit.setText(String.valueOf(roundToOneDecimalPlace(newProfit)));
                    preferences.edit().putInt(LOSE, lose).apply();
                    txtLose.setText(String.valueOf(lose));

                    proceedToReset();
                }
            });

            // Negative button (No)
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the "No" button click
                    // You can add your code here to perform an action when "No" is clicked
                    dialog.dismiss(); // Close the dialog
                }
            });

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {


            proceedToReset();


        }


    }

    void proceedToReset() {


        betsList = new ArrayList<>();
        winLoseList = new ArrayList<>();

        setMoneyManagementTitle(selectedMM);
        txtMessage.setText("");

//        betsList.clear();
//        betsList.add(baseBetAmount);
        winLoseList.clear();

        winAndLossLinearLayout.removeAllViews();

        txtNextBet.setText(String.valueOf(baseBetAmount));

        double unitOfBet = Double.parseDouble(txtNextBet.getText().toString()) / baseBetAmount;
        int intValue = (int) Math.round(unitOfBet);
        txtUnitOfBet.setText(String.valueOf(intValue));

        round++;

        setDisableButtons(false);


    }

    public void Lose_Clicked(View view) {
        setWinLoseList("L");
    }

    public void reset_Clicked(View view) {
        resetAll();
    }

    private static String getLastString(List<String> stringList) {
        // Check if the list is not empty
        if (!stringList.isEmpty()) {
            // Get the last string using the size of the list
            return stringList.get(stringList.size() - 1);
        } else {
            // Handle the case where the list is empty
            return "List is empty";
        }
    }


    private static Double getLastDouble(List<Double> doubleList) {
        // Check if the list is not empty
        if (!doubleList.isEmpty()) {
            // Get the last double using the size of the list
            return doubleList.get(doubleList.size() - 1);
        } else {
            // Handle the case where the list is empty
            // You can customize this behavior based on your requirements
            return null; // or throw an exception, or return a default value, etc.
        }
    }

    public static double roundToOneDecimalPlace(double number) {
        // Create a DecimalFormat object with the pattern "#.#" to format to one decimal place
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        // Format the number using the DecimalFormat object
        String formattedNumber = decimalFormat.format(number);
        // Parse the formatted number back to a double
        return Double.parseDouble(formattedNumber);
    }

    private void createWinAndLossView() {

        winAndLossLinearLayout.removeAllViews();
        for (int i = 0; i < betsList.size(); i++) {


            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // width
                    60);
            layoutParams.setMargins(1, 1, 1, 1);
            textView.setLayoutParams(layoutParams);


            if (betsList.get(i) > 0) {
                textView.setBackground(getResources().getDrawable(R.drawable.win));
            } else {
                textView.setBackground(getResources().getDrawable(R.drawable.lose));
            }

            textView.setPadding(5, 0, 5, 0);
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setText(String.valueOf(roundToOneDecimalPlace(betsList.get(i))));
            textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource
            textView.setTypeface(null, android.graphics.Typeface.BOLD);

            winAndLossLinearLayout.addView(textView);
        }
        scrollToEnd();
    }

    private void scrollToEnd() {
        // Scroll to the end
        winAndLossResultHorizontalSccrollView.post(new Runnable() {
            @Override
            public void run() {
                winAndLossResultHorizontalSccrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    public static double sumOfDoubles(List<Double> doubleList) {
        double sum = 0.0;

        if (doubleList != null) {
            // Start the loop from the second element (index 1)
            for (double d : doubleList) {

                sum += d;
            }
        }


        return roundToOneDecimalPlace(sum);
    }

    public void clearSharedPreferencesIfNeeded(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Get the last clear date from SharedPreferences
        long lastClearTime = preferences.getLong(LAST_CLEAR_DATE_KEY, 0);

        // Get the current date
        long currentTime = System.currentTimeMillis();

        // Check if it's a new day
        if (!isSameDay(lastClearTime, currentTime)) {
            // Clear SharedPreferences


            preferences.edit().putString("BaseUnit", String.valueOf(baseBetAmount)).apply();
            preferences.edit().putString("MoneyMangementName", MoneyManagement.OSCAR).apply();
            preferences.edit().putInt(Round, 0).apply();
            preferences.edit().putInt(GAME, 0).apply();
            preferences.edit().putString(PROFIT, "0.0").apply();
            preferences.edit().putInt(WIN, 0).apply();
            preferences.edit().putInt(LOSE, 0).apply();

            // Save the current date as the last clear date
            preferences.edit().putLong(LAST_CLEAR_DATE_KEY, currentTime).apply();
        }
    }


    private void forceClean() {

        preferences.edit().putString("BaseUnit", String.valueOf(baseBetAmount)).apply();
        preferences.edit().putString("MoneyMangementName", MoneyManagement.OSCAR).apply();
        preferences.edit().putInt(Round, 0).apply();
        preferences.edit().putInt(GAME, 0).apply();
        preferences.edit().putString(PROFIT, "0.0").apply();
        preferences.edit().putInt(WIN, 0).apply();
        preferences.edit().putInt(LOSE, 0).apply();


        String BaseUnitAmount = preferences.getString("BaseUnit", String.valueOf(baseBetAmount));
        selectedMM = preferences.getString("MoneyMangementName", MoneyManagement.OSCAR);
        game = preferences.getInt(GAME, 0);
        round = preferences.getInt(Round, 0);
        profit = preferences.getString(PROFIT, "0");
        win = preferences.getInt(WIN, 0);
        lose = preferences.getInt(LOSE, 0);

        baseBetAmount = Double.parseDouble(BaseUnitAmount);

        txtNextBet.setText(String.valueOf(baseBetAmount));
        txtGame.setText(String.valueOf(game));
        txtRound.setText(String.valueOf(round));
        txtProfit.setText(profit);
        txtWin.setText(String.valueOf(win));
        txtLose.setText(String.valueOf(lose));
        proceedToReset();

        Toast.makeText(this, "Done force reset", Toast.LENGTH_SHORT).show();

    }

    private static boolean isSameDay(long time1, long time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String dateString1 = sdf.format(new Date(time1));
        String dateString2 = sdf.format(new Date(time2));
        return dateString1.equals(dateString2);
    }

    private void showCustomDialog() {

        MoneyManagementItemList = ListItemMoneymanagement.getMoneyManagementList();

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.setting_modal, null);

// Inflate the custom header layout
        View customHeader = inflater.inflate(R.layout.custom_dialog_header, null);


        ListView listView = dialogView.findViewById(R.id.listView);
        TextView txtBaseUnit = dialogView.findViewById(R.id.txtBaseUnit);


        txtBaseUnit.setText(String.valueOf(baseBetAmount));

        // Add more items as needed

        CustomAdapter adapter = new CustomAdapter(this, MoneyManagementItemList);
        listView.setAdapter(adapter);

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the item click
                moneyManagement = MoneyManagementItemList.get(position);
                boolean clickedItemState = !moneyManagement.isSelected();


                for (int i = 0; i < MoneyManagementItemList.size(); i++) {
                    ListItemMoneymanagement item = MoneyManagementItemList.get(i);

                    if (item.getFieldName().equals(moneyManagement.getFieldName())) {
                        MoneyManagementItemList.set(i, new ListItemMoneymanagement(item.getFieldName(), item.getDescription(), item.getUnitsRequired(), clickedItemState));
                    } else {
                        MoneyManagementItemList.set(i, new ListItemMoneymanagement(item.getFieldName(), item.getDescription(), item.getUnitsRequired(), false));
                    }
                }

                // For example, you can do something with the clicked item
                // (e.g., display a message, start a new activity, etc.)
                // Toast.makeText(MainActivity.this, "Clicked: " + clickedItem.getFieldName(), Toast.LENGTH_SHORT).show();
                adapter.setSelectedPosition(position);
            }
        });


        builder.setView(dialogView)
                .setCustomTitle(customHeader) // Set the custom header layout
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle form submission
                        baseBetAmount = Double.parseDouble(txtBaseUnit.getText().toString());
                        txtNextBet.setText(String.valueOf(baseBetAmount));


                        preferences.edit().putString("BaseUnit", String.valueOf(baseBetAmount)).apply();
                        preferences.edit().putString("MoneyMangementName", MoneyManagement.OSCAR).apply();
                        preferences.edit().putInt(Round, 0).apply();
                        preferences.edit().putInt(GAME, 0).apply();
                        preferences.edit().putString(PROFIT, "0.0").apply();
                        preferences.edit().putInt(WIN, 0).apply();
                        preferences.edit().putInt(LOSE, 0).apply();
                        if (moneyManagement != null) {
                            txtTitle.setText(moneyManagement.getFieldName());
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void setting_Clicked(View view) {
        showCustomDialog();

    }

    public void forceReset_Clicked(View view) {

        forceClean();
    }

    private void startBlinking() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                // Toggle visibility of the button
//                if (btnReset.getVisibility() == View.VISIBLE) {
//                    btnReset.setVisibility(View.INVISIBLE);
//                } else {
//                    btnReset.setVisibility(View.VISIBLE);
//                }
//
//                // Schedule the next run after a delay
//                handler.postDelayed(this, 500); // 500 milliseconds delay (adjust as needed)
                // Toggle between two different images
                if (isImage1) {
                    btnReset.setImageResource(R.drawable.reset_green); // Set your second image resource
                } else {
                    btnReset.setImageResource(R.drawable.reset_30); // Set your first image resource
                }

                isImage1 = !isImage1; // Toggle the flag

                // Schedule the next run after a delay
                handler.postDelayed(this, 500); // 500 milliseconds delay (adjust as needed)
            }
        }, 0); // Start immediately
    }

    private void stopBlinking() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the activity or perform any other action
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}