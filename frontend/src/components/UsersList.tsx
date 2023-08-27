import React from 'react';
import { useQuery } from 'react-query';
import { LoadingPage } from './LoadingComponents';

export const UsersList: React.FC = () => {
  const { error, data } = useQuery({
    queryKey: 'usersList',
    queryFn: () => fetch('/api/users').then((response) => response.json())
  });

  if (error || !data) return <LoadingPage />;

  return (
    <div>{data.map((x: { id: number; username: string }) => x.username)}</div>
  );
};
