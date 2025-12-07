import { ICitizenService } from 'app/shared/model/citizen-service.model';

export interface IRequiredDocument {
  id?: number;
  name?: string;
  description?: string | null;
  mandatory?: boolean;
  orderIndex?: number | null;
  service?: ICitizenService | null;
}

export const defaultValue: Readonly<IRequiredDocument> = {
  mandatory: false,
};
