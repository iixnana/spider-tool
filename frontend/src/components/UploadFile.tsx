import React, { useState } from 'react';
import AuthService from '../services/authService';

const UploadFile = () => {
    const [files, setFiles] = useState<File[]>([]);
    //state for checking file size
    const [fileSize, setFileSize] = useState(true);
    // for file upload progress message
    const [fileUploadProgress, setFileUploadProgress] = useState(false);
    //for displaying response message
    const [fileUploadResponse, setFileUploadResponse] = useState(null);

    const uploadFileHandler = (event: { target: { files: any } }) => {
        setFiles(event.target.files);
    };

    const fileSubmitHandler = (event: { preventDefault: () => void }) => {
        event.preventDefault();
        setFileSize(true);
        setFileUploadProgress(true);
        setFileUploadResponse(null);

        const formData = new FormData();

        for (let i = 0; i < files.length; i++) {
            if (files[i].size > 5000000) {
                setFileSize(false);
                setFileUploadProgress(false);
                setFileUploadResponse(null);
                return;
            }

            formData.append(`files`, files[i]);
        }

        fetch('/api/files/upload', {
            method: 'POST',
            headers: { Authorization: AuthService.getAuthHeader() },
            body: formData
        })
            .then(async (response) => {
                const isJson = response.headers
                    .get('content-type')
                    ?.includes('application/json');
                const data = isJson && (await response.json());

                // check for error response
                if (!response.ok) {
                    // get error message
                    const error = (data && data.message) || response.status;
                    setFileUploadResponse(data.message);
                    return Promise.reject(error);
                }

                console.log(data.message);
                setFileUploadResponse(data.message);
            })
            .catch((error) => {
                console.error('Error while uploading file', error);
            });
        setFileUploadProgress(false);
    };

    const buttonClassName = `btn ${files.length === 0 && 'btn-primary'} ${
        files.length > 0 && 'btn-success'
    } ${files.length > 0 && !fileSize && 'btn-danger'}`;

    return (
        <form onSubmit={fileSubmitHandler}>
            <div className="btn-group" role="group" aria-label="Basic example">
                <button type="button" className={buttonClassName}>
                    <label htmlFor="file-upload" className="custom-file-upload">
                        Choose files
                    </label>
                </button>

                <input
                    id="file-upload"
                    type="file"
                    multiple
                    onChange={uploadFileHandler}
                />
                <button
                    type="submit"
                    className={buttonClassName}
                    disabled={files.length === 0}
                >
                    Upload
                </button>
            </div>
            {!fileSize && <p style={{ color: 'red' }}>File size exceeded!!</p>}
            {fileUploadProgress && (
                <p style={{ color: 'red' }}>Uploading File(s)</p>
            )}
            {fileUploadResponse != null && (
                <p style={{ color: 'green' }}>{fileUploadResponse}</p>
            )}
        </form>
    );
};
export default UploadFile;
