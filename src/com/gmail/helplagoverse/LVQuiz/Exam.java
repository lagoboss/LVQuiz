package com.gmail.helplagoverse.LVQuiz;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Date;

/**
 * Created by lake.smith on 5/16/2018.
 */
public class Exam {

    FileConfiguration inboundConfigYml;
    int inboundNumberTotalItems;
    Date inboundExamStartTime;
    Date inboundExamEndTime;
    String inboundName;

    public Exam(FileConfiguration inboundConfigYml,
                int inboundNumberTotalItems,
                String inboundName) {
        this.inboundConfigYml = inboundConfigYml;
        this.inboundNumberTotalItems = inboundNumberTotalItems;
        this.inboundName = inboundName;
    }

    public Exam(String inboundExamName, FileConfiguration inboundExamDataFile){

    }
}
