package com.example.boxtech.skillnetwork.Cores;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.CoresAbstract.AddSkill;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class AddSkillCore extends AddSkill {






    @Override
    protected void OnStartActivity() {


        app.getDatabaseSkills().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String id = snapshot.getKey();
                    String jobName = snapshot.child("jobName").getValue(String.class);
                    String type = snapshot.child("Type").getValue(String.class);
                    app.addSkill(id,jobName,type);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void addSkillToFirebase()
    {
        String uid = app.getUserInformation().getUID();
        app.getDatabaseUsersInfo().child(uid).child("_skills_").setValue(skillsModels);
    }
}
