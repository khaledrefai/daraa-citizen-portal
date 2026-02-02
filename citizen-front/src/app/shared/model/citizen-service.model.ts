import { IRequiredDocument } from 'app/shared/model/required-document.model';
import { IServiceFormTemplate } from 'app/shared/model/service-form-template.model';
import { IServiceCategory } from 'app/shared/model/service-category.model';
import { EstimatedTimeUnit } from 'app/shared/model/enumerations/estimated-time-unit.model';

export interface ICitizenService {
  id?: number;
  name?: string;
  description?: string;
  estimatedDuration?: number;
  estimatedDurationUnit?: keyof typeof EstimatedTimeUnit;
  requiresPhysicalPresence?: boolean;
  isElectronic?: boolean;
  hasSmartCard?: boolean;
  feesDescription?: string | null;
  serviceLink?: string | null;
  active?: boolean;
  category?: IServiceCategory | null;
  requiredDocuments?: IRequiredDocument[];
  formTemplates?: IServiceFormTemplate[];
}

export const defaultValue: Readonly<ICitizenService> = {
  requiresPhysicalPresence: false,
  isElectronic: false,
  hasSmartCard: false,
  active: false,
  requiredDocuments: [],
  formTemplates: [],
};
