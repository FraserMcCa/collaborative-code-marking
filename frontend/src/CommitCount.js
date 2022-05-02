import React, {Component, useState, useEffect} from 'react';
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import logo from './logo.svg';
import './App.css';

function CommitCount(props) {
    const sortedCommitters = Object.entries(props.commiters).sort((commiterOne, commiterTwo) => commiterTwo[1] - commiterOne[1]);
    return (
        <div className="App">
            <h3>Commit Count</h3>
            {
                sortedCommitters.map(([key, val]) =>
                    <p key={key}>{key}: {val}</p>
                )
            }
        </div>
    )
}

export default CommitCount;
