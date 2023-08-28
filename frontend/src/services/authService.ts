import { Cookies } from 'react-cookie';
import jwt_decode from 'jwt-decode';
import { IUserDto } from '../types/User';

class AuthService {
    private cookies = new Cookies();
    token = 'token';
    user = 'user';
    expire_after = 3600; // Will expire after 1hr

    setAuthCookies(user: IUserDto) {
        this.cookies.set(this.token, user.token, {
            maxAge: this.expire_after
        });
        this.cookies.set(this.user, user, {
            maxAge: this.expire_after
        });
    }

    getAuthToken() {
        return this.cookies.get(this.token);
    }

    getAuthHeader() {
        return `Bearer ${this.getAuthToken()}`;
    }

    getUserToken() {
        return this.cookies.get(this.user);
    }

    isAuthTokenValid() {
        if (!this.getAuthToken()) return false;
        const decodedToken = jwt_decode(this.getAuthToken());
        const currentDate = new Date();

        // @ts-ignore
        return decodedToken.exp * this.expire_after >= currentDate.getTime();
    }

    removeAuthCookies() {
        this.cookies.remove(this.token);
        this.cookies.remove(this.user);
    }
}

export default new AuthService();
