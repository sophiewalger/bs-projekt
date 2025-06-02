import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="bg-blue-600 p-4">
      <div className="container mx-auto">
        <div className="flex justify-between items-center">
          <div className="text-white font-bold text-xl">Hausverwaltung</div>
          <div className="space-x-4">
            <Link to="/" className="text-white hover:text-gray-200">Dashboard</Link>
            <Link to="/kunden" className="text-white hover:text-gray-200">Kunden</Link>
            <Link to="/ablesungen" className="text-white hover:text-gray-200">Ablesungen</Link>
            <Link to="/import-export" className="text-white hover:text-gray-200">Import/Export</Link>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 