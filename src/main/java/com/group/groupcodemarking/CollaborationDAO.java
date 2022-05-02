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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.stream.Collectors;

public class CollaborationDAO {
    private static final String githubAuth = "Bearer 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";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public List<String> getGitHubDetails() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        String url = "https://api.github.com/user/repos";
        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

        JSONArray jsonArray = (JSONArray) new JSONParser().parse(response.getBody());
        return jsonArray.stream().map(e -> (JSONObject) e).map(e -> e.get("name").toString()).collect(Collectors.toList());
    }

    public void getCodeScan() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        String url = "https://api.github.com/FraserMcCa/group-code-marking/code-scanning/alerts";

        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.getBody());
    }

    //Found it all avaliable here https://docs.github.com/en/rest/metrics/statistics#get-all-contributor-commit-activity
    public ArrayList<Committer> getCommits(String repo) throws URISyntaxException, IOException, InterruptedException, ParseException, UnirestException {
        Map<String, Integer> commitMap = new HashMap<>();
        String url ="https://api.github.com/repos/FraserMcCa/" + repo + "/commits";

        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", githubAuth)
                .header("Accept", "application/json")
                .asString();

        try {
            JSONArray jsonObject = (JSONArray) new JSONParser().parse(response.getBody());
            for (Object object : jsonObject) {
                JSONObject json = (JSONObject) object;
                JSONObject commit = (JSONObject) json.get("commit");
                JSONObject author = (JSONObject) commit.get("author");
                String authorName = author.getAsString("name");
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
        } catch (ClassCastException e) {
            System.out.println("No Commits Found for Repo: " + repo);
            return null;
        }
    }

    public ArrayList<Committer> getPullComments(String repo) throws URISyntaxException, IOException, InterruptedException, ParseException, UnirestException {

        Map<String, Integer> pullCommentMap = new HashMap<>();
        String url = "https://api.github.com/repos/spacedriveapp/spacedrive/pulls/comments";

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
        pullCommentMap.entrySet().stream().forEach(System.out::println);
        return null;
    }

    public void getJiraTickets() throws URISyntaxException, IOException, InterruptedException, UnirestException {
//        String encoding = Base64.getEncoder().encodeToString((jiraAuth).getBytes("UTF-8"));
        String url = "https://cs356group8.atlassian.net/rest/api/2/search?jql=";

        HttpResponse<JsonNode> response = Unirest.get(url)
                .basicAuth("fraser.mccallum.2018@uni.strath.ac.uk", "6m01T9syR28tvDtepwkW5A93")
                .header("Accept", "application/json")
                .asJson();
//        var request = HttpRequest.newBuilder().uri(url)
//                .
//                .GET()
//                .build();
//        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response);
    }
}
