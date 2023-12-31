import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';
import { SpiderDataList } from './SpiderDataList';

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

    // TODO: Should refresh after files were uploaded
    // TODO: Add nicer formatting of page
    return (
        <>
            <h2>Hello {AuthService.getUserToken()['firstName']}!</h2>
            <SpiderDataList />
        </>
    );
};
