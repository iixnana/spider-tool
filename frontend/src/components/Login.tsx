import React, { useRef } from 'react';
import AuthService from '../services/authService';
import { useNavigate } from 'react-router-dom';

export const Login: React.FC = () => {
    const navigate = useNavigate();
    const formRef = useRef<HTMLFormElement>(null);

    const handleSubmit = async (event: {
        preventDefault: () => void;
        target: any;
    }) => {
        event.preventDefault();

        const target = event.target as typeof event.target & {
            username: { value: string };
            password: { value: string };
        };
        const username = target.username.value;
        const password = target.password.value;

        await fetch(`/api/signIn`, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ login: username, password: password })
        })
            .then((response) => response.json())
            .then((data) => {
                try {
                    AuthService.setAuthCookies(data);
                    navigate('/');
                } catch (e) {
                    throw new Error('Incorrect auth data');
                }
            });
    };

    return (
        <form ref={formRef} onSubmit={handleSubmit}>
            <div>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        id="username"
                        name="username"
                        placeholder="Enter username"
                    />
                </div>
            </div>
            <div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        name="password"
                        placeholder="Enter password"
                    />
                </div>
            </div>
            <button type="submit" className="btn btn-primary">
                Log in
            </button>
        </form>
    );
};
