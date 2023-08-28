import React, { useEffect } from 'react';
import UploadFile from './UploadFile';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';

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
            <UploadFile />
        </div>
    );
};
