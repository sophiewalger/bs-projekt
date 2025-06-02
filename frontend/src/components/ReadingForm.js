// src/components/ReadingForm.js
import React, { useState } from 'react';

const ReadingForm = ({ onSubmit }) => {
    const [formData, setFormData] = useState({
        meterId: '',
        kindOfMeter: '',
        meterCount: '',
        dateOfReading: '',
        substitute: false,
        comment: '',
        customerId: ''
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
        setFormData({
            meterId: '',
            kindOfMeter: '',
            meterCount: '',
            dateOfReading: '',
            substitute: false,
            comment: '',
            customerId: ''
        });
    };

    return (
        <form onSubmit={handleSubmit}>
            <input name="meterId" value={formData.meterId} onChange={handleChange} placeholder="Zähler-ID" required />
            <select name="kindOfMeter" value={formData.kindOfMeter} onChange={handleChange} required>
                <option value="">Zählerart</option>
                <option value="HEIZUNG">Heizung</option>
                <option value="STROM">Strom</option>
                <option value="WASSER">Wasser</option>
            </select>
            <input type="number" name="meterCount" value={formData.meterCount} onChange={handleChange} placeholder="Zählerstand" required />
            <input type="date" name="dateOfReading" value={formData.dateOfReading} onChange={handleChange} required />
            <label>
                <input type="checkbox" name="substitute" checked={formData.substitute} onChange={handleChange} />
                Ersatz
            </label>
            <textarea name="comment" value={formData.comment} onChange={handleChange} placeholder="Kommentar"></textarea>
            <input name="customerId" value={formData.customerId} onChange={handleChange} placeholder="Kunden-ID" required />
            <button type="submit">Ablesung speichern</button>
        </form>
    );
};

export default ReadingForm;