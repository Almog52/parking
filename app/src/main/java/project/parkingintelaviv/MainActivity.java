package project.parkingintelaviv;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity  {
    private static  final String TAG = "MainActivity";//cheking if the user can make map request
    private static final int ERROR_DIALOG_REQUEST = 9001;// if the user cant make map request do this arro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOk()){
            init();
        }

    }

    private void init(){
        Button mapButton = (Button) findViewById(R.id.mapbutton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(MainActivity.this,MapActivty.class);
                startActivity(intent);

            }
        });

    }


    public boolean isServicesOk() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {// the user can make map request
            Log.d(TAG, "everthingOK : Google play Service is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {//Ther is an error but we can fix it
            Log.d(TAG, "an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "YOU can't make map requests", Toast.LENGTH_SHORT).show(); // nothing we can do
        }
        return false;

    }


    public void findParking(View view) {
    }
}
