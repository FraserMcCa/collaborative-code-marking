package com.group.groupcodemarking;

import com.mashape.unirest.http.exceptions.UnirestException;
import net.minidev.json.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class ContributionController {

    CollaborationDAO collaborationDAO = new CollaborationDAO();
    RepoData cachedRepoData;
    JiraData jiraTickets;
    TeamsData teamsDataCache;

    @GetMapping("/api/hello")
    public List<List<Repo>> hello() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        return Arrays.asList(collaborationDAO.getAllGithubRepos(), collaborationDAO.getAllJiraProjects(), collaborationDAO.getAllTeamsProjects());
    }

    @GetMapping("/api/repoData")
    public RepoData repoData(String repo) throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        cachedRepoData = new RepoData(collaborationDAO.getPullComments(repo), collaborationDAO.getCommits(repo), collaborationDAO.getCodeScan(repo));
        return cachedRepoData;
    }

    @GetMapping("/api/jiraData")
    public JiraData jiraTickets(String repo) throws IOException, InterruptedException, ParseException, UnirestException, URISyntaxException {

        jiraTickets = collaborationDAO.getJiraTickets(repo);
        return jiraTickets;
    }

    @GetMapping("/api/teamsData")
    public TeamsData teamsData(String repo) throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        Thread.sleep(1000);
        List<Committer> teamMessagesSent = Arrays.asList(new Committer("Sydney Bojsza", 184), new Committer("Claire Maunders", 86), new Committer("Dylan Gould", 35), new Committer("Fraser Thomas David McCallum", 120));
        List<Committer> filesUploaded = Arrays.asList(new Committer("Sydney Bojsza", 22), new Committer("Claire Maunders", 20), new Committer("Dylan Gould", 11), new Committer("Fraser Thomas David McCallum", 9));
        List<Committer> timeSpentInCalls = Arrays.asList(new Committer("Sydney Bojsza", 322), new Committer("Claire Maunders", 230), new Committer("Dylan Gould", 250), new Committer("Fraser Thomas David McCallum", 340));
        teamsDataCache = new TeamsData(teamMessagesSent, filesUploaded, timeSpentInCalls);
        returnScores();
        return teamsDataCache;
    }

    //    @GetMapping("/api/score")
    public List<Integer> returnScores() {
        List<Integer> list4 = new ArrayList<>();
        List<Integer> list1 = teamsDataCache.getTeamScores();
        List<Integer> list2 = jiraTickets.getTeamScores();
        List<Integer> list3 = cachedRepoData.getTeamScores();
        for (int i = 0; i < list1.size(); i++) {
            list4.add(list1.get(i) + list2.get(i) + list3.get(i));
        }
        System.out.println("WAHOO");
        list4.forEach(System.out::println);
        return list4;
    }


}
