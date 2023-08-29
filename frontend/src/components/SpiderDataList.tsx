import { useQuery } from 'react-query';
import AuthService from '../services/authService';
import ISpiderData from '../types/SpiderData';
import { LoadingObj, LoadingPage } from './LoadingComponents';
import React from 'react';
import UploadFile from './UploadFile';
import moment from 'moment';
import {
    DownloadFileFromServer,
    DownloadProblemFile
} from '../utils/DownloadFileUtil';

export const SpiderDataList: React.FC = () => {
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

    return (
        <div>
            <UploadFile />
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Created on</th>
                        <th scope="col">Last updated on</th>
                        <th scope="col">Problem</th>
                        <th scope="col">Solution</th>
                        <th scope="col">Author</th>
                    </tr>
                </thead>

                <tbody>
                    {data.map((row, i) => (
                        <tr key={i}>
                            <th scope="row">{row.id}</th>
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
                            <td>
                                <div
                                    className="btn-group"
                                    role="group"
                                    aria-label="Basic example"
                                >
                                    <button
                                        type="submit"
                                        className="btn btn-primary"
                                        onClick={() =>
                                            DownloadFileFromServer(
                                                row.problemFilename,
                                                ''
                                            )
                                        }
                                    >
                                        Original
                                    </button>
                                    <button
                                        type="submit"
                                        className="btn btn-primary"
                                        onClick={() =>
                                            DownloadProblemFile(
                                                row.problemFilename,
                                                '_problem'
                                            )
                                        }
                                    >
                                        Spider format
                                    </button>
                                </div>
                            </td>
                            <td>
                                {(row.solutionFilename && (
                                    <div
                                        className="btn-group"
                                        role="group"
                                        aria-label="Basic example"
                                    >
                                        <button
                                            type="submit"
                                            className="btn btn-primary"
                                            onClick={() =>
                                                DownloadFileFromServer(
                                                    row.solutionFilename,
                                                    ''
                                                )
                                            }
                                        >
                                            Original solution
                                        </button>
                                        <button
                                            type="submit"
                                            className="btn btn-primary"
                                            onClick={() =>
                                                DownloadFileFromServer(
                                                    row.solutionFilename +
                                                        '_csv',
                                                    ''
                                                )
                                            }
                                        >
                                            CSV
                                        </button>
                                    </div>
                                )) || <LoadingObj />}
                            </td>
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
