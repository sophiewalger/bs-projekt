// src/services/ApiService.js
import axios from 'axios';

const API_URL = 'http://localhost:8081/resources'; // URL zu deiner REST-API

// Axios Interceptor für globale Fehlerbehandlung
axios.interceptors.response.use(
    response => response,
    error => {
        console.error('API Fehler:', error.response?.data || error.message);
        return Promise.reject(error);
    }
);

export const getCustomers = async () => {
    try {
        const response = await axios.get(`${API_URL}/customers`);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Abrufen der Kunden:', error);
        throw error;
    }
};

export const createCustomer = async (customerData) => {
    try {
        // Wrap customer data in the expected format
        const wrappedData = {
            customer: customerData
        };
        const response = await axios.post(`${API_URL}/customers`, wrappedData);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Erstellen des Kunden:', error);
        throw error;
    }
};

export const updateCustomer = async (id, customerData) => {
    try {
        // Wrap customer data in the expected format
        const wrappedData = {
            customer: customerData
        };
        const response = await axios.put(`${API_URL}/customers/${id}`, wrappedData);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Aktualisieren des Kunden:', error);
        throw error;
    }
};

export const deleteCustomer = async (id) => {
    try {
        await axios.delete(`${API_URL}/customers/${id}`);
    } catch (error) {
        console.error('Fehler beim Löschen des Kunden:', error);
        throw error;
    }
};

export const getReadings = async () => {
    try {
        const response = await axios.get(`${API_URL}/readings`);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Abrufen der Ablesungen:', error);
        throw error;
    }
};

export const createReading = async (readingData) => {
    try {
        // Wrap reading data in the expected format
        const wrappedData = {
            reading: readingData
        };
        const response = await axios.post(`${API_URL}/readings`, wrappedData);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Erstellen der Ablesung:', error);
        throw error;
    }
};

// Weitere Methoden für PUT, DELETE...
