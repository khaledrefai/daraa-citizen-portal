import { IServiceCategory } from 'app/shared/model/service-category.model';
import { EstimatedTimeUnit } from 'app/shared/model/enumerations/estimated-time-unit.model';

export interface ICitizenService {
  id?: number;
  code?: string;
  name?: string;
  description?: string;
  estimatedDuration?: number;
  estimatedDurationUnit?: keyof typeof EstimatedTimeUnit;
  requiresPhysicalPresence?: boolean;
  isElectronic?: boolean;
  hasSmartCard?: boolean;
  feesDescription?: string | null;
  active?: boolean;
  category?: IServiceCategory | null;
}

export const defaultValue: Readonly<ICitizenService> = {
  requiresPhysicalPresence: false,
  isElectronic: false,
  hasSmartCard: false,
  active: false,
};
