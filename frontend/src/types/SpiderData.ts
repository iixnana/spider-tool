import IUserData from './User';

export default interface ISpiderData {
    id?: number;
    problemFilename: string;
    solutionFilename?: string;
    author: IUserData;
    createdOn: Date;
    lastUpdatedOn: Date;
}
