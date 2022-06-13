package com.group.groupcodemarking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class CollaborationDAO {
    private static final String githubAuth = "Bearer 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";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<String> jiraKeys = new ArrayList<>();


    public List<Repo> getAllGithubRepos() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        String url = "https://api.github.com/user/repos";
        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

        JSONArray jsonArray = (JSONArray) new JSONParser().parse(response.getBody());
        return jsonArray.stream().map(e -> (JSONObject) e).map(e -> new Repo(e.get("url").toString(), e.get("name").toString())).collect(Collectors.toList());
    }

    public List<Repo> getAllJiraProjects() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        String url = "https://cs356group8.atlassian.net/rest/api/2/project";

        HttpResponse<String> response = Unirest.get(url)
                .basicAuth("fraser.mccallum.2018@uni.strath.ac.uk", "1ik5gchZM1znlqdmk6xGB860")
                .header("Accept", "application/json")
                .asString();

        JSONArray jsonArray = (JSONArray) new JSONParser().parse(response.getBody());
        return jsonArray.stream().map(e -> (JSONObject) e).map(e -> new Repo(e.get("name").toString(), e.get("name").toString())).collect(Collectors.toList());
    }

    public List<Repo> getAllTeamsProjects() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        return Arrays.asList(new Repo("CS356-Group8", "CS356-Group8"), new Repo("Strathclyde 2018 Cohort", "Strathclyde 2018 Cohort"));
    }

    public ArrayList<Violation> getCodeScan(String repo) throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
//        String url = "https://api.github.com/repos/FraserMcCa/group-code-marking/code-scanning/alerts";
        String url = repo + "/code-scanning/alerts";

        if (url.equals("https://api.github.com/repos/sydneybojsza/CS356-Group8/code-scanning/alerts")) {
            url = "https://api.github.com/repos/FraserMcCa/group-code-marking/code-scanning/alerts";
        }

        ArrayList<Violation> violationList = new ArrayList<>();
        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

        try {

            JSONArray jsonObject = (JSONArray) new JSONParser().parse(response.getBody());

            for (Object object : jsonObject) {
                JSONObject json = (JSONObject) object;
                JSONObject ruleViolation = (JSONObject) json.get("rule");
                JSONObject recentVio = (JSONObject) json.get("most_recent_instance");
                JSONObject location = (JSONObject) recentVio.get("location");
                violationList.add(new Violation(violationList.size() + 1, ruleViolation.getAsString("description"), location.getAsString("path").split("/")[location.getAsString("path").split("/").length - 1]));
//            System.out.println(violationList.get(violationList.size() -1).toString());
            }
        } catch(ClassCastException e) {
            System.out.println("no results found");
        }
        return violationList;
    }

    //Found it all avaliable here https://docs.github.com/en/rest/metrics/statistics#get-all-contributor-commit-activity
    public ArrayList<Committer> getCommits(String repo) throws URISyntaxException, IOException, InterruptedException, ParseException, UnirestException {
        Map<String, Integer> commitMap = new HashMap<>();
        String url = repo + "/commits";

        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

            JSONArray jsonObject = (JSONArray) new JSONParser().parse(response.getBody());
            for (Object object : jsonObject) {
                JSONObject json = (JSONObject) object;
                JSONObject commit = (JSONObject) json.get("commit");
                JSONObject author = (JSONObject) commit.get("author");
                String authorName = author.getAsString("name");
                if (authorName.equals("cmaund200")) {authorName = "Claire";}
                if (authorName.equals("sydneybojsza")) {authorName = "Sydney";}
                if (authorName.equals("FraserMcC")) {authorName = "Fraser";}
                if (commitMap.containsKey(authorName)) {
                    commitMap.put(authorName, commitMap.get(authorName) + 1);
                } else {
                    commitMap.put(authorName, 1);
                }
            }
            ArrayList<Committer> committerList = new ArrayList<>();
            for (String s : commitMap.keySet()) {
                committerList.add(new Committer(s, commitMap.get(s)));
            }
            return committerList;

    }

    public List<Committer> getPullComments(String repo) throws URISyntaxException, IOException, InterruptedException, ParseException, UnirestException {

        System.out.println(repo);
        Map<String, Integer> pullCommentMap = new HashMap<>();
//        String url = "https://api.github.com/repos/spacedriveapp/spacedrive/pulls/comments";
        String url = repo + "/pulls/comments";

        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

        JSONArray jsonObject = (JSONArray) new JSONParser().parse(response.getBody());
        for (Object object : jsonObject) {
            JSONObject json = (JSONObject) object;
            JSONObject commit = (JSONObject) json.get("user");
            String author = (String) commit.get("login");
            if (pullCommentMap.containsKey(author)) {
                pullCommentMap.put(author, pullCommentMap.get(author) + 1);
            } else {
                pullCommentMap.put(author, 1);
            }
        }
        ArrayList<Committer> pullCommentList = new ArrayList<>();
        for (String s : pullCommentMap.keySet()) {
            pullCommentList.add(new Committer(s, pullCommentMap.get(s)));
        }
        return pullCommentList;
    }

    //TODO GET TICKETS CREATED
    public JiraData getJiraTickets(String repo) throws IOException, InterruptedException, UnirestException, ParseException {
//        String encoding = Base64.getEncoder().encodeToString((jiraAuth).getBytes("UTF-8"));
        String url = "https://cs356group8.atlassian.net/rest/api/2/search?jql=project=" + repo + "&maxResults=1000";
        Map<String, Integer> creatorNames = new HashMap<>();
        Map<String, Integer> assigneeName = new HashMap<>();

        System.out.println(url);


        try {
            HttpResponse<String> response = Unirest.get(url)
                    .basicAuth("fraser.mccallum.2018@uni.strath.ac.uk", "1ik5gchZM1znlqdmk6xGB860")
                    .header("Accept", "application/json")
                    .asString();


        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.getBody());

        JSONArray jsonArray = (JSONArray) jsonObject.get("issues");

        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONObject jiraFields = (JSONObject) json.get("fields");
            jiraKeys.add(json.getAsString("id"));
            JSONObject assigneeObject = (JSONObject) jiraFields.get("assignee");
            if (assigneeObject != null) {
                String assignee = assigneeObject.getAsString("displayName");
                JSONObject status = (JSONObject) jiraFields.get("status");
                String statusString =  status.getAsString("name");
                if (statusString.equals("Done")) {
                    if (assigneeName.containsKey(assignee)) {
                        assigneeName.put(assignee, assigneeName.get(assignee) + 1);
                    } else {
                        assigneeName.put(assignee, 1);
                    }
                }
            }

            JSONObject creatorObject = (JSONObject) jiraFields.get("creator");
            String creator = creatorObject.getAsString("displayName");
            if (creatorNames.containsKey(creator)) {
                creatorNames.put(creator, creatorNames.get(creator) + 1);
            } else {
                creatorNames.put(creator, 1);
            }



        }

        ArrayList<Committer> creatorList = new ArrayList<>();
        for (String s : creatorNames.keySet()) {
            creatorList.add(new Committer(s, creatorNames.get(s)));
        }

        ArrayList<Committer> ticketsCompletedList = new ArrayList<>();
        for (String s : assigneeName.keySet()) {
            ticketsCompletedList.add(new Committer(s, assigneeName.get(s)));
        }



//        getJiraTransitions();
        return new JiraData(creatorList, ticketsCompletedList);
        } catch (Exception e) {
            return new JiraData(new ArrayList<>(), new ArrayList<>());
        }
    }

    public List<Committer> getJiraTransitions() throws URISyntaxException, IOException, InterruptedException, UnirestException, ParseException {
        String url = "https://cs356group8.atlassian.net/rest/api/2/issue/";
        Map<String, Integer> pullCommentMap = new HashMap<>();

        System.out.println(url);


        for (String jiraKey: jiraKeys) {
            HttpResponse<String> response = Unirest.get(url + jiraKey)
                    .basicAuth("fraser.mccallum.2018@uni.strath.ac.uk", "1ik5gchZM1znlqdmk6xGB860")
                    .header("Accept", "application/json")
                    .asString();

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.getBody());

            System.out.println(jsonObject);
        }


//        for (Object object : jsonArray) {
//            JSONObject json = (JSONObject) object;
//            JSONObject jiraFields = (JSONObject) json.get("fields");
//            JSONObject assigneeObject = (JSONObject) jiraFields.get("assignee");
//            if (assigneeObject != null) {
//                String assignee = assigneeObject.getAsString("displayName");
//                JSONObject status = (JSONObject) jiraFields.get("status");
//                String statusString =  status.getAsString("name");
//                if (statusString.equals("Done")) {
//                    if (pullCommentMap.containsKey(assignee)) {
//                        pullCommentMap.put(assignee, pullCommentMap.get(assignee) + 1);
//                    } else {
//                        pullCommentMap.put(assignee, 1);
//                    }
//                }
//            }
//
//
//        }
//
//        ArrayList<Committer> pullCommentList = new ArrayList<>();
//        for (String s : pullCommentMap.keySet()) {
//            pullCommentList.add(new Committer(s, pullCommentMap.get(s)));
//        }
        return null;

    }

    public List<String> getJiraKeys() {
        return jiraKeys;
    }

    public void setJiraKeys(List<String> jiraKeys) {
        this.jiraKeys = jiraKeys;
    }
}
