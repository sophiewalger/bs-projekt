import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface Kunde {
  id: number;
  name: string;
  adresse: string;
  email: string;
}

const KundenListe = () => {
  const [kunden, setKunden] = useState<Kunde[]>([]);
  const [neuerKunde, setNeuerKunde] = useState<Omit<Kunde, 'id'>>({
    name: '',
    adresse: '',
    email: ''
  });

  useEffect(() => {
    loadKunden();
  }, []);

  const loadKunden = async () => {
    try {
      const response = await axios.get('/api/kunden');
      setKunden(response.data);
    } catch (error) {
      console.error('Fehler beim Laden der Kunden:', error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post('/api/kunden', neuerKunde);
      setNeuerKunde({ name: '', adresse: '', email: '' });
      loadKunden();
    } catch (error) {
      console.error('Fehler beim Erstellen des Kunden:', error);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Kundenverwaltung</h1>
      
      {/* Formular für neuen Kunden */}
      <div className="bg-white p-4 rounded shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Neuen Kunden anlegen</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1">Name:</label>
            <input
              type="text"
              value={neuerKunde.name}
              onChange={(e) => setNeuerKunde({...neuerKunde, name: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Adresse:</label>
            <input
              type="text"
              value={neuerKunde.adresse}
              onChange={(e) => setNeuerKunde({...neuerKunde, adresse: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <div>
            <label className="block mb-1">E-Mail:</label>
            <input
              type="email"
              value={neuerKunde.email}
              onChange={(e) => setNeuerKunde({...neuerKunde, email: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Kunde anlegen
          </button>
        </form>
      </div>

      {/* Kundenliste */}
      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="p-4 text-left">Name</th>
              <th className="p-4 text-left">Adresse</th>
              <th className="p-4 text-left">E-Mail</th>
              <th className="p-4 text-left">Aktionen</th>
            </tr>
          </thead>
          <tbody>
            {kunden.map((kunde) => (
              <tr key={kunde.id} className="border-t">
                <td className="p-4">{kunde.name}</td>
                <td className="p-4">{kunde.adresse}</td>
                <td className="p-4">{kunde.email}</td>
                <td className="p-4">
                  <button
                    onClick={() => {/* TODO: Implementieren Sie die Bearbeitung */}}
                    className="text-blue-600 hover:text-blue-800 mr-2"
                  >
                    Bearbeiten
                  </button>
                  <button
                    onClick={() => {/* TODO: Implementieren Sie das Löschen */}}
                    className="text-red-600 hover:text-red-800"
                  >
                    Löschen
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default KundenListe; 