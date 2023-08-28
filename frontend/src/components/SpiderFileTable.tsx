import { useQuery } from 'react-query';
import AuthService from '../services/authService';
import ISpiderFileData from '../types/SpiderFile';
import { LoadingPage } from './LoadingComponents';
import React from 'react';
import UploadFile from './UploadFile';

export const SpiderFileTable: React.FC = () => {
    const { error, data } = useQuery({
        queryKey: 'spiderFilesList',
        queryFn: () =>
            fetch('/api/spider-files', {
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
                                    createdOn: row.createdOn
                                }) as ISpiderFileData
                        );
                    } else {
                        throw new Error('Incorrect data format');
                    }
                }),
        enabled: AuthService.isAuthTokenValid()
    });

    if (error || !data) return <LoadingPage />;

    return (
        <div>
            <UploadFile />
            <table>
                <tr>
                    <th>ID</th>
                    <th>Created On</th>
                    <th>Problem Filename</th>
                    <th>Solution Filename</th>
                    <th>Author</th>
                </tr>
                {data.map((row) => (
                    <tr>
                        <td>{row.id}</td>
                        <td>{row.createdOn}</td>
                        <td>{row.problemFilename}</td>
                        <td>{row.solutionFilename}</td>
                        <td>{row.author.username}</td>
                    </tr>
                ))}
            </table>
        </div>
    );
};
