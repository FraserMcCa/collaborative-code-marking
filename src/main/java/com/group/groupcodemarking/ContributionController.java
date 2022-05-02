package com.group.groupcodemarking;

import com.mashape.unirest.http.exceptions.UnirestException;
import net.minidev.json.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ContributionController {

    CollaborationDAO collaborationDAO = new CollaborationDAO();

    @GetMapping("/api/hello")
    public List<String> hello() throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
//        collaborationDAO.getCodeScan();
        collaborationDAO.getJiraTickets();
        return collaborationDAO.getGitHubDetails();
    }

    @GetMapping("/api/commits")
    public ArrayList<Committer> commits(String repo) throws IOException, URISyntaxException, InterruptedException, ParseException, UnirestException {
        collaborationDAO.getPullComments("23412");
        return collaborationDAO.getCommits(repo);
    }
}
