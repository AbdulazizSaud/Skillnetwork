package com.example.boxtech.skillnetwork.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.boxtech.skillnetwork.R;

import de.hdodenhof.circleimageview.CircleImageView;


public abstract class ViewHolders extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {



    protected CircleImageView picture;
    protected TextView title,subtitle,time ,subtitle2 ;
    protected View view;
    protected ImageView subtitleImageview;
    protected TextView paymentAmount;

    public ViewHolders(View v) {
        super(v);
        this.view = v;
    }

    public View getView(){

    return view;

    }

    public static class CommunityHolder extends ViewHolders {
        TextView chatNewMessagesCount;
        TextView chatLastMessage;

        public CommunityHolder(View v) {
            super(v);



            picture = (CircleImageView) v.findViewById(R.id.chatOpponent);

            title =  (TextView) v.findViewById(R.id.chatOpponentFullname);

            Typeface playbold = Typeface.createFromAsset(title.getContext().getAssets(), "playbold.ttf");
            Typeface playregular = Typeface.createFromAsset(title.getContext().getAssets(), "playregular.ttf");


            title.setTypeface(playbold);


            time =  (TextView) v.findViewById(R.id.chatLastMessageAgo);
            time.setTypeface(playregular);

            chatNewMessagesCount =  (TextView) v.findViewById(R.id.chatNewMessagesCount);
            chatNewMessagesCount.setTypeface(playbold);

            chatLastMessage = (TextView) v.findViewById(R.id.chatLastMessage);
            chatLastMessage.setTypeface(playregular);

        }
        public void setCommunitySubtitle(String subtitle){
            this.chatLastMessage.setText(subtitle);
        }

        public TextView getChatCounterView(){
            return chatNewMessagesCount;
        }

        public void setCounter(String counter){
            this.chatNewMessagesCount.setText(counter);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
    public static class RequestHolder extends ViewHolders {
        TextView descrption,budget,skills;

        private ImageView requestStatus;

        public RequestHolder(View v) {
            super(v);





            title =  (TextView) v.findViewById(R.id.requestTitle);

            Typeface playbold = Typeface.createFromAsset(title.getContext().getAssets(), "playbold.ttf");
            Typeface playregular = Typeface.createFromAsset(title.getContext().getAssets(), "playregular.ttf");

            title.setTypeface(playbold);


            time =  (TextView) v.findViewById(R.id.time_stamp_request_model);


            descrption = (TextView) v.findViewById(R.id.request_description);
            descrption.setTypeface(playregular);

            budget = (TextView)v.findViewById(R.id.budget_requests);
            skills = (TextView)v.findViewById(R.id.skills_requests);


            requestStatus = (ImageView)v.findViewById(R.id.status_request_image_view);

        }


        public void setVisibility(int v){
            requestStatus.setVisibility(v);

        }

        public void setResource(int resource)
        {
            requestStatus.setImageResource(resource);
        }

        public void setDescriptionSubtitle(String subtitle){
            this.descrption.setText(subtitle);
        }

        public void setBudget(String budget){
            this.budget.setText(budget);
        }
        public void setSkills(String skiils){
            this.skills.setText(skiils);
        }




        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public static class BidsHolder extends  ViewHolders {

        private Button hireButton;

        private ImageView doneImg;


        public BidsHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.name_bidder_textview);
            picture = (CircleImageView) v.findViewById(R.id.game_photo_request_model);
            subtitle = (TextView) v.findViewById(R.id.description_request_model);
            paymentAmount = (TextView) v.findViewById(R.id.number_of_players_request_model);
            subtitleImageview = (ImageView) v.findViewById(R.id.match_type_request_model);
            subtitle2 = (TextView) v.findViewById(R.id.requester_request_model);
            time = (TextView) v.findViewById(R.id.time_stamp_request_model);
            hireButton = (Button)v.findViewById(R.id.hire_btn);
            doneImg = (ImageView)v.findViewById(R.id.done_image_view);

        }

        public Button getHireButton() {
            return hireButton;
        }

        public ImageView getDoneImg() {
            return doneImg;
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    public TextView getTitle() {
        return title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getSubtitle2() {
        return subtitle2;
    }

    public ImageView getSubtitleImageview() {
        return subtitleImageview;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public void setSubtitle(TextView subtitle) {
        this.subtitle = subtitle;
    }



    public void setSubtitle2(String subtitle2) {
        this.subtitle2.setText(subtitle2);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setSubtitleImageview(ImageView subtitleImageview) {
        this.subtitleImageview = subtitleImageview;
    }

    public void setPaymentAmount(TextView paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setPicture(CircleImageView picture) {
        this.picture = picture;
    }
    public CircleImageView getPicture() {
        return picture;
    }
    public TextView getTitleView() {
        return title;
    }
    public void setTitleView(TextView title) {
        this.title = title;
    }
    public TextView getSubtitleView() {
        return subtitle;
    }
    public void setSubtitleView(TextView subtitle) {
        this.subtitle = subtitle;
    }
    public TextView getTimeView() {
        return time;
    }
    public void setTimeView(TextView time) {
        this.time = time;
    }
    public String getPaymentAmount() {
        return paymentAmount.getText().toString().trim();
    }


    public void setBid(String numberOfPlayers) {
        this.paymentAmount.setText(numberOfPlayers);
    }

    public void setTitle(String title){
        this.title.setText(title);
    }
    public void setSubtitle(String subtitle){
        this.subtitle.setText(subtitle);
    }
    public void setTime(String time){
        this.time.setText(time);
    }

}
