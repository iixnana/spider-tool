import { useQuery } from 'react-query';
import AuthService from '../services/authService';
import ISpiderData from '../types/SpiderData';
import { LoadingPage } from './LoadingComponents';
import React from 'react';
import UploadFile from './UploadFile';
import moment from 'moment';

export const SpiderDataTable: React.FC = () => {
    const { error, data } = useQuery({
        queryKey: 'spiderDataList',
        queryFn: () =>
            fetch('/api/spider-data', {
                headers: { Authorization: AuthService.getAuthHeader() }
            })
                .then((response) => response.json())
                .then((data) => {
                    if (
                        Array.isArray(data) &&
                        data.every(
                            (row) => typeof row.problemFilename === 'string'
                        )
                    ) {
                        return data.map(
                            (row) =>
                                ({
                                    id: row.id,
                                    problemFilename: row.problemFilename,
                                    solutionFilename: row.solutionFilename,
                                    author: row.author,
                                    createdOn: row.createdOn,
                                    lastUpdatedOn: row.createdOn
                                }) as ISpiderData
                        );
                    } else {
                        throw new Error('Incorrect data format');
                    }
                }),
        enabled: AuthService.isAuthTokenValid()
    });

    if (error || !data) return <LoadingPage />;

    data.map((row, i) => console.log(row.createdOn));

    // TODO: Fix createdOn format
    return (
        <div>
            <UploadFile />
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Created on</th>
                        <th>Last updated on</th>
                        <th>Problem filename</th>
                        <th>Solution filename</th>
                        <th>Author</th>
                    </tr>
                </thead>

                <tbody>
                    {data.map((row, i) => (
                        <tr key={i}>
                            <td>{row.id}</td>
                            <td>
                                {moment(row.createdOn).format(
                                    'YYYY-MM-DD HH:mm:ss'
                                )}
                            </td>
                            <td>
                                {moment(row.lastUpdatedOn).format(
                                    'YYYY-MM-DD HH:mm:ss'
                                )}
                            </td>
                            <td>{row.problemFilename}</td>
                            <td>{row.solutionFilename}</td>
                            <td>
                                {row.author.firstName} {row.author.lastName}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};
