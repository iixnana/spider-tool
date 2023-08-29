import AuthService from '../services/authService';

function downloadBlobAsFile(blob: Blob, fileName: string) {
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
}

// @ts-ignore
export const downloadFile = ({ data, fileName, fileType }) => {
    // Create a blob with the data we want to download as a file
    const blob = new Blob([data], { type: fileType });
    // Create an anchor element and dispatch a click event on it
    // to trigger a download
    downloadBlobAsFile(blob, fileName);
};

export function DownloadProblemFile(
    filename: string,
    appendToFilename: string
) {
    return fetch(`/api/files/${filename}`, {
        headers: { Authorization: AuthService.getAuthHeader() }
    })
        .then((response) => response.json())
        .then((data) =>
            downloadFile({
                data: JSON.stringify(data),
                fileName: `${filename}${appendToFilename}.json`,
                fileType: 'text/json'
            })
        );
}

export function DownloadFileFromServer(
    filename?: string,
    appendToFilename?: string
) {
    return fetch(`/api/files/file/${filename}`, {
        headers: { Authorization: AuthService.getAuthHeader() }
    })
        .then((response) => response.blob())
        .then((blob) =>
            downloadBlobAsFile(
                blob,
                (filename || 'undefined') + (appendToFilename || '')
            )
        );
}
