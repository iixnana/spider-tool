export default interface IUserData {
    id?: number;
    username: string;
    firstName: string;
    lastName: string;
}

export interface IUserDto {
    id: number;
    login: string;
    firstName: string;
    lastName: string;
    token: string;
}
