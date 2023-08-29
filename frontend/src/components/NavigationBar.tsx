import { Link } from 'react-router-dom';
import React from 'react';
import { useCookies } from 'react-cookie';

export const NavigationBar: React.FC = () => {
    const [cookies] = useCookies(['user']);

    if (cookies.user === undefined) {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <a href="/login" className="navbar-brand">
                    Home
                </a>
            </nav>
        );
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <a href="/" className="navbar-brand">
                Home
            </a>
            <button
                className="navbar-toggler"
                type="button"
                data-toggle="collapse"
                data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
                <span className="navbar-toggler-icon"></span>
            </button>
            <div
                className="collapse navbar-collapse"
                id="navbarSupportedContent"
            >
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item active">
                        <Link to={'/users'} className="nav-link">
                            Users
                        </Link>
                    </li>

                    <li className="nav-item">
                        <Link to={'/logout'} className="nav-link">
                            Logout
                        </Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
};
