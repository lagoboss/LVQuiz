package com.gmail.helplagoverse.LVQuiz;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.bukkit.Bukkit.getServer;

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
    String fieldTotal = "Total_Correct";
    String fieldDateCompleted = "Date_Completed";
    String fieldComplete = "Graded";
    String fieldScore = "Score";

    File filePD;

    int total = 0;
    double requiredScore;

    String pluginLogo = ChatColor.GRAY + "[LVQuiz] ";

    String permissionsTagAdded = pluginLogo + ChatColor.GREEN + "Completion tag added to your permission's account...";

    public void onDemandGrader(String examName, String itemNumber, int answer, FileConfiguration inboundPlayerDataFile, FileConfiguration inboundExamData, Player player, File filePlayerData){

        this.playerData = inboundPlayerDataFile;
        this.examData = inboundExamData;
        this.itemN = itemNumber;
        this.ans = answer;
        this.examN = examName;
        this.locCorrectAns = "Correct_Answer";
        this.p = player;
        this.filePD = filePlayerData;

            //working!
            if(playerData.getBoolean(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed) != true){

                correctAns = examData.getInt(examN + dot + itemN + dot + locCorrectAns);
                // "usage is /LVQuiz answer <Exam_Code> <item> <answer>"

                try{
                    playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldValue, ans);

                    inboundPlayerDataFile.save(filePD);
                    p.sendMessage(pluginLogo + ChatColor.GRAY + "Saving your answer...");
                    p.sendMessage(ChatColor.GREEN + "You answered: " + ans + " please continue answering questions or submit your exam...");

                    if (ans == correctAns){

                        playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed, true);

                        total = playerData.getInt(headingExamName + dot + examN + dot + fieldTotal);
                        total = total + 1;

                        playerData.set(headingExamName + dot + examN + dot + fieldTotal, total);

                        try{
                            inboundPlayerDataFile.save(filePD);


                        }catch(IOException e){

                            p.sendMessage(pluginLogo + ChatColor.RED + "Error saving your file; please contact your server's admin for more  details...");
                            e.printStackTrace();
                        }

                    }
                    else{
                        playerData.set(headingExamName + dot + examN + dot + itemN + dot + fieldAnswer + dot + fieldPassed, false);

                        try{
                            inboundPlayerDataFile.save(filePD);

                        }catch (IOException e){
                            p.sendMessage(pluginLogo + ChatColor.RED + "Error saving your file; please contact your server's admin for more  details...");
                            e.printStackTrace();
                        }
                    }

                }catch (IOException e){
                    p.sendMessage("Error saving your file; please contact your server's admin for more  details...");
                    e.printStackTrace();
                }
            }else{
                p.sendMessage(pluginLogo + ChatColor.RED + "You already answered this question; please move on to the next item");
            }
        }
        //working
        public void endExam (File inboundPlayerData, FileConfiguration examen, FileConfiguration pData, String quizName, Player p){

            int numberCorrectInActualExam = pData.getInt(headingExamName + dot + quizName + dot + fieldTotal);

            int numberOfItemsOnTheExam = examen.getInt(quizName + dot + fieldNumberOfItems);

            double passingScore = examen.getDouble(quizName + dot + fieldPassingScore);

            double examScore = 100 * numberCorrectInActualExam/numberOfItemsOnTheExam;

            Date now = new Date();
            String nowAsString = now.toString();

            pData.set(this.headingExamName + dot + quizName + dot + fieldDateCompleted, nowAsString);
            pData.set(this.headingExamName + dot + quizName + dot + fieldScore, examScore);
            pData.set(this.headingExamName + dot + quizName + dot + fieldComplete, true);

            //make a method that updates the notes about a player

            try{
                pData.save(inboundPlayerData);

            }catch (IOException e){
                p.sendMessage(pluginLogo + ChatColor.RED + "Error saving your file; please contact your server's admin for more  details...");
                e.printStackTrace();
            }

            if(examScore >= passingScore){
                    p.sendMessage(pluginLogo + ChatColor.GRAY + "You earned: " + examScore + "%");
                    p.sendMessage(ChatColor.GREEN + "Congratulations, you passed!");
                    String commandToExecute = "lp user " + p.getName() + " permission settemp " + quizName + ".passed true 30d00h00m";
                    getServer().dispatchCommand(getServer().getConsoleSender(), commandToExecute);
                    p.sendMessage(permissionsTagAdded);

            }else{
                p.sendMessage(pluginLogo + ChatColor.GRAY + "You earned: " + examScore + "%");
                p.sendMessage(pluginLogo + ChatColor.RED + "We regret to inform you that you failed...");
                String commandToExecute = "lp user " + p.getName() + " permission settemp " + quizName + ".failed true 30d00h00m";
                getServer().dispatchCommand(getServer().getConsoleSender(), commandToExecute);
                p.sendMessage(permissionsTagAdded);
            }
        }
    }




