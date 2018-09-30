package com.example.boxtech.skillnetwork.CoresAbstract;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.boxtech.skillnetwork.Adapters.CommonAdapter;
import com.example.boxtech.skillnetwork.Adapters.ViewHolders;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Search extends AppCompatActivity {



    protected App app;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    private ImageView bgChatImageView;


    protected HashMap<String, RequestModel> requestModelHashMap = new HashMap<>();
    protected ArrayList<RequestModel> requestUserLists = new ArrayList<RequestModel>();
    protected RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        app = App.getInstance();
        setContentView(R.layout.activity_search);

        bgChatImageView = (ImageView) findViewById(R.id.splash_search);

        setupRecyclerView();

        OnStartActivity();



    }


    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.search_results_recyclerview);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mAdapter =   new CommonAdapter<RequestModel>(requestUserLists, R.layout.new_user_request_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.RequestHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final RequestModel model, int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.RequestHolder requestHolder = (ViewHolders.RequestHolder) holder;


                // Community BG declaration imageview
                if (mAdapter.getItemCount() < 1)
                    bgChatImageView.setVisibility(View.VISIBLE);
                else
                    bgChatImageView.setVisibility(View.INVISIBLE);


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v);
                    }
                });


                // Capitalize Title letters
                String chatTitle = model.getRequestTitle();

                if (chatTitle == null)
                    return;

                String capitlizedChatTitle = chatTitle.substring(0, 1).toUpperCase() + chatTitle.substring(1);

                if (chatTitle.contains(" ")) {
                    // Capitalize game title letters
                    String cpWord = "";
                    for (int i = 0; i < capitlizedChatTitle.length(); i++) {
                        if (capitlizedChatTitle.charAt(i) == 32 && capitlizedChatTitle.charAt(i + 1) != 32) {
                            cpWord = capitlizedChatTitle.substring(i + 1, i + 2).toUpperCase() + capitlizedChatTitle.substring(i + 2);
                            capitlizedChatTitle = capitlizedChatTitle.replace(capitlizedChatTitle.charAt(i + 1), cpWord.charAt(0));
                        }
                    }
                    holder.setTitle(capitlizedChatTitle);
                } else {
                    holder.setTitle(capitlizedChatTitle);
                }


                requestHolder.setDescriptionSubtitle(model.getRequestDescription());
                requestHolder.setBudget(String.valueOf(model.getRequestBudget())+""+model.getRequestCurrency());


                String skills = "";

                ArrayList<SkillsModel> skillsModels =  model.getSkillsModels();

                for(SkillsModel skill : skillsModels)
                    skills+=skill.getSkillName()+",";

                requestHolder.setSkills(skills);



                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));

                new HandlerCondition(new CallbackHandlerCondition() {
                    @Override
                    public boolean callBack() {

                        holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
                        return false;
                    }
                }, 20000);


            }


        };

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    protected abstract void OnStartActivity();
    protected abstract void OnClickHolders(RequestModel model, View v);
}
