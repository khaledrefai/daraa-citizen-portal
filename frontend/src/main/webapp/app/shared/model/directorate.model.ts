export interface IDirectorate {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  active?: boolean;
}

export const defaultValue: Readonly<IDirectorate> = {
  active: false,
};
