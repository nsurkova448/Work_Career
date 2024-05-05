// Класс LoadData (новый файл)
package com.example.workcareer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadData {
    private static final String CAREER_PLAN_ITEMS_KEY = "careerPlanItems";

    public static ArrayList<PlanFragment.CareerPlanItem> loadCareerPlanItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CareerPlanData", Context.MODE_PRIVATE);
        String careerPlanItemsJson = sharedPreferences.getString(CAREER_PLAN_ITEMS_KEY, null);

        if (careerPlanItemsJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PlanFragment.CareerPlanItem>>() {}.getType();
            return gson.fromJson(careerPlanItemsJson, type);
        }

        return null;
    }
}