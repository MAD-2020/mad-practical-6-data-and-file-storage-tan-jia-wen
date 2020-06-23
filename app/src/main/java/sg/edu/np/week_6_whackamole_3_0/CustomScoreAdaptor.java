package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    String username;
    ArrayList<Integer> levelsList;
    ArrayList<Integer> scoresList;

    public CustomScoreAdaptor(UserData userdata){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        username = userdata.getMyUserName();
        levelsList = userdata.getLevels();
        scoresList = userdata.getScores();
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layout is to be once the viewholder is created.
         */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main3, parent, false);
        CustomScoreViewHolder holder = new CustomScoreViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(final CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
        String levelNum = levelsList.get(position).toString();
        String scoreCount = scoresList.get(position).toString();

        holder.levels.setText("Level "+ levelNum);
        holder.highScores.setText("Score: " + scoreCount);

        Log.v(TAG, FILENAME + " Showing level " + levelsList.get(position) + " with highest score: " + scoresList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.levels.getContext(), Main4Activity.class);
                intent.putExtra("name", username);
                intent.putExtra("level", levelsList.get(position));
                Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + username);
                holder.levels.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return levelsList.size();
    }
}