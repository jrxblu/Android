package com.vacuum.app.plex.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vacuum.app.plex.MainActivity;
import com.vacuum.app.plex.Model.User;
import com.vacuum.app.plex.R;
import com.vacuum.app.plex.Utility.ApiClient;
import com.vacuum.app.plex.Utility.ApiInterface;
import com.vacuum.app.plex.Utility.SingleShotLocationProvider;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.vacuum.app.plex.Splash.SplashScreen.MY_PREFS_NAME;

public class EditProfile_Fragment extends Fragment implements View.OnClickListener {

    public final static String EDITPORFILE_FRAGMENT_TAG = "EDITPORFILE_FRAGMENT_TAG";
    private EditText full_name, email, password, phone;
    Button buttonRegister, determine_location, Date_of_Birth;
    Context mContext;
    static final Integer LOCATION = 0x1;
    String age, location, address;
    AnimationDrawable anim;
    String Details_MANUFACTURER;
    LottieAnimationView animation_view_fullname, animation_view_birth, animation_view_email, animation_view_password, animation_view_phone, animation_view_location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editprofile_fragment_layout, container, false);
        mContext = this.getActivity();
        full_name = view.findViewById(R.id.full_name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        phone = view.findViewById(R.id.phone);

        animation_view_fullname = view.findViewById(R.id.animation_view_fullname);
        animation_view_email = view.findViewById(R.id.animation_view_email);
        animation_view_password = view.findViewById(R.id.animation_view_password);
        animation_view_phone = view.findViewById(R.id.animation_view_phone);
        animation_view_location = view.findViewById(R.id.animation_view_location);
        animation_view_birth = view.findViewById(R.id.animation_view_birth);


        buttonRegister = view.findViewById(R.id.buttonRegister);
        determine_location = view.findViewById(R.id.determine_location);
        Date_of_Birth = view.findViewById(R.id.Date_of_Birth);

        buttonRegister.setOnClickListener(this);
        determine_location.setOnClickListener(this);
        Date_of_Birth.setOnClickListener(this);

        filled_field();
        getDetailsMANUFACTURER();
        validateFields2();
        return view;
    }

    private void filled_field() {
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        full_name.setText(prefs.getString("full_name",""));
        email.setText(prefs.getString("email",""));
        password.setText(prefs.getString("password",""));
        phone.setText(prefs.getString("phone",""));
        Date_of_Birth.setText(prefs.getString("age",""));

    }

    private void validateFields2() {
        full_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (full_name.getText().length() >= 6) {
                    animation_view_fullname.setAnimation(R.raw.success);
                    animation_view_fullname.playAnimation();
                } else {
                    animation_view_fullname.setProgress(0);
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (email.getText().toString().contains("@")) {
                    animation_view_email.setAnimation(R.raw.success);
                    animation_view_email.playAnimation();
                } else {
                    animation_view_email.setProgress(0);
                }
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().length() >= 8) {
                    animation_view_password.setAnimation(R.raw.success);
                    animation_view_password.playAnimation();
                } else {
                    animation_view_password.setProgress(0);
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phone.getText().length() >= 8) {
                    animation_view_phone.setAnimation(R.raw.success);
                    animation_view_phone.playAnimation();
                }
            }
        });
    }

    private void getDetailsMANUFACTURER() {
        Details_MANUFACTURER =
                "SERIAL: " + Build.SERIAL + "\n" +
                        "MODEL: " + Build.MODEL + "\n" +
                        "ID: " + Build.ID + "\n" +
                        "Manufacture: " + Build.MANUFACTURER + "\n" +
                        "Brand: " + Build.BRAND + "\n" +
                        "Type: " + Build.TYPE + "\n" +
                        "User: " + Build.USER + "\n" +
                        "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                        "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                        "SDK:  " + Build.VERSION.SDK + "\n" +
                        "BOARD: " + Build.BOARD + "\n" +
                        "BRAND: " + Build.BRAND + "\n" +
                        "HOST: " + Build.HOST + "\n" +
                        "FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                        "Version Code: " + Build.VERSION.RELEASE +
                        "Display : " + Build.DISPLAY;
        Log.e("TAG", Details_MANUFACTURER);
    }

    public void showDatePicker() {
        DatePickerDialog cc = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener, 1990,
                1, 1);
        cc.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Date_of_Birth.setText(view.getDayOfMonth() + " / " + (view.getMonth() + 1) + " / " + view.getYear());
                    age = view.getDayOfMonth() + " / " + (view.getMonth() + 1) + " / " + view.getYear();
                    animation_view_birth.setAnimation(R.raw.success);
                    animation_view_birth.playAnimation();
                }
            };

    private void insertUser() {
        String ROOT_URL = "https://mohamedebrahim.000webhostapp.com/";
        ApiInterface api = ApiClient.getClient(mContext, ROOT_URL).create(ApiInterface.class);
        api.registration(
                full_name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                phone.getText().toString(),
                2000,
                age,
                location,
                address,
                Details_MANUFACTURER
        ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User u = response.body();
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("full_name", full_name.getText().toString());
                    editor.putString("email", email.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.putString("phone", phone.getText().toString());
                    editor.putInt("points", u.getPoints());
                    editor.putString("age", age);
                    editor.putString("loation", location);
                    editor.putString("address", address);
                    editor.apply();
                    skipSplash();
                    Toast.makeText(mContext, "hi, " + full_name.getText().toString(), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(mContext, "unable to register", Toast.LENGTH_SHORT).show();
                Log.e("TAG", t.toString());
            }
        });
    }


    private void validateFields() {
        if (full_name.getText().length() <= 4) {
            full_name.setError("Error");
        } else if (email.getText().length() <= 5) {
            email.setError("Error");
        } else if (password.getText().length() == 0) {
            password.setError("Empty Field");
        } else if (phone.getText().length() <= 4) {
            phone.setError("Error");
        } else if (location == null) {
            Toast.makeText(mContext, "Determine location", Toast.LENGTH_SHORT).show();
            determine_location.setTextColor(Color.RED);
        } else {
            //insertUser();
            Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                validateFields();
                break;
            case R.id.determine_location:
                determine_location.setTextColor(Color.BLACK);
                determine_location.setText("Determining...");
                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
                break;
            case R.id.Date_of_Birth:
                showDatePicker();
                break;
        }
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{permission}, requestCode);
            }
        } else {
            getGPS();
        }
    }

    public void getGPS() {
        // when you need location
        // if inside activity context = this;
        SingleShotLocationProvider.requestSingleUpdate(mContext,determine_location,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location1) {
                        Log.e("TAG Location", "my location is " + location1.latitude + "//////" + location1.longitude);
                        try {
                            Geocoder geo = new Geocoder(mContext, Locale.getDefault());
                            List<Address> addresses = geo.getFromLocation(location1.latitude, location1.longitude, 1);
                            if (addresses.isEmpty()) {
                            } else {
                                if (addresses.size() > 0) {
                                    Log.e("TAG", addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                                    location = location1.latitude + "," + location1.longitude;
                                    address = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                                    age = "YYYY/MM/DD";
                                    determine_location.setText("Determine Location");
                                    animation_view_location.setVisibility(View.VISIBLE);
                                    animation_view_location.setAnimation(R.raw.success);
                                    animation_view_location.playAnimation();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void skipSplash() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    //Stopping animation:- stop the animation on onPause.
    @Override
    public void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
}