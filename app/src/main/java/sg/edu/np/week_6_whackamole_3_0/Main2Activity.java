package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");
         */
        Button buttonCreate = findViewById(R.id.buttonCreate);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        final EditText enterCreateUsername = findViewById(R.id.enterCreateUsername);
        final EditText enterCreatePassword = findViewById(R.id.enterCreatePassword);
        Log.v(TAG, FILENAME + "New user creation with: " + enterCreateUsername.getText().toString() + "|" + enterCreatePassword.getText().toString());

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> levelList = new ArrayList<>();
                ArrayList<Integer> scoreList = new ArrayList<>();
                UserData userData = dbHandler.findUser(enterCreateUsername.getText().toString());

                if (userData != null){
                    Toast.makeText(Main2Activity.this, "User already exists. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + "User already exists during new user creation!");
                }
                else{
                    userData.setMyUserName(enterCreateUsername.getText().toString());
                    userData.setMyPassword(enterCreatePassword.getText().toString());
                    for (int i = 1; i < 11; i++){
                        levelList.add(i);
                        scoreList.add(0);
                    }
                    userData.setLevels(levelList);
                    userData.setScores(scoreList);
                    dbHandler.addUser(userData);
                    Toast.makeText(Main2Activity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
