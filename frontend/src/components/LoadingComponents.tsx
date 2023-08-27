import { FC } from 'react';

export const LoadingPage: FC = () => {
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

export const LoadingButton: FC = () => {
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
