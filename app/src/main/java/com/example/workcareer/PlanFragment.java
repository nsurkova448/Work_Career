package com.example.workcareer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PlanFragment extends Fragment {

    private static final String CAREER_PLAN_ITEMS_KEY = "careerPlanItems";
    private static final String PROGRESS_VALUE_KEY = "progressValue";

    private EditText careerGoalEditText;
    private EditText stepsEditText;
    private Button savePlanButton;
    private Button editPlanButton;
    private Button deletePlanButton;
    private ListView planListView;
    private ArrayList<CareerPlanItem> careerPlanItems;
    private CareerPlanAdapter careerPlanAdapter;
    private ProgressBar progressBar;
    private TextView progressTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        careerGoalEditText = view.findViewById(R.id.career_goal_edit_text);
        stepsEditText = view.findViewById(R.id.steps_edit_text);
        savePlanButton = view.findViewById(R.id.save_plan_button);
        editPlanButton = view.findViewById(R.id.edit_plan_button);
        deletePlanButton = view.findViewById(R.id.delete_plan_button);
        planListView = view.findViewById(R.id.plan_list_view);
        progressBar = view.findViewById(R.id.progress_bar);
        progressTextView = view.findViewById(R.id.progress_text_view);

        careerPlanItems = LoadData.loadCareerPlanItems(getActivity());
        if (savedInstanceState != null) {
            careerPlanItems = savedInstanceState.getParcelableArrayList(CAREER_PLAN_ITEMS_KEY);
            int progressValue = savedInstanceState.getInt(PROGRESS_VALUE_KEY, 0);
            progressBar.setProgress(progressValue);
        } else {
            careerPlanItems = LoadData.loadCareerPlanItems(getActivity());
            if (careerPlanItems == null) {
                careerPlanItems = new ArrayList<>();
            }
        }

        careerPlanAdapter = new CareerPlanAdapter(getActivity(), careerPlanItems, this);
        planListView.setAdapter(careerPlanAdapter);

        savePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCareerPlan();
            }
        });

        editPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPlanDialog();
            }
        });

        deletePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePlanDialog();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Сохранение данных при переходе на другую активность
        SaveData.saveCareerPlanItems(getActivity(), careerPlanItems);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CAREER_PLAN_ITEMS_KEY, careerPlanItems);
        outState.putInt(PROGRESS_VALUE_KEY, progressBar.getProgress());
    }

    private void saveCareerPlan() {
        String careerGoal = careerGoalEditText.getText().toString().trim();
        String steps = stepsEditText.getText().toString().trim();

        if (careerGoal.isEmpty() || steps.isEmpty()) {
            Toast.makeText(getActivity(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        CareerPlanItem planItem = new CareerPlanItem(careerGoal, steps, null, false);
        careerPlanItems.add(planItem);
        careerPlanAdapter.notifyDataSetChanged();
        updateProgressBar();

        clearFields();
        Toast.makeText(getActivity(), "План карьерного роста сохранен", Toast.LENGTH_SHORT).show();
    }

    private void showEditPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Редактировать элемент плана");

        final ArrayList<String> items = new ArrayList<>();
        for (CareerPlanItem item : careerPlanItems) {
            items.add(item.getGoalText());
        }

        final CharSequence[] itemsArray = items.toArray(new CharSequence[0]);

        builder.setItems(itemsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CareerPlanItem item = careerPlanItems.get(which);
                String goal = item.getGoalText();
                String steps = item.getStepsText();

                showEditItemDialog(goal, steps, which);
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showDeletePlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Удалить элемент плана");

        final ArrayList<String> items = new ArrayList<>();
        for (CareerPlanItem item : careerPlanItems) {
            items.add(item.getGoalText());
        }

        final CharSequence[] itemsArray = items.toArray(new CharSequence[0]);

        builder.setItems(itemsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                careerPlanItems.remove(which);
                careerPlanAdapter.notifyDataSetChanged();
                updateProgressBar();
                Toast.makeText(getActivity(), "Элемент плана удален", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showEditItemDialog(final String goal, final String steps, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Редактировать элемент плана");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_plan_item, null);
        final EditText goalEditText = dialogView.findViewById(R.id.goal_edit_text);
        final EditText stepsEditText = dialogView.findViewById(R.id.steps_edit_text);

        goalEditText.setText(goal);
        stepsEditText.setText(steps);

        builder.setView(dialogView);

        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newGoal = goalEditText.getText().toString().trim();
                String newSteps = stepsEditText.getText().toString().trim();

                if (newGoal.isEmpty() || newSteps.isEmpty()) {
                    Toast.makeText(getActivity(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }
                CareerPlanItem item = careerPlanItems.get(position);
                item.setGoalAndSteps(newGoal, newSteps);
                careerPlanAdapter.notifyDataSetChanged();
                updateProgressBar();
                Toast.makeText(getActivity(), "Элемент плана обновлен", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void clearFields() {
        careerGoalEditText.setText("");
        stepsEditText.setText("");
    }

    private void updateProgressBar() {
        int totalSteps = 0;
        int completedSteps = 0;

        for (CareerPlanItem item : careerPlanItems) {
            totalSteps += item.getStepsArray().length;
            if (item.isChecked) {
                completedSteps += item.getStepsArray().length;
            }
        }

        int progress = totalSteps > 0 ? (completedSteps * 100) / totalSteps : 0;
        progressBar.setProgress(progress);
        progressBar.setSecondaryProgress(100);
        progressTextView.setText(progress + "%");
    }

    static class CareerPlanItem implements Parcelable {
        String careerGoal;
        String steps;
        String goal;
        boolean isChecked;

        CareerPlanItem(String careerGoal, String steps, String goal, boolean isChecked) {
            this.careerGoal = careerGoal;
            this.steps = steps;
            this.goal = goal;
            this.isChecked = isChecked;
        }

        protected CareerPlanItem(Parcel in) {
            careerGoal = in.readString();
            steps = in.readString();
            goal = in.readString();
            isChecked = in.readByte() != 0;
        }

        public static final Creator<CareerPlanItem> CREATOR = new Creator<CareerPlanItem>() {
            @Override
            public CareerPlanItem createFromParcel(Parcel in) {
                return new CareerPlanItem(in);
            }

            @Override
            public CareerPlanItem[] newArray(int size) {
                return new CareerPlanItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(careerGoal);
            dest.writeString(steps);
            dest.writeString(goal);
            dest.writeByte((byte) (isChecked ? 1 : 0));
        }

        public String getGoalText() {
            return careerGoal != null ? careerGoal : goal;
        }

        public String getStepsText() {
            return careerGoal != null ? steps : goal;
        }

        public String[] getStepsArray() {
            return getStepsText().split("\n");
        }

        public void setGoalAndSteps(String goal, String steps) {
            if (careerGoal != null) {
                this.careerGoal = goal;
                this.steps = steps;
            } else {
                this.goal = goal;
                this.goal = steps;
            }
        }

        public void setSteps(String steps) {
            if (careerGoal != null) {
                this.steps = steps;
            } else {
                this.goal = steps;
            }
        }
    }

    private static class CareerPlanAdapter extends ArrayAdapter<CareerPlanItem> {
        private PlanFragment fragment;
        private LayoutInflater inflater;

        CareerPlanAdapter(Context context, ArrayList<CareerPlanItem> items, PlanFragment fragment) {
            super(context, 0, items);
            this.fragment = fragment;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_career_plan, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final CareerPlanItem item = getItem(position);

            holder.goalTextView.setText(item.getGoalText());
            holder.stepsTextView.setText(item.getStepsText());
            holder.checkBox.setChecked(item.isChecked);

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.isChecked = isChecked;
                    item.setSteps(getUpdatedSteps(item, isChecked));
                    notifyDataSetChanged();
                    fragment.updateProgressBar();
                }
            });

            return convertView;
        }

        private String getUpdatedSteps(CareerPlanItem item, boolean isChecked) {
            String[] steps = item.getStepsArray();
            StringBuilder builder = new StringBuilder();

            for (String step : steps) {
                if (isChecked && !step.startsWith("✓ ")) {
                    builder.append("✓ ").append(step).append("\n");
                } else if (!isChecked && step.startsWith("✓ ")) {
                    builder.append(step.substring(2)).append("\n");
                } else {
                    builder.append(step).append("\n");
                }
            }

            return builder.toString().trim();
        }

        private static class ViewHolder {
            TextView goalTextView;
            TextView stepsTextView;
            CheckBox checkBox;

            ViewHolder(View view) {
                goalTextView = view.findViewById(R.id.goal_text_view);
                stepsTextView = view.findViewById(R.id.steps_text_view);
                checkBox = view.findViewById(R.id.checkbox);
            }
        }
    }
}