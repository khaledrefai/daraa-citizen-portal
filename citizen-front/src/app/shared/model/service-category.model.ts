import { IDirectorate } from 'app/shared/model/directorate.model';

export interface IServiceCategory {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  active?: boolean;
  directorate?: IDirectorate | null;
}

export const defaultValue: Readonly<IServiceCategory> = {
  active: false,
};
