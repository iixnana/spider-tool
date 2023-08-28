import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import AuthService from '../services/authService';
import { LoadingPage } from './LoadingComponents';

export const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`/api/signOut`, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json',
                Authorization: AuthService.getAuthHeader()
            }
        }).then(() => AuthService.removeAuthCookies());
        navigate('/login');
    });

    return <LoadingPage />;
};
