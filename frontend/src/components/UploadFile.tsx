import React, { useState } from 'react';
import AuthService from '../services/authService';
import { LoadingButton } from './LoadingComponents';

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
            if (files[i].size > 1024000) {
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
        <>
            {fileUploadResponse != null && (
                <div className="alert alert-success" role="alert">
                    {fileUploadResponse}. You can reload the page now.
                </div>
            )}
            {!fileSize && (
                <div className="alert alert-danger" role="alert">
                    File size exceeded. It must be below 1024KB.
                </div>
            )}

            <form onSubmit={fileSubmitHandler}>
                <div
                    className="btn-group"
                    role="group"
                    aria-label="Basic example"
                >
                    {(!fileUploadProgress && (
                        <>
                            <button
                                type="button"
                                className={buttonClassName}
                                disabled={fileUploadProgress}
                            >
                                <label
                                    htmlFor="file-upload"
                                    className="custom-file-upload"
                                >
                                    Choose files
                                </label>
                            </button>
                            <input
                                id="file-upload"
                                type="file"
                                multiple
                                onChange={uploadFileHandler}
                            />
                        </>
                    )) || <LoadingButton />}
                    {(!fileUploadProgress && (
                        <button
                            type="submit"
                            className={buttonClassName}
                            disabled={files.length === 0 || fileUploadProgress}
                        >
                            Upload to system
                        </button>
                    )) || <LoadingButton />}
                </div>
            </form>
        </>
    );
};
export default UploadFile;
