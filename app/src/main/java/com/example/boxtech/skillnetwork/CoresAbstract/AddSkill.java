package com.example.boxtech.skillnetwork.CoresAbstract;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.MainMenuCore;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.R;

import java.util.ArrayList;

public abstract class AddSkill extends AppCompatActivity {


    protected App app;
    protected ProgressBar loadGamesProgressbar;
    protected ArrayList<SkillsModel> skillsModels = new ArrayList<SkillsModel>();


    private Button tourButton;
    private AutoCompleteTextView skillsEditText;


    private ListView listView;
    private ArrayList<String> skills = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;

    protected TextView noResultsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);

        app = App.getInstance();

        noResultsTextView = (TextView) findViewById(R.id.no_skills_message);
        tourButton = (Button) findViewById(R.id.tour_finish_button);
        skillsEditText = (AutoCompleteTextView) findViewById(R.id.add_skills);
        listView = (ListView) findViewById(R.id.rec_add_skill);
        loadGamesProgressbar = (ProgressBar) findViewById(R.id.load_skills_progressbar);
        loadGamesProgressbar.setVisibility(View.GONE);

        mAdapter = new ArrayAdapter<String>(this,
                R.layout.auto_complete_text_view_list_item, skills);

        listView.setAdapter(mAdapter);


        String status = getIntent().getStringExtra("CameFrom");

        if(status.equals("Login"))
        showtourDialog();

        tourButton.setVisibility(View.VISIBLE);
        tourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), MainMenuCore.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               // startActivity(intent);
                overridePendingTransition( R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
                addSkillToFirebase();

                finish();
            }
        });



        OnStartActivity();


        skillsEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String skill = (String)adapterView.getAdapter().getItem(i);

               SkillsModel skillsModel = app.getSkillManager().getSkillByName(skill);
               skills.add(skill);
               skillsModels.add(skillsModel);

                noResultsTextView.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();

                skillsEditText.setText("");
                skillsEditText.clearListSelection();


            }
        });

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                app.reloadSkills();
                skillsEditText.setAdapter(app.getSkillAdapter());
            }
        }.start();


    }



    protected ArrayList<SkillsModel> getSkills(){


        ArrayList<SkillsModel> skillsModelArrayList = new ArrayList<>();
        String[] skills = skillsEditText.getText().toString().split(",");

        for(String skill : skills)
        {
            SkillsModel skillsModel =  app.getSkillManager().getSkillByName(skill);

            if(skillsModel !=null)
            {
                skillsModelArrayList.add(skillsModel);
            }
        }

        return skillsModelArrayList;
    }


    protected void showtourDialog() {

        final Dialog tourDialog;
        tourDialog = new Dialog(AddSkill.this);
        tourDialog.setContentView(R.layout.tour_dialog);
        tourDialog.show();

        tourDialog.setCancelable(false);

        tourDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView greetingTextview , tourMessageTextview;
        Button addGamesButton , skipButton;




        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getResources().getAssets() ,"playregular.ttf");

        greetingTextview = (TextView) tourDialog.findViewById(R.id.tour_dialog_greeting) ;
        greetingTextview.setTypeface(playregular);

        tourMessageTextview = (TextView) tourDialog.findViewById(R.id.tour_message_dialog);
        tourMessageTextview.setTypeface(playregular);

        addGamesButton = ( Button) tourDialog.findViewById(R.id.add_games_tour_button);
        skipButton = ( Button) tourDialog.findViewById(R.id.skip_button_dialog);

        greetingTextview.setText("Hi");

        addGamesButton.setTypeface(sansation);
        skipButton.setTypeface(sansation);


        // Show the add game activity
        addGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourDialog.dismiss();
            }
        });



        // Skip the add game activity and go to the main app menu
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });




        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = tourDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


    protected abstract void OnStartActivity();
    protected abstract void addSkillToFirebase();

}
