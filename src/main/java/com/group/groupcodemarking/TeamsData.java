package com.group.groupcodemarking;

import java.io.Serializable;
import java.util.*;

import static com.group.groupcodemarking.MathHelper.normaliseCommitters;
import static com.group.groupcodemarking.MathHelper.normaliseList;

public class TeamsData implements Serializable {
    private List<Committer> teamMessageSent;
    private List<Committer> filesUploaded;
    private List<Committer> timeSpentInCalls;


    public TeamsData(List<Committer> teamMessageSent, List<Committer> filesUploaded, List<Committer> timeSpentInCalls) {
        this.teamMessageSent = teamMessageSent;
        this.filesUploaded = filesUploaded;
        this.timeSpentInCalls = timeSpentInCalls;
    }

    public List<Committer> getTeamMessageSent() {
        return teamMessageSent;
    }

    public void setTeamMessageSent(List<Committer> teamMessageSent) {
        this.teamMessageSent = teamMessageSent;
    }

    public List<Committer> getFilesUploaded() {
        return filesUploaded;
    }

    public void setFilesUploaded(List<Committer> filesUploaded) {
        this.filesUploaded = filesUploaded;
    }

    public List<Committer> getTimeSpentInCalls() {
        return timeSpentInCalls;
    }

    public void setTimeSpentInCalls(List<Committer> timeSpentInCalls) {
        this.timeSpentInCalls = timeSpentInCalls;
    }


    public Map<String, Double> calculateTeamScores() {
        List<Integer> values = new ArrayList<>();
        Map<String, Double> peopleMap = new HashMap<>();
//        teamMessageSent.forEach(e -> {
//            values.add(e.getValue() + filesUploaded.stream().filter(f -> f.getName().equals(e.getName())).findFirst().get().getValue()
//                    + timeSpentInCalls.stream().filter(f -> f.getName().equals(e.getName())).findFirst().get().getValue());
//        });

        List<Committer> messagesSentNormal = getMessagesSentScore();
        List<Committer> documentationUploadNormal = getDocumentationUploadScores();
        List<Committer> spentInCallNormal = getTimeSpentInCallsScore();
        for (Committer com : messagesSentNormal) {
            List<Integer> committers = new ArrayList<>();
            committers.add(com.getValue());
            committers.add(documentationUploadNormal.stream().filter(f -> f.getName().equals(com.getName())).findFirst().get().getValue());
            committers.add(spentInCallNormal.stream().filter(f -> f.getName().equals(com.getName())).findFirst().get().getValue());

            Double average = committers.stream().mapToInt(val -> val).average().orElse(0.0);

            peopleMap.put(com.getName().split(" ")[0], average);
        }

        return peopleMap;

    }

    private List<Committer> getMessagesSentScore() {

        double sum = teamMessageSent.stream()
                .mapToDouble(Committer::getValue)
                .sum();
        if (sum > 500) {
            return normaliseCommitters(teamMessageSent, 75, 100);
        } else if (sum > 400) {
            return normaliseCommitters(teamMessageSent, 60, 90);
        } else if (sum > 300) {
            return normaliseCommitters(teamMessageSent, 55, 80);
        } else if (sum > 200) {
            return normaliseCommitters(teamMessageSent, 45, 70);
        } else if (sum > 100) {
            return normaliseCommitters(teamMessageSent, 35, 60);
        } else if (sum > 75) {
            return normaliseCommitters(teamMessageSent, 25, 50);
        } else {
            return normaliseCommitters(teamMessageSent, 1, 20);
        }
    }

    private List<Committer> getDocumentationUploadScores() {

        double sum = filesUploaded.stream()
                .mapToDouble(Committer::getValue)
                .sum();
        if (sum > 75) {
            return normaliseCommitters(filesUploaded, 75, 100);
        } else if (sum > 50) {
            return normaliseCommitters(filesUploaded, 60, 90);
        } else if (sum > 35) {
            return normaliseCommitters(filesUploaded, 55, 80);
        } else if (sum > 30) {
            return normaliseCommitters(filesUploaded, 45, 70);
        } else if (sum > 20) {
            return normaliseCommitters(filesUploaded, 35, 60);
        } else if (sum > 10) {
            return normaliseCommitters(filesUploaded, 25, 50);
        } else {
            return normaliseCommitters(filesUploaded, 1, 20);
        }
    }

    private List<Committer> getTimeSpentInCallsScore() {

        //Highest score is hour a week
        double sum = timeSpentInCalls.stream()
                .mapToDouble(Committer::getValue)
                .sum();
        if (sum > 2800) {
            return normaliseCommitters(timeSpentInCalls, 75, 100);
        } else if (sum > 2300) {
            return normaliseCommitters(timeSpentInCalls, 60, 90);
        } else if (sum > 1800) {
            return normaliseCommitters(timeSpentInCalls, 55, 80);
        } else if (sum > 1400) {
            return normaliseCommitters(timeSpentInCalls, 45, 70);
        } else if (sum > 1000) {
            return normaliseCommitters(timeSpentInCalls, 35, 60);
        } else if (sum > 500) {
            return normaliseCommitters(timeSpentInCalls, 25, 50);
        } else {
            return normaliseCommitters(timeSpentInCalls, 1, 20);
        }
    }

}