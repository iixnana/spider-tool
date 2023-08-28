import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';
import { SpiderDataTable } from './SpiderDataTable';

export const Home: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (!AuthService.isAuthTokenValid()) {
            navigate('/login');
        }
    }, [navigate]);

    if (!AuthService.isAuthTokenValid()) {
        return (
            <div>
                Please login
                <Link to="/login" />
            </div>
        );
    }

    return (
        <>
            Hello {AuthService.getUserToken()['firstName']}!
            <SpiderDataTable />
        </>
    );
};
