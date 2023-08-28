import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';
import { SpiderFileTable } from './SpiderFileTable';

export const Home: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (!AuthService.isAuthTokenValid()) {
            navigate('/login');
        }
    });

    return (
        <div>
            Hello world!
            <SpiderFileTable />
        </div>
    );
};
