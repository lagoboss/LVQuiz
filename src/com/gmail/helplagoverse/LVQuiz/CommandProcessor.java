package com.gmail.helplagoverse.LVQuiz;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by lake.smith on 5/17/2018.
 */
public class CommandProcessor implements CommandExecutor {

    private final Main plugin;

    public CommandProcessor(Main plugin){
        this.plugin = plugin;
    }

    String start = "You have attempted to start an exam... Good luck!";
    String answer = "You have attempted to answer an exam question...";
    String start_error_noExamProvided = "You did not provide a valid exam code... Please enter a valid exam code from your instructor or institution...";
    String args1Options = "Do you want to start, answer, end, or update the Quiz?";
    String answerUsage = "usage is /LVQuiz answer <Exam_Code> <item> <answer>";



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Economy economy = plugin.getEcononomy();
        FileConfiguration config = plugin.getConfig();

        Player p = (Player) sender;
        String fileName = p.getUniqueId().toString();
        File playerData = new File(plugin.getDataFolder() + "//data//", fileName + ".yml");
        FileConfiguration users = YamlConfiguration.loadConfiguration(playerData);

        boolean configContainsTest;
        boolean configContainsItem;
        boolean testIsEnabled;

        if (!playerData.exists()){

            try{
                users.save(playerData);
            }catch (IOException e){
                e.printStackTrace();
            }
            p.sendMessage("New testing file created for you...");

        }

        if (playerData.exists()){

            p.sendMessage("Accessing your testing file...");

        }

        if(command.getName().equalsIgnoreCase("LVQuiz")){

            if(args.length == 0){
                p.sendMessage(args1Options);
               return true;
            }

            if(args.length == 1){
                if(args[0].equalsIgnoreCase("start")){
                    p.sendMessage("usage is /LVQuiz start <Exam_Code>");
                    return true;
                }
                if(args[0].equalsIgnoreCase("answer")){
                    p.sendMessage(answerUsage);
                    return true;
                }
                if(args[0].equalsIgnoreCase("update")){
                    p.sendMessage("usage is /LVQuiz update <Exam_Code>");
                    return true;
                }
                if(args[0].equalsIgnoreCase("end")){
                    p.sendMessage("usage is /LVQuiz end <Exam_Code>");
                    return true;
                }
                p.sendMessage(args1Options);
                return true;
            }

            if(args.length == 2){

                //configContainsTest = plugin.getConfig().contains(args[1]);
                //configContainsItem = plugin.getConfig().contains(args[1] + "." + args[2]);
                testIsEnabled = users.getBoolean("Exam_Name." + args[1] + ".Started");

                if(args[0].equalsIgnoreCase("start")){
                    p.sendMessage(start);

                        if(plugin.getConfig().contains(args[1]) && !users.contains("Exam_Name." + args[1] + ".Started", true)){
                            users = YamlConfiguration.loadConfiguration(playerData);

                            //use date later for when they complete/end it
                            //Date now = new Date();
                            //String nowAsString = now.toString();

                            users.set("Exam_Name", args[1]);
                            users.set("Exam_Name." + args[1] + ".Started", true);

                            try{
                                users.save(playerData);
                                p.sendMessage("Exam File Added!");
                                p.sendMessage("Testing has started!");
                                p.sendMessage(answerUsage);

                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            return true;


                            }
                        if(plugin.getConfig().contains(args[1]) && users.getBoolean("Exam_Name." + args[1] + ".Started", true)){
                            p.sendMessage("You have already started this exam...");
                            p.sendMessage(answerUsage);
                            return true;
                        }
                        else{
                            p.sendMessage(start_error_noExamProvided);
                            return true;
                        // lvquiz start invalid_test_code
                    }
                }

                if(args[0].equalsIgnoreCase("answer")){
                    p.sendMessage(answerUsage);
                    return true;

                }

                if(args[0].equalsIgnoreCase("end")){
                    p.sendMessage("end");
                    return true;
                }

                if(args[0].equalsIgnoreCase("update")){
                    p.sendMessage("update");
                    return true;
                }
                else{
                    p.sendMessage("Do you want to start, answer, end, or update the Quiz?");
                }
            }

            ///LVQuiz end <exam_code> confirm
            if(args.length == 3){
                if(args[0].equalsIgnoreCase("end")){
                Grader grader = new Grader();
                    grader.endExam(config, users, args[1], p);
                    //has to be a config, has to be playerdata, has to be a real exam, has to be a player, cannot divide by 0


                return true;}
            }

            if(args.length == 4){

                // "usage is /LVQuiz answer <Exam_Code> <item> <answer>"
                if(args[0].equalsIgnoreCase("answer")) {

                    configContainsTest = plugin.getConfig().contains(args[1]);
                    configContainsItem = plugin.getConfig().contains(args[1] + "." + args[2]);
                    testIsEnabled = users.getBoolean("Exam_Name." + args[1] + ".Started");
                    int testItemEntered = 1000;

                    if(args[2].length() > 4){

                        if (!firstFour(args[2]).equals("item")) {

                            p.sendMessage("All exam questions are called item[n]; i.e., 'item9'");
                            p.sendMessage("Please enter a valid exam question code in the format of 'Item + Number'");
                            p.sendMessage(firstFour(args[2]));
                            return true;

                        } else {

                            try {

                                testItemEntered = Integer.parseInt(args[3]) + 1;

                            } catch (Exception e) {
                                System.out.println("not a number");
                                p.sendMessage("Answer '" + args[3] + "' is not an integer...");

                                return true;
                            }

                        }
                    }else{
                        p.sendMessage("Invalid question code; please enter a valid question code");

                        return true;
                    }
                    // "usage is /LVQuiz answer <Exam_Code> <item> <answer>"
                    //this needs to check if the item is correct if answer eqals the answer for the exam key and then the grader can count the amount correct{
                    if(configContainsTest && configContainsItem && testIsEnabled && testItemEntered != 1000){

                        Grader grader = new Grader();
                        grader.onDemandGrader(args[1],args[2],Integer.parseInt(args[3]),users,config,p,playerData);

                        return true;

                    } else
                        p.sendMessage("Error answering test...");
                    //p.sendMessage("You attempted to answer an invalid test, you have not started the test yet, and/or you have entered an invalid test item...");
                    p.sendMessage("Please verify you have the correct testing information");


                    return true;
                }
            }

            return true;
        }

        return true;
    }

    public String firstFour(String str) {

        char char1 = str.charAt(0);
        char char2 = str.charAt(1);
        char char3 = str.charAt(2);
        char char4 = str.charAt(3);

        String first4 = (Character.toString(char1) +
                Character.toString(char2) +
                Character.toString(char3) +
                Character.toString(char4));


        return first4;
    }
}
