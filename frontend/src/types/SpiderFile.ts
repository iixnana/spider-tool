import IUserData from './User';

export default interface ISpiderFileData {
    id?: number;
    problemFilename: string;
    solutionFilename?: string;
    author: IUserData;
    createdOn: string;
}
