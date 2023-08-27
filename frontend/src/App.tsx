import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link, Route, Routes } from 'react-router-dom';
import { UsersList } from './components/UsersList';
import { QueryClient, QueryClientProvider } from 'react-query';

const queryClient = new QueryClient();

const App: React.FC = () => {
  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <a href="/" className="navbar-brand">
          Home
        </a>
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={'/users'} className="nav-link">
              Users
            </Link>
          </li>
          <li className="nav-item">
            <Link to={'/etc'} className="nav-link">
              Etc
            </Link>
          </li>
        </div>
      </nav>

      <div className="container mt-3">
        <QueryClientProvider client={queryClient}>
          <Routes>
            <Route path="/" />
            <Route path="/users" element={<UsersList />} />
          </Routes>
        </QueryClientProvider>
      </div>
    </div>
  );
};

export default App;
