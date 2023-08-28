import React, { useRef } from 'react';

export const Login: React.FC = () => {
    const formRef = useRef<HTMLFormElement>(null);

    const handleSubmit = async (event: {
        preventDefault: () => void;
        target: any;
    }) => {
        event.preventDefault();

        const target = event.target as typeof event.target & {
            username: { value: string };
            password: { value: string };
        };
        const username = target.username.value;
        const password = target.password.value;

        await fetch(`/api/signIn`, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ login: username, password: password })
        }).then((data) => console.log(data));
    };

    return (
        <form ref={formRef} onSubmit={handleSubmit}>
            <div>
                <label>
                    Username:
                    <input type="text" name="username" />
                </label>
            </div>
            <div>
                <label>
                    Password:
                    <input type="password" name="password" />
                </label>
            </div>
            <div>
                <input type="submit" value="Log in" />
            </div>
        </form>
    );
};
