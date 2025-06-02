import React, { useState } from 'react';
import axios from 'axios';

const ImportExport = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [importFormat, setImportFormat] = useState<'json' | 'xml' | 'csv'>('json');
  const [exportFormat, setExportFormat] = useState<'json' | 'xml' | 'csv'>('json');

  const handleFileUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setSelectedFile(event.target.files[0]);
    }
  };

  const handleImport = async () => {
    if (!selectedFile) return;

    const formData = new FormData();
    formData.append('file', selectedFile);
    formData.append('format', importFormat);

    try {
      await axios.post('/api/import', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      alert('Import erfolgreich!');
    } catch (error) {
      console.error('Fehler beim Import:', error);
      alert('Fehler beim Import der Daten');
    }
  };

  const handleExport = async () => {
    try {
      const response = await axios.get(`/api/export?format=${exportFormat}`, {
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `export.${exportFormat}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error('Fehler beim Export:', error);
      alert('Fehler beim Export der Daten');
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Import/Export</h1>

      {/* Import Section */}
      <div className="bg-white p-4 rounded shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Daten importieren</h2>
        <div className="space-y-4">
          <div>
            <label className="block mb-1">Format auswählen:</label>
            <select
              value={importFormat}
              onChange={(e) => setImportFormat(e.target.value as 'json' | 'xml' | 'csv')}
              className="w-full p-2 border rounded"
            >
              <option value="json">JSON</option>
              <option value="xml">XML</option>
              <option value="csv">CSV</option>
            </select>
          </div>
          <div>
            <label className="block mb-1">Datei auswählen:</label>
            <input
              type="file"
              onChange={handleFileUpload}
              accept={`.${importFormat}`}
              className="w-full p-2 border rounded"
            />
          </div>
          <button
            onClick={handleImport}
            disabled={!selectedFile}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:bg-gray-400"
          >
            Importieren
          </button>
        </div>
      </div>

      {/* Export Section */}
      <div className="bg-white p-4 rounded shadow">
        <h2 className="text-xl font-semibold mb-4">Daten exportieren</h2>
        <div className="space-y-4">
          <div>
            <label className="block mb-1">Format auswählen:</label>
            <select
              value={exportFormat}
              onChange={(e) => setExportFormat(e.target.value as 'json' | 'xml' | 'csv')}
              className="w-full p-2 border rounded"
            >
              <option value="json">JSON</option>
              <option value="xml">XML</option>
              <option value="csv">CSV</option>
            </select>
          </div>
          <button
            onClick={handleExport}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Exportieren
          </button>
        </div>
      </div>
    </div>
  );
};

export default ImportExport; 