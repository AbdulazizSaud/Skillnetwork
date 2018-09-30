package com.example.boxtech.skillnetwork.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.boxtech.skillnetwork.Models.CommunityChatModel;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class CommunityFragment extends Fragment  {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private RecyclerView mRecyclerView;
    private ImageView bgChatImageView;

    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton newPrivateChatFloatingActionButton;

    protected HashMap<String, CommunityChatModel> communityChatModelHashMap = new HashMap<>();
    protected HashMap<String, CommunityChatModel> deletedCommunityChatModelHashMap = new HashMap<>();

    protected ArrayList<CommunityChatModel> communityUserLists = new ArrayList<CommunityChatModel>();
    protected RecyclerView.Adapter mAdapter;

    protected App app;


    public CommunityFragment() {
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

        View v = inflater.inflate(R.layout.fragment_community, container, false);
        setupRecyclerView(v);


        bgChatImageView = (ImageView) v.findViewById(R.id.splash);
        OnStartActivity();
        return v;
    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mAdapter =   new CommonAdapter<CommunityChatModel>(communityUserLists, R.layout.new_user_chat_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final CommunityChatModel model, int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder) holder;


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


                holder.getPicture().setBorderWidth(6);
                holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.app_color));


                // Capitalize Title letters
                String chatTitle = model.getChatName();

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


                communityHolder.setCommunitySubtitle(model.getLastMsg());
                app.loadingImage(getContext(), holder.getPicture(), model.getUserPictureURL());


                // Hide the counter if unseen messages less than 1 message
                if (model.getChatCounter() < 1) {
                    communityHolder.getChatCounterView().setVisibility(View.INVISIBLE);
                } else {
                    communityHolder.getChatCounterView().setVisibility(View.VISIBLE);
                    communityHolder.setCounter(String.valueOf(model.getChatCounter()));
                }

                communityHolder.setCounter(String.valueOf(model.getChatCounter()));

                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));

                new HandlerCondition(new CallbackHandlerCondition() {
                    @Override
                    public boolean callBack() {

                        holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
                        return false;
                    }
                }, 20000);
//                Log.i("->",""+getAllUnseenMessages());






            }




        };

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);




    }


    protected void showOnLongClickDialog(final CommunityChatModel communityChatModel) {

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
                removeChatFromlist(communityChatModel);
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


    protected void addChat(CommunityChatModel communityChatModel) {



        if(!hasChat(communityChatModel.getChatKey())) {
            communityUserLists.add(communityChatModel);
            communityChatModelHashMap.put(communityChatModel.getChatKey(), communityChatModel);
            mAdapter.notifyDataSetChanged();
        } else {

        }

        // Community BG declaration imageview
        if (mAdapter.getItemCount() < 1)
            bgChatImageView.setVisibility(View.VISIBLE);
        else
            bgChatImageView.setVisibility(View.INVISIBLE);

    }



    protected CommunityChatModel addChat(String chatKey, String chatType, String chatName, String pictureURL, String lastMessage, long timeStamp, long messageNumber) {

        CommunityChatModel communityUserList = new CommunityChatModel();
        communityUserList.setChatKey(chatKey);
        communityUserList.setChatType(chatType);
        communityUserList.setChatName(chatName);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        communityUserList.setChatCounter(messageNumber);
        communityUserList.setTimeStamp(timeStamp);



        if(!hasChat(communityUserList.getChatKey())) {
            communityUserLists.add(communityUserList);
            communityChatModelHashMap.put(communityUserList.getChatKey(), communityUserList);
            mAdapter.notifyDataSetChanged();
        } else {

        }

        // Community BG declaration imageview
        if (mAdapter.getItemCount() < 1)
            bgChatImageView.setVisibility(View.VISIBLE);
        else
            bgChatImageView.setVisibility(View.INVISIBLE);


        return communityUserList;
    }



    public void removeFromList(String key) {

        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(key)) {
                communityUserLists.remove(communityChatModel);
                communityChatModelHashMap.remove(communityChatModel.getChatKey());
                mAdapter.notifyDataSetChanged();
                break;
            }
        }


    }
    public boolean hasChat(String key)
    {
        return communityChatModelHashMap.containsKey(key);
    }


    protected abstract void removeChatFromlist(CommunityChatModel model);

    protected abstract void OnStartActivity();
    protected abstract void OnClickHolders(CommunityChatModel model, View v);

}
