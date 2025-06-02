// src/components/CustomerForm.js
import React, { useState, useEffect } from 'react';
import '../styles/CustomerForm.css';

const CustomerForm = ({ onSubmit, customer }) => {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        birthDate: '',
        gender: '',
        street: '',
        houseNumber: '',
        postcode: '',
        city: ''
    });

    useEffect(() => {
        if (customer) {
            setFormData({
                firstName: customer.firstName || '',
                lastName: customer.lastName || '',
                birthDate: customer.birthDate || '',
                gender: customer.gender || '',
                street: customer.street || '',
                houseNumber: customer.houseNumber || '',
                postcode: customer.postcode || '',
                city: customer.city || ''
            });
        }
    }, [customer]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="form-grid">
            <div className="form-field">
                <label className="form-label" htmlFor="firstName">Vorname</label>
                <input
                    id="firstName"
                    className="form-input"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="lastName">Nachname</label>
                <input
                    id="lastName"
                    className="form-input"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="birthDate">Geburtsdatum</label>
                <input
                    id="birthDate"
                    className="form-input"
                    type="date"
                    name="birthDate"
                    value={formData.birthDate}
                    onChange={handleChange}
                />
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="gender">Geschlecht</label>
                <select
                    id="gender"
                    className="form-select"
                    name="gender"
                    value={formData.gender}
                    onChange={handleChange}
                >
                    <option value="">Bitte wählen</option>
                    <option value="M">Männlich</option>
                    <option value="F">Weiblich</option>
                    <option value="D">Divers</option>
                </select>
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="street">Straße</label>
                <input
                    id="street"
                    className="form-input"
                    name="street"
                    value={formData.street}
                    onChange={handleChange}
                />
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="houseNumber">Hausnummer</label>
                <input
                    id="houseNumber"
                    className="form-input"
                    name="houseNumber"
                    value={formData.houseNumber}
                    onChange={handleChange}
                />
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="postcode">PLZ</label>
                <input
                    id="postcode"
                    className="form-input"
                    name="postcode"
                    value={formData.postcode}
                    onChange={handleChange}
                />
            </div>

            <div className="form-field">
                <label className="form-label" htmlFor="city">Stadt</label>
                <input
                    id="city"
                    className="form-input"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                />
            </div>

            <div className="form-field" style={{ gridColumn: '1 / -1' }}>
                <button type="submit" className="form-submit">
                    {customer ? 'Aktualisieren' : 'Erstellen'}
                </button>
            </div>
        </form>
    );
};

export default CustomerForm;