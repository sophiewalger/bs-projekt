import axios, { AxiosError } from 'axios';
import { Kunde, Ablesung, CreateKundeDTO, UpdateKundeDTO, CreateAblesungDTO, UpdateAblesungDTO } from '../types';

const api = axios.create({
  baseURL: 'http://localhost:8081/resources',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Globaler Error Handler
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.message === 'Network Error') {
      console.error('Backend-Server ist nicht erreichbar. Bitte stellen Sie sicher, dass der Server läuft.');
    }
    return Promise.reject(error);
  }
);

const handleApiError = (error: any, context: string) => {
  if (axios.isAxiosError(error)) {
    if (error.message === 'Network Error') {
      throw new Error(`${context}: Backend-Server ist nicht erreichbar. Bitte stellen Sie sicher, dass der Server läuft.`);
    }
    throw new Error(`${context}: ${error.response?.data?.message || error.message}`);
  }
  throw error;
};

// Hilfsfunktion zum Konvertieren des Backend-Kunden in das Frontend-Format
const convertBackendCustomerToFrontend = (customer: any): Kunde => ({
  id: customer.id,
  name: `${customer.firstName} ${customer.lastName}`,
  adresse: `${customer.street} ${customer.houseNumber}`,
  email: customer.email || '',
});

// Hilfsfunktion zum Konvertieren des Frontend-Kunden in das Backend-Format
const convertFrontendCustomerToBackend = (customer: CreateKundeDTO) => {
  const [firstName, ...lastNameParts] = customer.name.split(' ');
  const lastName = lastNameParts.join(' ');
  const [street, houseNumber] = customer.adresse.split(' ');
  
  return {
    firstName,
    lastName,
    street,
    houseNumber,
    email: customer.email,
    gender: 'UNKNOWN', // Standard-Wert
    birthDate: new Date().toISOString(), // Standard-Wert
    postcode: '00000', // Standard-Wert
    city: 'Unbekannt', // Standard-Wert
  };
};

export const kundenAPI = {
  getAll: async () => {
    try {
      const response = await api.get<any[]>('/customers');
      return response.data.map(convertBackendCustomerToFrontend);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Laden der Kunden');
    }
  },
  getById: async (id: string) => {
    try {
      const response = await api.get<any>(`/customers/${id}`);
      return convertBackendCustomerToFrontend(response.data);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Laden des Kunden');
    }
  },
  create: async (kunde: CreateKundeDTO) => {
    try {
      const backendCustomer = convertFrontendCustomerToBackend(kunde);
      const response = await api.post<any>('/customers', backendCustomer);
      return convertBackendCustomerToFrontend(response.data);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Erstellen des Kunden');
    }
  },
  update: async (id: string, kunde: UpdateKundeDTO) => {
    try {
      const backendCustomer = convertFrontendCustomerToBackend(kunde as CreateKundeDTO);
      const response = await api.put<any>(`/customers/${id}`, backendCustomer);
      return convertBackendCustomerToFrontend(response.data);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Aktualisieren des Kunden');
    }
  },
  delete: async (id: string) => {
    try {
      await api.delete(`/customers/${id}`);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Löschen des Kunden');
    }
  },
};

// Hilfsfunktion zum Konvertieren der Backend-Ablesung in das Frontend-Format
const convertBackendReadingToFrontend = (reading: any): Ablesung => ({
  id: reading.id,
  kundeId: reading.customerId,
  kundeName: reading.customer ? `${reading.customer.firstName} ${reading.customer.lastName}` : '',
  zaehlerstand: reading.meterCount,
  datum: reading.dateOfReading,
  notiz: reading.comment || '',
});

// Hilfsfunktion zum Konvertieren der Frontend-Ablesung in das Backend-Format
const convertFrontendReadingToBackend = (reading: CreateAblesungDTO) => ({
  customerId: reading.kundeId,
  meterCount: reading.zaehlerstand,
  dateOfReading: reading.datum,
  comment: reading.notiz || '',
  meterId: '0', // Standard-Wert
  kindOfMeter: 'WATER', // Standard-Wert
  substitute: false, // Standard-Wert
});

export const ablesungenAPI = {
  getAll: async () => {
    try {
      const response = await api.get<any[]>('/readings');
      return response.data.map(convertBackendReadingToFrontend);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Laden der Ablesungen');
    }
  },
  getById: async (id: string) => {
    try {
      const response = await api.get<any>(`/readings/${id}`);
      return convertBackendReadingToFrontend(response.data);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Laden der Ablesung');
    }
  },
  getByKunde: async (kundeId: string) => {
    try {
      const response = await api.get<any[]>(`/readings?customerId=${kundeId}`);
      return response.data.map(convertBackendReadingToFrontend);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Laden der Kundenablesungen');
    }
  },
  create: async (ablesung: CreateAblesungDTO) => {
    try {
      const backendReading = convertFrontendReadingToBackend(ablesung);
      const response = await api.post<any>('/readings', backendReading);
      return convertBackendReadingToFrontend(response.data);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Erstellen der Ablesung');
    }
  },
  update: async (id: string, ablesung: UpdateAblesungDTO) => {
    try {
      const backendReading = convertFrontendReadingToBackend(ablesung as CreateAblesungDTO);
      const response = await api.put<any>(`/readings/${id}`, backendReading);
      return convertBackendReadingToFrontend(response.data);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Aktualisieren der Ablesung');
    }
  },
  delete: async (id: string) => {
    try {
      await api.delete(`/readings/${id}`);
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Löschen der Ablesung');
    }
  },
};

export const importExportAPI = {
  importData: async (file: File, format: 'json' | 'xml' | 'csv') => {
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('format', format);
      const response = await api.post('/import', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Importieren der Daten');
    }
  },
  exportData: async (format: 'json' | 'xml' | 'csv') => {
    try {
      const response = await api.get('/export', {
        params: { format },
        responseType: 'blob',
      });
      return response.data;
    } catch (error) {
      throw handleApiError(error, 'Fehler beim Exportieren der Daten');
    }
  },
};

export default api; 