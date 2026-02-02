import { ICitizenService } from 'app/shared/model/citizen-service.model';

export interface IServiceFormTemplate {
  id?: number;
  name?: string;
  description?: string | null;
  fileContentType?: string;
  file?: string;
  active?: boolean;
  orderIndex?: number | null;
  service?: ICitizenService | null;
}

export const defaultValue: Readonly<IServiceFormTemplate> = {
  active: false,
};
