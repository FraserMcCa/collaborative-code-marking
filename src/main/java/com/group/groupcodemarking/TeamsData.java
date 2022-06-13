package com.group.groupcodemarking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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


    public List<Integer> getTeamScores() {
        List<Integer> values = new ArrayList<>();
        teamMessageSent.forEach(e -> {
            values.add(e.getValue() + filesUploaded.stream().filter(f -> f.getName().equals(e.getName())).findFirst().get().getValue()
                    + timeSpentInCalls.stream().filter(f -> f.getName().equals(e.getName())).findFirst().get().getValue());
        });

        double sum = values.stream()
                .mapToDouble(a -> a)
                .sum();

        if (sum > 800) {
            return MathHelper.normaliseList(values, 60, 100);
        } else if (sum > 700) {
            return MathHelper.normaliseList(values, 50, 90);
        } else if (sum > 550) {
            return MathHelper.normaliseList(values, 40, 80);
        }  else if (sum > 350) {
            return MathHelper.normaliseList(values, 30, 70);
        }  else if (sum > 200) {
            return MathHelper.normaliseList(values, 20, 60);
        }  else if (sum > 150) {
            return MathHelper.normaliseList(values, 10, 50);
        } else {
            return MathHelper.normaliseList(values, 1, 35);
        }
    }

}