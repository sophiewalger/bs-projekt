import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface Ablesung {
  id: number;
  kundeId: number;
  kundeName: string;
  datum: string;
  zaehlerstand: number;
  notiz?: string;
}

const AblesungenListe = () => {
  const [ablesungen, setAblesungen] = useState<Ablesung[]>([]);
  const [kunden, setKunden] = useState<{ id: number; name: string }[]>([]);
  const [neueAblesung, setNeueAblesung] = useState<Omit<Ablesung, 'id' | 'kundeName'>>({
    kundeId: 0,
    datum: new Date().toISOString().split('T')[0],
    zaehlerstand: 0,
    notiz: ''
  });

  useEffect(() => {
    loadAblesungen();
    loadKunden();
  }, []);

  const loadAblesungen = async () => {
    try {
      const response = await axios.get('/api/ablesungen');
      setAblesungen(response.data);
    } catch (error) {
      console.error('Fehler beim Laden der Ablesungen:', error);
    }
  };

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
      await axios.post('/api/ablesungen', neueAblesung);
      setNeueAblesung({
        kundeId: 0,
        datum: new Date().toISOString().split('T')[0],
        zaehlerstand: 0,
        notiz: ''
      });
      loadAblesungen();
    } catch (error) {
      console.error('Fehler beim Erstellen der Ablesung:', error);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Ablesungen</h1>

      {/* Formular für neue Ablesung */}
      <div className="bg-white p-4 rounded shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Neue Ablesung erfassen</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1">Kunde:</label>
            <select
              value={neueAblesung.kundeId}
              onChange={(e) => setNeueAblesung({...neueAblesung, kundeId: Number(e.target.value)})}
              className="w-full p-2 border rounded"
              required
            >
              <option value="">Kunde auswählen</option>
              {kunden.map((kunde) => (
                <option key={kunde.id} value={kunde.id}>{kunde.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block mb-1">Datum:</label>
            <input
              type="date"
              value={neueAblesung.datum}
              onChange={(e) => setNeueAblesung({...neueAblesung, datum: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Zählerstand:</label>
            <input
              type="number"
              value={neueAblesung.zaehlerstand}
              onChange={(e) => setNeueAblesung({...neueAblesung, zaehlerstand: Number(e.target.value)})}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Notiz:</label>
            <textarea
              value={neueAblesung.notiz}
              onChange={(e) => setNeueAblesung({...neueAblesung, notiz: e.target.value})}
              className="w-full p-2 border rounded"
            />
          </div>
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Ablesung erfassen
          </button>
        </form>
      </div>

      {/* Ablesungsliste */}
      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="p-4 text-left">Kunde</th>
              <th className="p-4 text-left">Datum</th>
              <th className="p-4 text-left">Zählerstand</th>
              <th className="p-4 text-left">Notiz</th>
              <th className="p-4 text-left">Aktionen</th>
            </tr>
          </thead>
          <tbody>
            {ablesungen.map((ablesung) => (
              <tr key={ablesung.id} className="border-t">
                <td className="p-4">{ablesung.kundeName}</td>
                <td className="p-4">{new Date(ablesung.datum).toLocaleDateString()}</td>
                <td className="p-4">{ablesung.zaehlerstand}</td>
                <td className="p-4">{ablesung.notiz}</td>
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

export default AblesungenListe; 