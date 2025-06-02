// src/components/CustomerList.js
import React, { useEffect, useState } from 'react';
import { getCustomers, createCustomer, updateCustomer, deleteCustomer } from '../services/ApiService';
import CustomerForm from './CustomerForm';
import './CustomerList.css';

const CustomerList = () => {
    const [customers, setCustomers] = useState([]);
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        setIsLoading(true);
        try {
            const data = await getCustomers();
            setCustomers(data);
        } catch (error) {
            console.error('Fehler beim Laden der Kunden:', error);
        }
        setIsLoading(false);
    };

    const handleCreateOrUpdate = async (customer) => {
        setIsLoading(true);
        try {
            if (selectedCustomer) {
                await updateCustomer(selectedCustomer.id, customer);
            } else {
                await createCustomer(customer);
            }
            setSelectedCustomer(null);
            setIsModalOpen(false);
            await fetchCustomers();
        } catch (error) {
            console.error('Fehler beim Speichern:', error);
        }
        setIsLoading(false);
    };

    const handleEdit = (customer) => {
        setSelectedCustomer(customer);
        setIsModalOpen(true);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Möchten Sie diesen Kunden wirklich löschen?')) {
            setIsLoading(true);
            try {
                await deleteCustomer(id);
                await fetchCustomers();
            } catch (error) {
                console.error('Fehler beim Löschen:', error);
            }
            setIsLoading(false);
        }
    };

    const handleAddNew = () => {
        setSelectedCustomer(null);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setSelectedCustomer(null);
        setIsModalOpen(false);
    };

    const formatDate = (dateString) => {
        if (!dateString) return '-';
        return new Date(dateString).toLocaleDateString('de-DE');
    };

    const filteredCustomers = customers.filter(customer => {
        const searchLower = searchTerm.toLowerCase();
        return (
            customer.firstName?.toLowerCase().includes(searchLower) ||
            customer.lastName?.toLowerCase().includes(searchLower) ||
            customer.city?.toLowerCase().includes(searchLower) ||
            customer.street?.toLowerCase().includes(searchLower)
        );
    });

    return (
        <div className="customer-list-container">
            <h2>
                <button className="add-button" onClick={handleAddNew}>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                    </svg>
                    Kunde hinzufügen
                </button>
            </h2>
            
            {isModalOpen && (
                <div className="modal-overlay" onClick={handleCloseModal}>
                    <div className="modal-content" onClick={e => e.stopPropagation()}>
                        <div className="modal-header">
                            <h3 className="modal-title">
                                {selectedCustomer ? 'Kunde bearbeiten' : 'Neuer Kunde'}
                            </h3>
                            <button className="close-button" onClick={handleCloseModal}>&times;</button>
                        </div>
                        <CustomerForm onSubmit={handleCreateOrUpdate} customer={selectedCustomer} />
                    </div>
                </div>
            )}
            
            <div className="table-container">
                <div className="table-header">
                    <input
                        type="text"
                        placeholder="Kunden suchen..."
                        className="search-input"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
                
                <div className="table-responsive">
                    <table className="customer-table">
                        <thead>
                            <tr>
                                <th>Vorname</th>
                                <th>Nachname</th>
                                <th>Geburtsdatum</th>
                                <th>Geschlecht</th>
                                <th>Straße</th>
                                <th>Hausnummer</th>
                                <th>PLZ</th>
                                <th>Stadt</th>
                                <th>Aktionen</th>
                            </tr>
                        </thead>
                        <tbody>
                            {isLoading ? (
                                <tr>
                                    <td colSpan="9" style={{ textAlign: 'center', padding: '20px' }}>
                                        Laden...
                                    </td>
                                </tr>
                            ) : filteredCustomers.length === 0 ? (
                                <tr>
                                    <td colSpan="9" style={{ textAlign: 'center', padding: '20px' }}>
                                        Keine Kunden gefunden
                                    </td>
                                </tr>
                            ) : (
                                filteredCustomers.map(customer => (
                                    <tr key={customer.id}>
                                        <td>{customer.firstName || '-'}</td>
                                        <td>{customer.lastName || '-'}</td>
                                        <td>{formatDate(customer.birthDate)}</td>
                                        <td>
                                            <span className={`status status-${customer.gender}`}>
                                                {customer.gender === 'M' ? 'Männlich' :
                                                 customer.gender === 'F' ? 'Weiblich' :
                                                 customer.gender === 'D' ? 'Divers' : 'N/A'}
                                            </span>
                                        </td>
                                        <td>{customer.street || '-'}</td>
                                        <td>{customer.houseNumber || '-'}</td>
                                        <td>{customer.postcode || '-'}</td>
                                        <td>{customer.city || '-'}</td>
                                        <td className="action-buttons">
                                            <button 
                                                className="edit-button"
                                                onClick={() => handleEdit(customer)}
                                            >
                                                Bearbeiten
                                            </button>
                                            <button 
                                                className="delete-button"
                                                onClick={() => handleDelete(customer.id)}
                                            >
                                                Löschen
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default CustomerList;
