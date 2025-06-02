// src/components/ReadingList.js
import React, { useEffect, useState } from 'react';
import { getReadings, createReading } from '../services/ApiService';
import ReadingForm from './ReadingForm';

const ReadingList = () => {
    const [readings, setReadings] = useState([]);

    useEffect(() => {
        fetchReadings();
    }, []);

    const fetchReadings = async () => {
        const data = await getReadings();
        setReadings(data);
    };

    const handleCreateReading = async (reading) => {
        await createReading(reading);
        fetchReadings();
    };

    return (
        <div>
            <h2>Ablesungen</h2>
            <ReadingForm onSubmit={handleCreateReading} />
            <ul>
                {readings.map(reading => (
                    <li key={reading.id}>
                        {reading.meterId} - {reading.meterCount} - {reading.dateOfReading}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ReadingList;