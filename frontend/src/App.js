// src/App.js
import React from 'react';
import CustomerList from './components/CustomerList';
import ReadingList from './components/ReadingList';
import './styles/App.css';

const App = () => {
    return (
        <div className="app-container">
            <CustomerList />
            <ReadingList />
        </div>
    );
};

export default App;