import React from 'react';
import {PieChart, Pie, Sector, Cell, ResponsiveContainer, Tooltip} from 'recharts';

export const PieChartComponent = ({data, title}) => {

    let renderLabel = function (entry) {
        return entry.name;
    }
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#BF40BF'];

    return (
        data.length > 0 ?
            (<div style={{width: '500px', height: '250px'}}>
            <h6>{title}</h6>
            <ResponsiveContainer>
                <PieChart width={500} height={400}>
                    <Pie data={data} dataKey="value" cx="50%" cy="50%" outerRadius={80} fill="#8884d8"
                         label={renderLabel} isAnimationActive={false}>
                        {
                            data.sort().map((entry, index) => <Cell fill={COLORS[index % COLORS.length]}/>)
                        }
                    </Pie>
                    <Tooltip/>
                </PieChart>
            </ResponsiveContainer>
        </div>) : <div />
    )
}

