import React, {Component, useState, useEffect} from 'react';
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import logo from './logo.svg';
import './App.css';
import CommitCount from "./CommitCount";
import {PieChart, Pie, Sector, Cell, ResponsiveContainer, Tooltip} from 'recharts';

const mapValuesToOptions = (values) => values.map(e => {
        return {'value': e, 'label': e}
    }
);

function App() {
    const [message, setMessage] = useState([]);
    const [committers, setCommitters] = useState([]);

    useEffect(() => {
        fetch('/api/hello')
            .then(response => response.text())
            .then(message => {
                setMessage(mapValuesToOptions(JSON.parse(message)));
            });
    }, [])

    const onSelect = (value) => {
        fetchRepoCommits(value.value);
    }

    const fetchRepoCommits = (repo) => {
        if (repo !== "") {
            fetch('/api/commits?repo=' + repo)
                .then(response => response.text())
                .then(message => {
                    if (message !== "") {
                        setCommitters(JSON.parse(message));
                    } else {
                        setCommitters([])
                    }

                });
        }
    }
    const data = [
        {name: 'Group A', value: 400},
        {name: 'Group B', value: 300},
        {name: 'Group C', value: 300},
        {name: 'Group D', value: 200},
    ];

    let renderLabel = function (entry) {
        return entry.name;
    }
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

    return (
        <div className="App">
            <Dropdown onChange={onSelect} options={message}/>
            <div style={{display: 'inline-block'}}>
                <div style={{width: '500px', height: '300px'}}>
                    <h3>Commits per user</h3>
                    <ResponsiveContainer>
                        <PieChart width={400} height={400}>
                            <Pie data={committers} dataKey="value" cx="50%" cy="50%" outerRadius={80} fill="#8884d8"
                                 label={renderLabel} isAnimationActive={false}>
                                {
                                    committers.map((entry, index) => <Cell fill={COLORS[index % COLORS.length]}/>)
                                }
                            </Pie>
                            <Tooltip/>
                        </PieChart>
                    </ResponsiveContainer>
                </div>
            </div>

        </div>
    )
}

export default App;
