package com.group.groupcodemarking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepoData implements Serializable {
    private List<Committer> pullComments;
    private List<Committer> committerList;
    private List<Violation> violationList;

    public RepoData(List<Committer> pullComments, List<Committer> committerList, List<Violation> violationList) {
        this.pullComments = pullComments;
        this.committerList = committerList;
        this.violationList = violationList;
    }


    public List<Committer> getPullComments() {
        return pullComments;
    }

    public void setPullComments(List<Committer> pullComments) {
        this.pullComments = pullComments;
    }

    public List<Committer> getCommitterList() {
        return committerList;
    }

    public void setCommitterList(List<Committer> committerList) {
        this.committerList = committerList;
    }

    public List<Violation> getViolationList() {
        return violationList;
    }

    public void setViolationList(List<Violation> violationList) {
        this.violationList = violationList;
    }


    public List<Integer> getTeamScores() {
        List<Integer> values = new ArrayList<>();
        committerList.forEach(e -> {
            if (pullComments.size() > 0) {
                values.add(e.getValue() + pullComments.stream().filter(f -> f.getName().equals(e.getName())).findFirst().get().getValue());
            } else {
                values.add(e.getValue());
            }
        });
        double sum = values.stream()
                .mapToDouble(a -> a)
                .sum();

        if (sum > 50) {
            return MathHelper.normaliseList(values, 60, 100);
        } else if (sum > 40) {
            return MathHelper.normaliseList(values, 50, 90);
        } else if (sum > 30) {
            return MathHelper.normaliseList(values, 40, 80);
        }  else if (sum > 20) {
            return MathHelper.normaliseList(values, 30, 70);
        }  else if (sum > 10) {
            return MathHelper.normaliseList(values, 20, 60);
        }  else if (sum > 5) {
            return  MathHelper.normaliseList(values, 10, 50);
        } else {
            return  MathHelper.normaliseList(values, 1, 35);
        }
    }

}
