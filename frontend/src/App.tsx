import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Layout/Navbar';
import KundenListe from './components/Kunden/KundenListe';
import AblesungenListe from './components/Ablesungen/AblesungenListe';
import ImportExport from './components/ImportExport/ImportExport';
import ErrorBoundary from './components/Layout/ErrorBoundary';

const App = () => {
  return (
    <ErrorBoundary>
      <Router>
        <div className="min-h-screen bg-gray-100">
          <Navbar />
          <main className="py-4">
            <Routes>
              <Route path="/" element={<KundenListe />} />
              <Route path="/kunden" element={<KundenListe />} />
              <Route path="/ablesungen" element={<AblesungenListe />} />
              <Route path="/import-export" element={<ImportExport />} />
            </Routes>
          </main>
        </div>
      </Router>
    </ErrorBoundary>
  );
};

export default App; 