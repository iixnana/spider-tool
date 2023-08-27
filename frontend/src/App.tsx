import React, {useEffect} from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Link, Route, Routes} from "react-router-dom";

const App: React.FC = () => {
    useEffect(() => {
        fetch('/api/users')
            .then(response => response.json())
            .then(json => console.log(json))
    }, []);

    return (
        <div>
            <nav className="navbar navbar-expand navbar-dark bg-dark">
                <a href="/tutorials" className="navbar-brand">
                    Home
                </a>
                <div className="navbar-nav mr-auto">
                    <li className="nav-item">
                        <Link to={"/tutorials"} className="nav-link">
                            Tutorials
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to={"/add"} className="nav-link">
                            Add
                        </Link>
                    </li>
                </div>
            </nav>

            <div className="container mt-3">
                <Routes>
                    <Route path="/"/>
                </Routes>
            </div>
        </div>
    );
}

export default App;
