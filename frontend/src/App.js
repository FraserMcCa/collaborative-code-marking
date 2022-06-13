import React, {Component, useState, useEffect} from 'react';
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import './App.css';
import {PieChartComponent} from "./PieCharts/PieChartComponent";
import {DataGrid, GridColDef, GridValueGetterParams} from '@mui/x-data-grid';
import {Alert, CircularProgress} from "@mui/material";

function App() {
    const [repos, setRepos] = useState([]);
    const [jiraProjects, setJiraProjects] = useState([]);
    const [teams, setTeams] = useState([]);
    const [committers, setCommitters] = useState([]);
    const [pullComments, setPullComments] = useState([]);
    const [violations, setViolations] = useState([]);
    const [loading, setLoading] = useState(false);
    const [jiraDataFetched, setJiraDataFetched] = useState(false);
    const [jiraTickets, setJiraTickets] = useState([]);
    const [jirasCreated, setJirasCreated] = useState([]);
    const [teamMessagesSent, setTeamMessagesSent] = useState([]);
    const [filesUploaded, setFilesUploaded] = useState([]);
    const [timeSpentInCalls, setTimeSpentInCalls] = useState([]);

    useEffect(() => {
        fetch('/api/hello')
            .then(response => response.text())
            .then(message => {
                let response = JSON.parse(message);
                setRepos(response[0]);
                setJiraProjects(response[1]);
                setTeams(response[2]);
            });
    }, [])

    const onSelect = (value) => {
        setLoading(true);
        fetchRepoInformation(value.value);
    }

    const onSelectJira = (value) => {
        setLoading(true);
        fetchJiraInformation(value.value);
    }

    const onSelectTeams = (value) => {
        setLoading(true);
        fetchTeamsData(value.value);
    }

    const fetchRepoInformation = (repo) => {
        if (repo !== "") {
            fetch('/api/repoData?repo=' + repo)
                .then(response => response.text())
                .then(message => {
                    if (message !== "") {
                        let response = JSON.parse(message);
                        console.log(response);
                        setViolations(response["violationList"]);
                        setCommitters(response["committerList"]);
                        setPullComments(response["pullComments"]);
                    } else {
                        setCommitters([])
                    }
                    setLoading(false);
                });
        }
    }

    const fetchJiraInformation = (repo) => {
        if (repo !== "") {
            fetch('/api/jiraData?repo=' + repo)
                .then(response => response.text())
                .then(message => {
                    if (message !== "") {
                        let response = JSON.parse(message);
                        setJiraDataFetched(true);

                        setJiraTickets(response["jirasCompleted"]);
                        setJirasCreated(response["jirasCreated"]);
                    } else {
                        setJiraDataFetched(false);
                        setJiraTickets([])
                        setJirasCreated([])
                    }
                    setLoading(false);
                });
        }
    }

    const fetchTeamsData = (repo) => {
        if (repo !== "") {
            fetch('/api/teamsData?repo=' + repo)
                .then(response => response.text())
                .then(message => {
                    if (message !== "") {
                        let response = JSON.parse(message);
                        console.log(response);
                        console.log(response);
                        setTeamMessagesSent(response["teamMessageSent"])
                        setFilesUploaded(response["filesUploaded"])
                        setTimeSpentInCalls(response["timeSpentInCalls"])
                    } else {
                        setJiraTickets([])
                    }
                    setLoading(false);
                });
        }
    }

    const columns = [
        {field: 'id', headerName: 'ID', width: 70},
        {field: 'description', headerName: 'Violation', width: 500},
        {field: 'className', headerName: 'Class', width: 250},
    ];

    return (
        <div className="App">
            <div style={{maxWidth: '1500px'}} className="justify-content-center container">
                <h2>Collaborative Code Marking Tool</h2>
                <div className="mt-3" style={{display: 'inline-flex', width: '80%'}}>
                    <div style={{width: '40%'}}>
                        <h5>Please select a Jira Project below:</h5>
                        <Dropdown onChange={onSelectJira} options={jiraProjects}/>
                    </div>
                    <div style={{width: '40%'}}>
                        <h5>Please select a GitHub repo below:</h5>
                        <Dropdown onChange={onSelect} options={repos}/>
                    </div>
                    <div style={{width: '40%'}}>
                        <h5>Please select a Microsoft Team below:</h5>
                        <Dropdown onChange={onSelectTeams} options={teams}/>
                    </div>
                </div>
                {loading && (
                    <div>
                        <h3>Fetching a lot of data....</h3>
                        <CircularProgress size={150}/>
                    </div>
                )}
                {committers.length > 0 && (
                    <div className="row mt-5">
                        <h3>Hover over a pie chunk to see the value</h3>
                        <hr/>
                        <div className="justify-content-center row">
                            <h3>Github Data</h3>
                            <PieChartComponent data={committers} title={"Commits per user"}/>
                            <PieChartComponent data={pullComments} title={"Pull Comments per use"}/>
                            {violations.length > 0 ? (
                                <div style={{height: 400, width: '70%', marginTop: '2%'}}>
                                    <DataGrid
                                        rows={violations}
                                        columns={columns}
                                        pageSize={5}
                                        rowsPerPageOptions={[5]}
                                    />
                                </div>
                            ) : (
                                <div style={{width: '30%', margin: '2%'}}>
                                    <Alert severity="info">Enabling code scans on this repo will allow for automated
                                        code scanning reports <br/> <a
                                            href="https://docs.github.com/en/enterprise-server@3.4/code-security/code-scanning/automatically-scanning-your-code-for-vulnerabilities-and-errors/setting-up-code-scanning-for-a-repository">Information
                                            can be found here</a>
                                    </Alert>
                                </div>
                            )}
                            <hr/>
                        </div>
                    </div>)}
                <div className="justify-content-center row mt-5">
                    {jiraTickets.length > 0 ? (
                        <>
                            <h3>Jira Data</h3>
                            <PieChartComponent data={jiraTickets} title={"Jira Tickets Completed By User"}/>
                            <PieChartComponent data={jirasCreated} title={"Jira Tickets Created By User"}/>
                            <hr/>
                        </>) : jiraDataFetched === true ? (
                        <div style={{width: '30%', margin: '2%'}}>
                            <h3>Jira Data</h3>
                            <Alert severity="warning">No JIRA Data Found for This Project</Alert>
                        </div>
                    ) : <div />}
                </div>

                {teamMessagesSent.length > 0 && (
                    <div className="justify-content-center row mt-5">
                        <h3>Teams Data</h3>
                        <PieChartComponent data={teamMessagesSent} title={"Team Messages Sent By User"}/>
                        <PieChartComponent data={filesUploaded} title={"Documentation Uploads By User"}/>
                        <PieChartComponent data={timeSpentInCalls} title={"Time Spent In Calls By User"}/>
                    </div>)}
            </div>

        </div>
    )
}

export default App;
