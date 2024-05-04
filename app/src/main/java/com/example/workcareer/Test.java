package com.example.workcareer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
public class Test extends Fragment {

    private EditText careerGoalEditText;
    private EditText skillsEditText;
    private EditText certificationEditText;
    private Button savePlanButton;
    private Button editPlanButton;
    private Button updatePlanButton;
    private ListView planListView;
    private ArrayList<CareerPlanItem> careerPlanItems;
    private CareerPlanAdapter careerPlanAdapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_test, container, false );

        careerGoalEditText = view.findViewById( R.id.career_goal_edit_text );
        skillsEditText = view.findViewById( R.id.skills_edit_text );
        certificationEditText = view.findViewById( R.id.certification_edit_text );
        savePlanButton = view.findViewById( R.id.save_plan_button );
        editPlanButton = view.findViewById( R.id.edit_plan_button );
        updatePlanButton = view.findViewById( R.id.update_plan_button );
        planListView = view.findViewById( R.id.plan_list_view );
        progressBar = view.findViewById( R.id.progress_bar );

        careerPlanItems = new ArrayList<>();
        careerPlanAdapter = new CareerPlanAdapter( getActivity(), careerPlanItems );
        planListView.setAdapter( careerPlanAdapter );

        savePlanButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                saveCareerPlan();
            }
        } );

        editPlanButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                showEditPlanDialog();
            }
        } );

        updatePlanButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                showUpdatePlanDialog();
            }
        } );

        return view;
    }

    private void saveCareerPlan () {
        String careerGoal = careerGoalEditText.getText().toString().trim();
        String skills = skillsEditText.getText().toString().trim();
        String certifications = certificationEditText.getText().toString().trim();

        CareerPlanItem planItem = new CareerPlanItem( careerGoal, skills, certifications );
        careerPlanItems.add( planItem );
        careerPlanAdapter.notifyDataSetChanged();
        updateProgressBar();

        clearFields();
        Toast.makeText( getActivity(), "План карьерного роста сохранен", Toast.LENGTH_SHORT ).show();
    }

    private void showEditPlanDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setTitle( "Редактировать элемент плана" );

        View dialogView = getLayoutInflater().inflate( R.layout.dialog_edit_plan_item, null );
        final EditText goalEditText = dialogView.findViewById( R.id.goal_edit_text );
        final EditText stepsEditText = dialogView.findViewById( R.id.steps_edit_text );
        final EditText resourcesEditText = dialogView.findViewById( R.id.resources_edit_text );
        final DatePicker datePicker = dialogView.findViewById( R.id.date_picker );

        builder.setView( dialogView );

        builder.setPositiveButton( "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                String goal = goalEditText.getText().toString().trim();
                String steps = stepsEditText.getText().toString().trim();
                String resources = resourcesEditText.getText().toString().trim();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                CareerPlanItem planItem = new CareerPlanItem( goal, steps, resources, year, month, day );
                careerPlanItems.add( planItem );
                careerPlanAdapter.notifyDataSetChanged();
                updateProgressBar();
            }
        } );

        builder.setNegativeButton( "Отмена", null );

        builder.show();
    }

    private void showUpdatePlanDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setTitle( "Обновить план карьерного роста" );

        View dialogView = getLayoutInflater().inflate( R.layout.dialog_update_plan, null );
        final EditText careerGoalEditText = dialogView.findViewById( R.id.career_goal_edit_text );
        final EditText skillsEditText = dialogView.findViewById( R.id.skills_edit_text );
        final EditText certificationEditText = dialogView.findViewById( R.id.certification_edit_text );

        builder.setView( dialogView );

        builder.setPositiveButton( "Обновить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                String newCareerGoal = careerGoalEditText.getText().toString().trim();
                String newSkills = skillsEditText.getText().toString().trim();
                String newCertifications = certificationEditText.getText().toString().trim();

                careerPlanItems.clear();
                CareerPlanItem planItem = new CareerPlanItem( newCareerGoal, newSkills, newCertifications );
                careerPlanItems.add( planItem );
                careerPlanAdapter.notifyDataSetChanged();
                updateProgressBar();

                clearFields();
                Toast.makeText( getActivity(), "План карьерного роста обновлен", Toast.LENGTH_SHORT ).show();
            }
        } );

        builder.setNegativeButton( "Отмена", null );

        builder.show();
    }

    private void clearFields () {
        careerGoalEditText.setText( "" );
        skillsEditText.setText( "" );
        certificationEditText.setText( "" );
    }

    private void updateProgressBar () {
        int totalSteps = 0;
        int completedSteps = 0;

        for (CareerPlanItem item : careerPlanItems) {
            if (item.goal != null) {
                totalSteps += item.steps.split( "\n" ).length;
                for (String step : item.steps.split( "\n" )) {
                    if (step.startsWith( "✓ " )) {
                        completedSteps++;
                    }
                }
            }
        }

        int progress = totalSteps > 0 ? (completedSteps * 100) / totalSteps : 0;
        progressBar.setProgress( progress );
    }

    private static class CareerPlanItem {
        String careerGoal;
        String skills;
        String certifications;
        String goal;
        String steps;
        String resources;
        int year;
        int month;
        int day;

        CareerPlanItem ( String careerGoal, String skills, String certifications ) {
            this.careerGoal = careerGoal;
            this.skills = skills;
            this.certifications = certifications;
        }

        CareerPlanItem ( String goal, String steps, String resources, int year, int month, int day ) {
            this.goal = goal;
            this.steps = steps;
            this.resources = resources;
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    private static class CareerPlanAdapter extends ArrayAdapter<CareerPlanItem> {
        CareerPlanAdapter ( Context context, ArrayList<CareerPlanItem> items ) {
            super( context, 0, items );
        }

        @NonNull
        @Override
        public View getView ( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
            if (convertView == null) {
                convertView = LayoutInflater.from( getContext() ).inflate( R.layout.item_career_plan, parent, false );
            }

            CareerPlanItem item = getItem( position );

            TextView goalTextView = convertView.findViewById( R.id.goal_text_view );
            TextView stepsTextView = convertView.findViewById( R.id.steps_text_view );
            TextView resourcesTextView = convertView.findViewById( R.id.resources_text_view );
            TextView dateTextView = convertView.findViewById( R.id.date_text_view );

            if (item.careerGoal != null) {
                goalTextView.setText( item.careerGoal );
                stepsTextView.setText( item.skills );
                resourcesTextView.setText( item.certifications );
                dateTextView.setVisibility( View.GONE );
            } else {
                goalTextView.setText( item.goal );
                stepsTextView.setText( item.steps );
                resourcesTextView.setText( item.resources );
                dateTextView.setText( String.format( "%04d-%02d-%02d", item.year, item.month + 1, item.day ) );
            }

            return convertView;
        }
    }
}