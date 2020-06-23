package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    private int advancedScore = 0;
    private String selectedLevel;
    private String username;
    CountDownTimer readyCountdown;
    TextView showScore;
    MyDBHandler dbHandler;
    boolean lvl2 = false;
    final Toast[] mtoast = {null};
    ArrayList<Button> buttonList = new ArrayList<Button>();

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyCountdown = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/1000);
                if (mtoast[0] != null) {
                    mtoast[0].cancel();
                }
                mtoast[0] = Toast.makeText(getApplicationContext(), "Get Ready In " + millisUntilFinished/1000 + " seconds", Toast.LENGTH_SHORT);
                mtoast[0].show();
            }

            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                if (mtoast[0] != null) {
                    mtoast[0].cancel();
                }
                mtoast[0] = Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT);
                mtoast[0].show();
                placeMoleTimer();
                lvl2 = true;
            }
        };
        readyCountdown.start();
    }

    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int levelSpeed = (11- Integer.parseInt(selectedLevel));
        newMolePlaceTimer = new CountDownTimer(levelSpeed, levelSpeed) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (Integer.parseInt(selectedLevel) < 5){
                    setNewMole();
                }
                else
                    setTwoMoles();
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer .start();
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent receiveData = getIntent();
        selectedLevel = receiveData.getStringExtra("Level");
        username = receiveData.getStringExtra("Username");
        advancedScore = receiveData.getIntExtra("Score", 10);

        showScore = findViewById(R.id.score);
        Log.v(TAG, "Current User Score: " + showScore);

        dbHandler = new MyDBHandler(this, null, null, 1);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button buttons = findViewById(id);
            buttonList.add(buttons);
            buttons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button buttonCheck = findViewById(v.getId());
                    doCheck(buttonCheck);
                    newMolePlaceTimer.cancel();

                    if (Integer.parseInt(selectedLevel) < 5){
                        setNewMole();
                    }
                    else
                        setTwoMoles();
                    newMolePlaceTimer.start();
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
        if (Integer.parseInt(selectedLevel) < 5){
            setNewMole();
        }
        else
            setTwoMoles();
    }

    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if (checkButton.getText() == "*"){
            advancedScore += 1;
        }
        else {
            advancedScore -= 1;
        }
        showScore.setText("" + advancedScore);
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        for (int i=0; i<BUTTON_IDS.length; i++){
            Button b = (Button) findViewById(BUTTON_IDS[i]);
            if (i != randomLocation){
                b.setText("O");
            }
            else
                b.setText("*");
        }
    }

    public void setTwoMoles()
    {
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        int randomLocation2 = ran.nextInt(9);
        if (randomLocation == randomLocation2){
            setTwoMoles();
        }
        else{
            for (int i=0; i<BUTTON_IDS.length; i++){
                Button c = (Button) findViewById(BUTTON_IDS[i]);
                if (i != randomLocation || i != randomLocation2){
                    c.setText("O");
                }
                else
                    c.setText("*");
            }
        }
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
     Log.v(TAG, FILENAME + ": Update User Score...");
     newMolePlaceTimer.cancel();
     readyTimer.cancel();
     UserData userData = dbHandler.findUser(username);
     int thisLevel = userData.getLevels().get(Integer.parseInt(selectedLevel)-1);
     int highscore = userData.getScores().get(thisLevel);
     String currentScore = String.valueOf(advancedScore);
    }
}
