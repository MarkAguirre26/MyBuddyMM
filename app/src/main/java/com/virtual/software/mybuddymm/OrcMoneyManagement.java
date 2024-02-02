package com.virtual.software.mybuddymm;

import java.util.ArrayList;
import java.util.List;

public class OrcMoneyManagement {
    public static String[] levels = {"Pos1", "Base", "Step1", "Step2", "Step3", "Step4", "Step5"};
    private static String[] positions = levels;
    private static List<BetAmountModel> amounts = new ArrayList<>();

    public static final int POS1 = 0;
    public static final int BASE = 1;
    public static final int STEP1 = 2;
    public static final int STEP2 = 3;
    public static final int STEP3 = 4;
    public static final int STEP4 = 5;
    public static final int STEP5 = 6;

    public static void setBetAmount(double baseBetAmount) {
        amounts.add(new BetAmountModel(positions[0], toTwoDecimal(baseBetAmount * 2)));
        amounts.add(new BetAmountModel(positions[1], toTwoDecimal(baseBetAmount)));
        amounts.add(new BetAmountModel(positions[2], toTwoDecimal(baseBetAmount * 2)));
        amounts.add(new BetAmountModel(positions[3], toTwoDecimal(baseBetAmount)));
        amounts.add(new BetAmountModel(positions[4], toTwoDecimal(baseBetAmount * 2)));
        amounts.add(new BetAmountModel(positions[5], toTwoDecimal(baseBetAmount * 4 - baseBetAmount)));
        amounts.add(new BetAmountModel(positions[6], toTwoDecimal(baseBetAmount * 4 - baseBetAmount)));
    }

    public static double getBetAmount(String keyword) {
        return amounts.stream().filter(item -> item.level.equals(keyword)).findFirst().orElse(new BetAmountModel("", 0.0)).amount;
    }

    private static double toTwoDecimal(double input) {
        return Double.parseDouble(String.format("%.2f", input));
    }

    public static List<BetAmountModel> getBetAmountList() {
        return amounts;
    }
}

class BetAmountModel {
    String level;
    double amount;

    public BetAmountModel(String level, double amount) {
        this.level = level;
        this.amount = amount;
    }
}




