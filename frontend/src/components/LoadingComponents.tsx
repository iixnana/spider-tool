import React from 'react';

export const LoadingPage: React.FC = () => {
  return (
    <div className="d-flex justify-content-center">
      <div className="spinner-border" role="status">
        <span className="sr-only" aria-hidden="true">
          Loading...
        </span>
      </div>
    </div>
  );
};

export const LoadingButton: React.FC = () => {
  return (
    <button className="btn btn-primary" type="button" disabled>
      <span
        className="spinner-border spinner-border-sm"
        role="status"
        aria-hidden="true"
      ></span>
      Loading...
    </button>
  );
};
