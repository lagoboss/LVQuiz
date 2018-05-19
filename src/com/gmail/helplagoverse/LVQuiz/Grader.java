package com.gmail.helplagoverse.LVQuiz;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by lake.smith on 5/17/2018.
 */
public class Grader {

    FileConfiguration playerData;
    FileConfiguration examData;
    String itemN;
    int ans;
    boolean isCorrect;
    int correctAns;
    String examN;
    String dot = ".";
    String locCorrectAns;
    Player p;
    String correct;

    String headingExamName = "Exam_Name";
    String fieldAnswer = "Answer";
    String fieldValue = "Value";
    String fieldPassed = "Passed";

    String fieldNumberOfItems = "Number-of-Items";
    String fieldPassingScore = "passing_score";

    File filePD;

    int total;
    double requiredScore;

    public void onDemandGrader(String examName, String itemNumber, int answer, FileConfiguration inboundPlayerDataFile, FileConfiguration inboundExamData, Player player, File filePlayerData){

        this.playerData = inboundPlayerDataFile;
        this.examData = inboundExamData;
        this.itemN = itemNumber;
        this.ans = answer;
        this.examN = examName;
        this.locCorrectAns = "Correct_Answer";
        this.p = player;
        this.filePD = filePlayerData;


        correctAns = examData.getInt(examN + dot + itemN + dot + locCorrectAns);
        // "usage is /LVQuiz answer <Exam_Code> <item> <answer>"

        try{
            playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldValue, ans);

            inboundPlayerDataFile.save(filePD);
            p.sendMessage("Saving your answer...");
            p.sendMessage("You answered: " + ans + " please continue answering questions or submit your exam...");

            if (ans == correctAns){

                playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed, true);

                try{
                    inboundPlayerDataFile.save(filePD);


                }catch(IOException e){

                    p.sendMessage("Error saving your file; please contact your server's admin for more  details...");
                    e.printStackTrace();
                }

            }
            else{
                playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed, false);

                try{
                    inboundPlayerDataFile.save(filePD);

                }catch (IOException e){
                    p.sendMessage("Error saving your file; please contact your server's admin for more  details...");
                    e.printStackTrace();
                }
            }

        }catch (IOException e){
            p.sendMessage("Error saving your file; please contact your server's admin for more  details...");
            e.printStackTrace();
        }
    }

    public void endExam (FileConfiguration examen, FileConfiguration pData, String quizName, Player p){


        int examPoint;

        int examScore;
        double passingScore;
        int pointsPossible;

        int examPoints = 0;

        for(int i = 0; i <= this.total; i++) {

            //headingExamName + dot + quizName + dot + itemN + dot + fieldAnswer + dot + fieldPassed
            itemN = "item" + i;

            boolean point = pData.getBoolean(headingExamName + dot + quizName + dot + itemN + dot + fieldAnswer + dot + fieldPassed);

            if(point) {
                examPoints++;
                p.sendMessage("point added, points = " + examPoints);
            }

            //examen.getInt(examN + dot + fieldNumberOfItems);

            pointsPossible = examen.getInt("Exam1.Number-of-Items");

            p.sendMessage("points possible are: " + pointsPossible);

            examScore = 100 * examPoints/pointsPossible;

            p.sendMessage("exam score is: " + examScore);

            passingScore = examen.getDouble(examN + dot + fieldPassingScore);




        if(examScore >= passingScore){
                p.sendMessage("You earned a: " + examScore);
                p.sendMessage("Congratulations, you passed!");
        }else{
            p.sendMessage("You earned a: " + examScore);
            p.sendMessage("We regret to inform you that you failed...");
        }
        }
    }
}

        /**this.examData = examen;
        this.playerData = pData;

        this.total = examData.getInt(examN + dot + fieldNumberOfItems);
        this.requiredScore = examData.getInt(examN + dot + fieldPassingScore);


        p.sendMessage("Total is: " + this.total);
        int points = 0;

        for(int i = 0; i <= this.total; i++) {
            boolean point = playerData.getBoolean(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed);

            if(point) {
                points++;
                }
            }

            //int plyrScore = points/this.total;
            //double plyrScoreD = plyrScore;

            if((plyrScoreD) > this.requiredScore){
                p.sendMessage("Congratulations, you passed your exam!");


            }
        }**/
    //playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed);
    /**
     * NEED TO MAKE AN EXAM GENERATOR METHOD THAT ADDS THESE FIELDS TO THE PLAYER'S EXAM AND ADDS EACH POSSIBLE EXAM ITEM WITH A DEFAULT VALUE
     * Exam_Name:
     Exam1:
     Started: true
     Finished: true
     Date_Started:
     Date_Completed:
     Passed:
     Total_Points:
     Grade:
     item1:
     Answer:
     Value: 9
     Passed: false
     item2:
     Answer:
     Value: 90
     Passed: false
     **/




