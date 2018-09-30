package com.example.boxtech.skillnetwork.untill;


import com.example.boxtech.skillnetwork.Models.SkillsModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SkillManager {

    private HashMap<String,SkillsModel> skillIDs =  new HashMap<>();
    private HashMap<String,SkillsModel>  allSkillName =  new HashMap<>();
    private HashMap<String,SkillsModel> skillsModelHashMap  = new HashMap<>();



    public void addSkill(SkillsModel skillsModel)
    {
        skillIDs.put(skillsModel.getSkillsId(),skillsModel);
        allSkillName.put(skillsModel.getSkillName().toLowerCase(),skillsModel);
        skillsModelHashMap.put(skillsModel.getSkillsId(),skillsModel);
    }

    public void deleteSkill(String skillId,String skillName)
    {
        skillsModelHashMap.remove(skillId);
        skillIDs.remove(skillId);
        allSkillName.remove(skillName);
    }



    public SkillsModel getSkillByName(String name)
    {
        SkillsModel skillsModel = null;
        skillsModel = allSkillName.get(name.toLowerCase());
        return skillsModel;
    }

    public SkillsModel getSkillById(String Id){

        return skillIDs.get(Id);
    }

    private void clear()
    {
        skillsModelHashMap.clear();
    }


    public boolean checkIfHasSkillByName(String name)
    {
        SkillsModel skillsModel = null ;
        skillsModel =  getSkillByName(name);

        if (skillsModel != null)
            return true;

        return false;
    }

    public  boolean checkIfHasSkillById(String id){

        SkillsModel gameModel = null ;
        gameModel =  getSkillById(id);

        if (gameModel != null)
            return true;

        return false;
    }

    public ArrayList<SkillsModel> getAllSkills() {
        return new ArrayList<>(skillsModelHashMap.values());
    }
}
