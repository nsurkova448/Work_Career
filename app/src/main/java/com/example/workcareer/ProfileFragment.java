package com.example.workcareer;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final String SHARED_PREFS_NAME = "profile_prefs";

    private CircleImageView imageView;
    private EditText usernameEditText, workExperienceEditText, educationEditText, skillsEditText, achievementsEditText;
    private Button saveButton;
    private ImageView sign_out;

    private Uri selectedImageUri;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.add_a_photo);
        imageView.setOnClickListener(v -> showImageOptionsDialog());

        usernameEditText = view.findViewById(R.id.username);
        workExperienceEditText = view.findViewById(R.id.work_experience);
        educationEditText = view.findViewById(R.id.education);
        skillsEditText = view.findViewById(R.id.skills);
        achievementsEditText = view.findViewById(R.id.achievements);
        saveButton = view.findViewById(R.id.save_button);
        sign_out = view.findViewById(R.id.sign_out);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Выход из аккаунта");
                builder.setMessage("Вы уверены, что хотите выйти из аккаунта?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finishAffinity();
                    }
                });

                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        saveButton.setOnClickListener( v -> saveProfileData() );
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            loadProfileData();
        }

        if (savedInstanceState != null) {
            selectedImageUri = Uri.parse( savedInstanceState.getString( "selected_image_uri" ) );
            if (selectedImageUri != null) {
                imageView.setImageURI( selectedImageUri );
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState ( @NonNull Bundle outState ) {
        super.onSaveInstanceState( outState );
        if (selectedImageUri != null) {
            outState.putString( "selected_image_uri", selectedImageUri.toString() );
        }
    }

    private void loadProfileData () {
        SharedPreferences prefs = requireActivity().getSharedPreferences( SHARED_PREFS_NAME, 0 );
        usernameEditText.setText( prefs.getString( "username", "" ) );
        workExperienceEditText.setText( prefs.getString( "work_experience", "" ) );
        educationEditText.setText( prefs.getString( "education", "" ) );
        skillsEditText.setText( prefs.getString( "skills", "" ) );
        achievementsEditText.setText( prefs.getString( "achievements", "" ) );

        String imageUriString = prefs.getString( "image_uri", null );
        if (imageUriString != null) {
            selectedImageUri = Uri.parse( imageUriString );
            imageView.setImageURI( selectedImageUri );
        }
    }

    private void saveProfileData () {
        SharedPreferences prefs = requireActivity().getSharedPreferences( SHARED_PREFS_NAME, 0 );
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( "username", usernameEditText.getText().toString() );
        editor.putString( "work_experience", workExperienceEditText.getText().toString() );
        editor.putString( "education", educationEditText.getText().toString() );
        editor.putString( "skills", skillsEditText.getText().toString() );
        editor.putString( "achievements", achievementsEditText.getText().toString() );
        if (selectedImageUri != null) {
            editor.putString( "image_uri", selectedImageUri.toString() );
        }
        editor.apply();
    }

    private void showImageOptionsDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder( requireContext() );

        if (selectedImageUri == null) {
            builder.setTitle( "Фотография" )
                    .setItems( new CharSequence[]{"Загрузить с устройства", "Сделать снимок"}, ( dialogInterface, i ) -> {
                        if (i == 0) {
                            openGallery();
                        } else {
                            takePhoto();
                        }
                    } );
        } else {
            builder.setTitle( "Опции фото" )
                    .setItems( new CharSequence[]{"Открыть фото", "Изменить фото", "Удалить фото"}, ( dialogInterface, i ) -> {
                        if (i == 0) {
                            openFullScreenImage();
                        } else if (i == 1) {
                            openGallery();
                        } else {
                            deleteImage();
                        }
                    } );
        }

        builder.create().show();
    }

    private void openFullScreenImage () {
        Intent intent = new Intent( Intent.ACTION_VIEW );
        intent.setDataAndType( selectedImageUri, "image/*" );
        startActivity( intent );
    }

    private void openGallery () {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, PICK_IMAGE_REQUEST );
    }

    private void takePhoto () {
        if (ContextCompat.checkSelfPermission( requireContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION );
        } else {
            Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
            startActivityForResult( intent, TAKE_PHOTO_REQUEST );
        }
    }

    private void deleteImage () {
        selectedImageUri = null;
        imageView.setImageResource( R.drawable.group_7 );
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                imageView.setImageURI( selectedImageUri );
            } else if (requestCode == TAKE_PHOTO_REQUEST && data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get( "data" );
                imageView.setImageBitmap( bitmap );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadProfileData();
            } else {
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {

            }
        }
    }
}