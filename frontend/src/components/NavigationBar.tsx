import { Link } from 'react-router-dom';
import React from 'react';
import { useCookies } from 'react-cookie';

export const NavigationBar: React.FC = () => {
    const [cookies] = useCookies(['user']);

    if (cookies.user === undefined) {
        return (
            <nav className="navbar navbar-expand navbar-dark bg-dark">
                <a href="/login" className="navbar-brand">
                    Home
                </a>
            </nav>
        );
    }

    return (
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
                    <Link to={'/logout'} className="nav-link">
                        Logout
                    </Link>
                </li>
            </div>
        </nav>
    );
};