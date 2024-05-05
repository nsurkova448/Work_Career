package com.example.workcareer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SaveData {
    private static final String CAREER_PLAN_ITEMS_KEY = "careerPlanItems";

    public static void saveCareerPlanItems(Context context, ArrayList<PlanFragment.CareerPlanItem> careerPlanItems) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CareerPlanData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String careerPlanItemsJson = gson.toJson(careerPlanItems);
        editor.putString(CAREER_PLAN_ITEMS_KEY, careerPlanItemsJson);
        editor.apply();
    }
}