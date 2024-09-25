import React from 'react';
import { FaUser, FaLock } from "react-icons/fa";

const course = [
    {
        title: 'Web Development',
        icon: <FaUser />,
    },
    {
        title: 'App Development',
        duration: '2 Hours',
        icon: <FaLock />,
    },
    {
        title: 'UX & UI',
        duration: '2 Hours',
        icon: <FaUser />,
    },
];

const Cards = () => {
    return (
        <div className="card-container">
            {course.map((item, index) => (
                <div className="card" key={index}>
                    <div className="card-cover">
                        {item.icon}
                    </div>
                    <div className="card-title">
                        <h2>{item.title}</h2>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default Cards;
