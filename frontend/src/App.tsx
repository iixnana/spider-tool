import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, Routes } from 'react-router-dom';
import { UsersList } from './components/UsersList';
import { QueryClient, QueryClientProvider } from 'react-query';
import { Home } from './components/Home';
import { Login } from './components/Login';
import { Logout } from './components/Logout';
import { NavigationBar } from './components/NavigationBar';

const queryClient = new QueryClient();

const App: React.FC = () => {
    return (
        <div>
            <NavigationBar />
            <div className="container mt-3">
                <QueryClientProvider client={queryClient}>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/users" element={<UsersList />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/logout" element={<Logout />} />
                    </Routes>
                </QueryClientProvider>
            </div>
        </div>
    );
};

export default App;
