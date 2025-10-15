import { CountTypeEnum } from "./count-type";  

export interface Count {
    id: number;
    name: string;
    email: string;
    password: string;
    type: CountTypeEnum;
}