import {useQuery} from 'react-query';
import AuthService from '../services/authService';
import ISpiderData from '../types/SpiderData';
import {LoadingPage} from './LoadingComponents';
import React from 'react';
import UploadFile from './UploadFile';
import moment from 'moment';

// @ts-ignore
const downloadFile = ({ data, fileName, fileType }) => {
    // Create a blob with the data we want to download as a file
    const blob = new Blob([data], { type: fileType });
    // Create an anchor element and dispatch a click event on it
    // to trigger a download
    const a = document.createElement('a');
    a.download = fileName;
    a.href = window.URL.createObjectURL(blob);
    const clickEvt = new MouseEvent('click', {
        view: window,
        bubbles: true,
        cancelable: true
    });
    a.dispatchEvent(clickEvt);
    a.remove();
};

function DownloadProblemFile(filename: string) {
    return fetch(`/api/files/${filename}`, {
        headers: { Authorization: AuthService.getAuthHeader() }
    })
        .then((response) => response.json())
        .then((data) =>
            downloadFile({
                data: JSON.stringify(data),
                fileName: `${filename}_problem.json`,
                fileType: 'text/json'
            })
        );
}

function DownloadSolutionFile(filename: string) {
    var csv = 'test,test2';

    return downloadFile({
        data: csv,
        fileName: `${filename}_solution.csv`,
        fileType: 'text/csv;charset=utf-8'
    });
}

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
                                <button
                                    type="submit"
                                    className="btn btn-primary"
                                    onClick={() =>
                                        DownloadProblemFile(row.problemFilename)
                                    }
                                >
                                    Download problem
                                </button>
                            </td>
                            <td>
                                {row.solutionFilename}
                                <button
                                    type="submit"
                                    className="btn btn-primary"
                                    onClick={() =>
                                        DownloadSolutionFile(
                                            row.problemFilename
                                        )
                                    }
                                >
                                    Download solution
                                </button>
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
