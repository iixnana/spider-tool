import React, { useEffect } from 'react';
import UploadFile from './UploadFile';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';

export const Home: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (!AuthService.isAuthTokenValid()) {
            navigate('/login');
        } else {
            fetch('/api/spider-files', {
                headers: { Authorization: AuthService.getAuthHeader() }
            })
                .then((response) => response.json())
                .then((data) => {
                    console.log(data);
                });
        }
    });

    return (
        <div>
            Hello world!
            <UploadFile />
        </div>
    );
};
