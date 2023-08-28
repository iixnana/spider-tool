import React, { useEffect } from 'react';
import UploadFile from './UploadFile';
import { useNavigate } from 'react-router-dom';

export const Home: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        console.log('Checking cookies');
        navigate('/login');
    });

    return (
        <div>
            Hello world!
            <UploadFile />
        </div>
    );
};
