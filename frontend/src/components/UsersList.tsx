import React from 'react';
import { useQuery } from 'react-query';
import { LoadingPage } from './LoadingComponents';
import IUserData from '../types/User';
import AuthService from '../services/authService';

export const UsersList: React.FC = () => {
    const { error, data } = useQuery({
        queryKey: 'usersList',
        queryFn: () =>
            fetch('/api/users', {
                headers: { Authorization: AuthService.getAuthHeader() }
            })
                .then((response) => response.json())
                .then((data) => {
                    // TODO: Should be done by OpenApi
                    if (
                        Array.isArray(data) &&
                        data.every((row) => typeof row.username === 'string')
                    ) {
                        return data.map(
                            (row) =>
                                ({
                                    id: row.id,
                                    firstName: row.firstName,
                                    lastName: row.lastName,
                                    username: row.username
                                }) as IUserData
                        );
                    } else {
                        throw new Error('Incorrect data format');
                    }
                })
    });

    if (error || !data) return <LoadingPage />;

    // TODO: create user signup page, user actions
    return (
        <div>
            <button disabled className="btn btn-primary">
                Create user
            </button>
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Firstname</th>
                        <th scope="col">Lastname</th>
                        <th scope="col">Username</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    {data.map((row: IUserData, i) => (
                        <tr key={i}>
                            <th scope="row">{row.id}</th>
                            <td>{row.firstName}</td>
                            <td>{row.lastName}</td>
                            <td>{row.username}</td>
                            <td>
                                <div
                                    className="btn-group"
                                    role="group"
                                    aria-label="Basic example"
                                >
                                    <button
                                        disabled
                                        className="btn btn-primary"
                                    >
                                        Edit user
                                    </button>
                                    <button
                                        disabled
                                        className="btn btn-primary"
                                    >
                                        Delete user
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};
