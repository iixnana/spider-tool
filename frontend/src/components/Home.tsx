import React, { useEffect, useState } from 'react';
import UploadFile from './UploadFile';
import { useCookies } from 'react-cookie';
import { Link } from 'react-router-dom';
import { LoadingPage } from './LoadingComponents';

export const Home: React.FC = () => {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [user, setUser] = useState(undefined);
  const [cookies] = useCookies(['XSRF-TOKEN']);

  useEffect(() => {
    setLoading(true);
    fetch('api/user', { credentials: 'include' })
      .then((response) => response.text())
      .then((body) => {
        if (body === '') {
          setAuthenticated(false);
        } else {
          setUser(JSON.parse(body));
          setAuthenticated(true);
        }
        setLoading(false);
      });
  }, [setAuthenticated, setLoading, setUser]);

  const login = () => {
    let port = window.location.port ? ':' + window.location.port : '';
    if (port === ':3000') {
      port = ':8080';
    }
    // redirect to a protected URL to trigger authentication
    window.location.href = `//${window.location.hostname}${port}/api/private`;
  };

  const logout = () => {
    fetch('/api/logout', {
      method: 'POST',
      credentials: 'include',
      headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] }
    })
      .then((res) => res.json())
      .then((response) => {
        window.location.href =
          `${response.logoutUrl}?id_token_hint=${response.idToken}` +
          `&post_logout_redirect_uri=${window.location.origin}`;
      });
  };

  const message = user ? (
    <h2>Welcome!</h2>
  ) : (
    <p>Please log in to manage driving routes.</p>
  );

  const button = authenticated ? (
    <div>
      <button color="link">
        <Link to="/groups">Manage JUG Tour</Link>
      </button>
      <br />
      <button color="link" onClick={logout}>
        Logout
      </button>
    </div>
  ) : (
    <button color="primary" onClick={login}>
      Login
    </button>
  );

  if (loading) {
    return <LoadingPage />;
  }

  return (
    <div>
      {message}
      {button}
      <UploadFile />
    </div>
  );
};
