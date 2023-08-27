import React from 'react';
import { useQuery } from 'react-query';
import { LoadingPage } from './LoadingComponents';
import IUserData from '../types/User';

export const UsersList: React.FC = () => {
  const { error, data } = useQuery({
    queryKey: 'usersList',
    queryFn: () =>
      fetch('/api/users')
        .then((response) => response.json())
        .then((data) => {
          if (
            Array.isArray(data) &&
            data.every((row) => typeof row.username === 'string')
          ) {
            return data.map(
              (row) =>
                ({
                  id: row.id,
                  username: row.username
                }) as IUserData
            );
          } else {
            throw new Error('Incorrect data format');
          }
        })
  });

  if (error || !data) return <LoadingPage />;

  return <div>{data.map((x: IUserData) => x.username)}</div>;
};
