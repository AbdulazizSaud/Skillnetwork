package com.example.boxtech.skillnetwork.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boxtech.skillnetwork.Adapters.CommonAdapter;
import com.example.boxtech.skillnetwork.Adapters.ViewHolders;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.Request.CreateRequestStep1Core;
import com.example.boxtech.skillnetwork.Cores.SearchCore;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.DURING_STATUS;


public  abstract class RequestsFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



    private RecyclerView mRecyclerView;
    private ImageView bgChatImageView;

    private RecyclerView.LayoutManager mLayoutManager;

    protected HashMap<String, RequestModel> requestModelHashMap = new HashMap<>();
    protected ArrayList<RequestModel> requestUserLists = new ArrayList<RequestModel>();
    protected RecyclerView.Adapter mAdapter;

    protected FloatingActionButton createRequestBtn;
    protected FloatingActionButton searchRequestBtn;



    protected App app;




    public RequestsFragment() {
        app = App.getInstance();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_requests, container, false);
        setupRecyclerView(v);


        bgChatImageView = (ImageView) v.findViewById(R.id.splash);
        createRequestBtn = (FloatingActionButton)v.findViewById(R.id.create_request_btn);
        searchRequestBtn = (FloatingActionButton)v.findViewById(R.id.search_request_btn);





        if(app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_CUSTOMER))
            createRequestBtn.setVisibility(View.VISIBLE);
        else if (app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_FREELANCER))
            searchRequestBtn.setVisibility(View.VISIBLE);
        else
        {
        }



        createRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCreateRequest();
            }
        });
        searchRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSearch();
            }
        });
        OnStartActivity();
        return v;
    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
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

                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showOnLongClickDialog(model);
                        return false;
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
//                Log.i("->",""+getAllUnseenMessages());



                if(app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_FREELANCER))
                {

                    if(model.equals(DURING_STATUS)) {
                        ArrayList<FreelancersModel> freelancers = new ArrayList<FreelancersModel>(model.getFreelancers().values());
                        for(FreelancersModel freelancersModel : freelancers) {

                            if(app.getUserInformation().getUID().equals(freelancersModel.getUid()) && freelancersModel.getBid().isStatus()) {
                                requestHolder.setResource(R.drawable.ic_done_24dp);
                                break;
                            } else if (freelancersModel.getBid().isStatus()){
                                requestHolder.setResource(R.drawable.ic_info_24dp);
                            }
                        }
                    } else {
                        requestHolder.setResource(R.drawable.ic_update_24dp);
                    }
                } else {

                    if(model.equals(DURING_STATUS))
                        requestHolder.setResource(R.drawable.ic_done_24dp);
                    else
                    requestHolder.setResource(0);
                }

            }




        };

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);




    }


    protected void showOnLongClickDialog(final RequestModel RequestModel) {

        final Dialog onLongClickGameDialog;
        onLongClickGameDialog = new Dialog(getContext());
        onLongClickGameDialog.setContentView(R.layout.chat_long_click_pop_up);
        onLongClickGameDialog.show();


        onLongClickGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView verificationDeleteText;
        Button deleteYesButton, deleteNoButton;


        verificationDeleteText = (TextView) onLongClickGameDialog.findViewById(R.id.delete_verification_text);
        deleteYesButton = (Button) onLongClickGameDialog.findViewById(R.id.delete_chat_yes_button);
        deleteNoButton = (Button) onLongClickGameDialog.findViewById(R.id.delete_chat_no_button);


        Typeface playregular = Typeface.createFromAsset(getResources().getAssets() ,"playregular.ttf");
        Typeface sansation = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        deleteYesButton.setTypeface(sansation);
        deleteNoButton.setTypeface(sansation);
        verificationDeleteText.setTypeface(playregular);


        // Delete chat
        deleteYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChatFromlist(RequestModel);
                onLongClickGameDialog.dismiss();
            }
        });


        // Remove Dialog
        deleteNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongClickGameDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = onLongClickGameDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


    protected RequestModel addRequest(RequestModel requestModel) {



        if(!hasRequest(requestModel.getRequestId())) {
            requestUserLists.add(requestModel);
            requestModelHashMap.put(requestModel.getRequestId(), requestModel);

            if(!app.getSkillManager().checkIfHasSkillById(requestModel.getRequestId()))
            {
             app.addRequest(requestModel);
            }

            mAdapter.notifyDataSetChanged();
        } else{


            int index=-1;
            for(int i=0;i<requestUserLists.size();i++)
            {
                if(requestUserLists.get(i).getRequestId().equals(requestModel.getRequestId()))
                {
                    index = i;
                    break;
                }
            }

            if(index!=-1)
            {
                requestUserLists.set(index,requestModel);
            }

        }
        // Community BG declaration imageview
        if (mAdapter.getItemCount() < 1)
            bgChatImageView.setVisibility(View.VISIBLE);
        else
            bgChatImageView.setVisibility(View.INVISIBLE);


        return requestModel;
    }


    private void toCreateRequest()
    {
        Intent i = new Intent(getActivity(), CreateRequestStep1Core.class);
        startActivity(i);
    }

    private void toSearch()
    {
        Intent i = new Intent(getActivity(), SearchCore.class);
        startActivity(i);
    }


    public boolean hasRequest(String key)
    {
        return requestModelHashMap.containsKey(key);
    }


    protected abstract void removeChatFromlist(RequestModel model);
    protected abstract void OnStartActivity();
    protected abstract void OnClickHolders(RequestModel model, View v);

    protected abstract void toRequest();
}
