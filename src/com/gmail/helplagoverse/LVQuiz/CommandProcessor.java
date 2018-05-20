package com.gmail.helplagoverse.LVQuiz;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.bukkit.Bukkit.getServer;

/**
 * Created by lake.smith on 5/17/2018.
 */
public class CommandProcessor implements CommandExecutor {

    private final Main plugin;

    public CommandProcessor(Main plugin){
        this.plugin = plugin;
    }

    String pluginLogo = ChatColor.GRAY + "[LVQuiz] ";

    String start = pluginLogo + ChatColor.GREEN + "You have attempted to start an exam... Good luck!";
    String answer = pluginLogo + ChatColor.GREEN + "You have attempted to answer an exam question...";
    String start_error_noExamProvided = pluginLogo + ChatColor.RED + "You did not provide a valid exam code... Please enter a valid exam code from your instructor or institution...";
    String args1Options = pluginLogo + ChatColor.GRAY + "Do you want to start, answer, end, or update the quiz?";
    String answerUsage = pluginLogo + ChatColor.RED + "usage is /LVQuiz answer <Exam_Code> <item> <answer>.";
    String startUsage = pluginLogo + ChatColor.RED + "usage is /LVQuiz start <Exam_Code>";
    String endUsage = pluginLogo + ChatColor.RED + "usage is /LVQuiz end <Exam_Code>";
    String errorExamAlreadyStarted = ChatColor.RED + "You have already started this exam...";
    String errorItemFormat = ChatColor.RED + "All exam questions are labeled item[n].";
    String testingFileCreated = pluginLogo + ChatColor.GREEN + "New testing file created for you...";
    String examFileCreated = pluginLogo + ChatColor.GREEN + "Exam File Added!";
    String accessingTestingFile = pluginLogo + ChatColor.GREEN + "Accessing your testing file...";
    String examStarted = pluginLogo + ChatColor.GREEN + "Testing has started!";

    String endPromptConfirm = pluginLogo + ChatColor.GREEN + "To confirm, run /LVQuiz end <Exam_Code> confirm";
    String genericError = pluginLogo + ChatColor.RED + "Please verify you have the correct testing information";

    String errorNoPermission = pluginLogo + ChatColor.RED + "You do not have permission to take this exam; please check you have the correct exam and/or speak with your institution...";
    String errorExamAlreadyGraded = pluginLogo + ChatColor.RED + "This exam has already been graded; is there another exam you would like to end?";
    String successReload = pluginLogo + ChatColor.GREEN + "Config reloaded!";
    String savingConfig = pluginLogo + ChatColor.GREEN + "Saving config...";
    String insuffcientPermission = pluginLogo + ChatColor.RED + "Insuffcient permission to execute this command...";
    String errorSavingPlayerData = pluginLogo + ChatColor.RED + "Error saving player data file...";




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
        boolean isGraded;

        if (!playerData.exists()){

            try{
                users.save(playerData);
            }catch (IOException e){
                e.printStackTrace();
            }
            p.sendMessage(testingFileCreated);

        }

        if (playerData.exists()){

            p.sendMessage(accessingTestingFile);

        }

        if(command.getName().equalsIgnoreCase("LVQuiz")){

            if(args.length == 0){
                p.sendMessage(args1Options);
               return true;
            }

            if(args.length == 1){
                if(args[0].equalsIgnoreCase("start")){
                    p.sendMessage(startUsage);
                    return true;
                }
                if(args[0].equalsIgnoreCase("answer")){
                    p.sendMessage(answerUsage);
                    return true;
                }
                if(args[0].equalsIgnoreCase("update")){
                    p.sendMessage("usage is /LVQuiz update <Exam_Code>; command coming soon!");
                    return true;
                }
                if(args[0].equalsIgnoreCase("end")){
                    p.sendMessage(endUsage);
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")){
                    if(p.hasPermission("LVquiz.reload")){

                        plugin.saveConfig();
                        try{
                            users.save(playerData);

                        }catch (IOException e){
                            e.printStackTrace();
                            p.sendMessage(errorSavingPlayerData);
                        }
                        p.sendMessage(savingConfig);
                        p.sendMessage(successReload);
                        return true;
                    }
                    else{
                        p.sendMessage(insuffcientPermission);
                        return true;
                    }
                }
                else{
                    p.sendMessage(args1Options);
                    return true;
                }
            }

            if(args.length == 2){

                //configContainsTest = plugin.getConfig().contains(args[1]);
                //configContainsItem = plugin.getConfig().contains(args[1] + "." + args[2]);


                if(args[0].equalsIgnoreCase("start")){
                    p.sendMessage(start);

                        String permissionReq = config.getString(args[1]+".permission_start");

                        if(plugin.getConfig().contains(args[1]) && !users.contains("Exam_Name." + args[1] + ".Started", true)){

                                if(p.hasPermission(permissionReq)){
                                users = YamlConfiguration.loadConfiguration(playerData);

                                users.set("Exam_Name", args[1]);
                                users.set("Exam_Name." + args[1] + ".Started", true);
                                Date now = new Date();
                                String nowAsString = now.toString();
                                users.set("Exam_Name." + args[1] + ".Date_Started", nowAsString);
                                users.set("Exam_Name." + args[1] + ".Total_Correct", 0);
                                users.set("Exam_Name." + args[1] + ".Score", 0.00);
                                users.set("Exam_Name." + args[1] + ".Date_Completed", "");
                                users.set("Exam_Name." + args[1] + ".Graded", false);


                                String commandToExecute = "lp user " + p.getName() + " permission unsettemp " + args[1] + ".failed";
                                getServer().dispatchCommand(getServer().getConsoleSender(), commandToExecute);

                                try{
                                    users.save(playerData);
                                    p.sendMessage(examFileCreated);
                                    p.sendMessage(examStarted);
                                    p.sendMessage(answerUsage);

                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                                return true;
                            } else {
                                p.sendMessage(errorNoPermission);
                                    return true;
                                }

                        }
                        if(plugin.getConfig().contains(args[1]) && users.getBoolean("Exam_Name." + args[1] + ".Started", true)){
                            p.sendMessage(errorExamAlreadyStarted);
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
                    p.sendMessage(endUsage);
                    p.sendMessage(endPromptConfirm);
                    return true;
                }

                if(args[0].equalsIgnoreCase("update")){
                    p.sendMessage("update");
                    return true;
                }
                else{
                    p.sendMessage(args1Options);
                }
            }

            ///LVQuiz end <exam_code> confirm
            if(args.length == 3){

                //&& users.getBoolean("Exam_Name." + args[1] + ".Started", true)
                //&& users.getBoolean("Exam_Name." + args[1] + ".Completed", false)

                if(args[0].equalsIgnoreCase("answer")){
                    p.sendMessage(answerUsage);
                    return true;
                }

                isGraded = users.getBoolean("Exam_Name." + args[1] + ".Graded");

                if(args[0].equalsIgnoreCase("end")){
                    if(!isGraded){
                        if(plugin.getConfig().contains(args[1])&& args[2].equalsIgnoreCase("confirm")){

                            Grader grader = new Grader();
                            grader.endExam(playerData, config, users, args[1], p);

                        return true;
                        }
                        else {
                            p.sendMessage(genericError);
                            return true;
                        }
                    }
                    else{
                        p.sendMessage(errorExamAlreadyGraded);
                        return true;
                    }
                }

            }

            if(args.length == 4){

                // "usage is /LVQuiz answer <Exam_Code> <item> <answer>"
                if(args[0].equalsIgnoreCase("answer")) {

                    configContainsTest = plugin.getConfig().contains(args[1]);
                    configContainsItem = plugin.getConfig().contains(args[1] + "." + args[2]);

                    int testItemEntered = 1000;

                    if(args[2].length() > 4){

                        if (!firstFour(args[2]).equals("item")) {

                            p.sendMessage(errorItemFormat);
                            p.sendMessage(firstFour(args[2]));
                            return true;

                        } else {

                            try {

                                testItemEntered = Integer.parseInt(args[3]) + 1;

                            } catch (Exception e) {
                                p.sendMessage(pluginLogo + ChatColor.RED + "Answer '" + args[3] + "' is not an integer...");

                                return true;
                            }

                        }
                    }else{
                        p.sendMessage(errorItemFormat);

                        return true;
                    }
                    testIsEnabled = users.getBoolean("Exam_Name." + args[1] + ".Started");

                    if(configContainsTest && configContainsItem && testIsEnabled && testItemEntered != 1000){
                        isGraded = users.getBoolean("Exam_Name." + args[1] + ".Graded");

                        if(isGraded){
                            p.sendMessage(errorExamAlreadyGraded);

                        return true;
                        }
                        else{
                            Grader grader = new Grader();
                            grader.onDemandGrader(args[1],args[2],Integer.parseInt(args[3]),users,config,p,playerData);
                            return true;
                        }

                    } else
                        p.sendMessage(genericError);
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
