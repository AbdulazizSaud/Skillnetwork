package com.example.boxtech.skillnetwork.Fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.boxtech.skillnetwork.Adapters.CommonAdapter;
import com.example.boxtech.skillnetwork.Adapters.ViewHolders;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.ACCOUNT_TYPE_CUSTOMER;
import static com.example.boxtech.skillnetwork.Interfaces.Constants.WATTING_STATUS;


public abstract class FreelancerTab extends Fragment {


    private MaterialBetterSpinner searchPrioritySpinner;
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ArrayList<FreelancersModel> bidsFreelancer = new ArrayList<>();
    private ArrayList<String> searchPriorityList;
    private ArrayAdapter searchPriorityAdapter;




    public RequestModel currentRequest;
    protected HashMap<String,FreelancersModel> bidHashmap = new HashMap<>();
    protected App app;

    public FreelancerTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = App.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.

        View v = inflater.inflate(R.layout.fragment_freelancer, container, false);

        app = App.getInstance();
        String requestId = getArguments().getString("requestId");
        currentRequest = app.getRequestById(requestId);



        initControls(v);
        setupRecyclerView(v);
        OnStartActivity();

        return v;
    }


    private void initControls(View v) {
        bidsFreelancer = new ArrayList<FreelancersModel>();
    }





    private void setupRecyclerView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.search_results_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);



        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);


        // adding items



    }


    protected void modifyBids(FreelancersModel freelancersModel)
    {
        bidHashmap.put(freelancersModel.getUid(),freelancersModel);
        currentRequest.getFreelancers().put(freelancersModel.getUid(),freelancersModel);


        int i=0;
        for(FreelancersModel fm : bidsFreelancer)
        {
            if(freelancersModel.getUid().equals(fm.getUid()))
            {
                bidsFreelancer.set(i,freelancersModel);
                break;
            }
            i++;
        }


        mAdapter.notifyDataSetChanged();
    }

    protected void addBidders(FreelancersModel freelancersModel) {

        if(bidHashmap.containsKey(freelancersModel.getUid())) {
            modifyBids(freelancersModel);
            return;
        }

        bidHashmap.put(freelancersModel.getUid(),freelancersModel);
        currentRequest.getFreelancers().put(freelancersModel.getUid(),freelancersModel);
        bidsFreelancer.add(freelancersModel);

        mAdapter.notifyDataSetChanged();
    }


    protected void removeBids(FreelancersModel freelancersModel)
    {


        if(!bidsFreelancer.contains(freelancersModel))
            return;

        bidHashmap.remove(freelancersModel);
        currentRequest.getFreelancers().remove(freelancersModel);
        bidsFreelancer.remove(bidsFreelancer);
        mAdapter.notifyDataSetChanged();
    }


    public CommonAdapter<FreelancersModel> createAdapter() {
        return new CommonAdapter<FreelancersModel>(bidsFreelancer, R.layout.bids_model) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.BidsHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders pattrenHolder, final FreelancersModel model, int position) {

                ViewHolders.BidsHolder holder = (ViewHolders.BidsHolder)pattrenHolder;


                app.loadingImage(getActivity().getApplicationContext(), holder.getPicture(), model.getProfilePicture());

                // Capitalize Request Title letters
                String bidUsername = model.getUsername();

                String capitlizedReqtitle = bidUsername.substring(0,1).toUpperCase() +  bidUsername.substring(1);

                    // Capitalize game title letters
                    String cpWord= "";
                    for (int  i = 0 ; i < capitlizedReqtitle.length(); i++)
                    {
                        if (capitlizedReqtitle.charAt(i) == 32 && capitlizedReqtitle.charAt(i+1) != 32)
                        {
                            cpWord= capitlizedReqtitle.substring(i+1,i+2).toUpperCase() + capitlizedReqtitle.substring(i+2);
                            capitlizedReqtitle = capitlizedReqtitle.replace(capitlizedReqtitle.charAt(i+1),cpWord.charAt(0));
                        }
                    }


                holder.setTitle(capitlizedReqtitle);


                //holder.setSubtitle(model.getDescription());
                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getBid().getTimeStamp()));

                holder.setBid(String.valueOf(model.getBid().getBid()) + " " +currentRequest.getRequestCurrency());
                holder.setSubtitle(model.getBid().getBidDescription());


                Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
                Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");
                // Set the font
                holder.getTitleView().setTypeface(playbold);
                holder.getSubtitleView().setTypeface(playregular);




                if(app.getUserInformation().getType().equals(ACCOUNT_TYPE_CUSTOMER) && currentRequest.getStatus().equals(WATTING_STATUS))
                {
                    holder.getHireButton().setVisibility(View.VISIBLE);
                } else  holder.getHireButton().setVisibility(View.GONE);


                if(model.getBid().isStatus())
                {
                    holder.getDoneImg().setVisibility(View.VISIBLE);
                }




                holder.getHireButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure to hire ( "+model.getUsername()+" )?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                OnClickHire(model);
                            }
                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).show();

                    }
                });


                // Set the request image border width
                holder.getPicture().setBorderWidth(8);


                // Game provider settings
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model,v);

                    }
                });
            }

        };
    }



    protected abstract void OnStartActivity();
    protected abstract void OnClickHolders(FreelancersModel model, View v);
    protected abstract void OnClickHire(FreelancersModel model);




}
