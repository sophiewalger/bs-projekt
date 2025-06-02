export interface Kunde {
  id: string;
  name: string;
  adresse: string;
  email: string;
}

export interface Ablesung {
  id: string;
  kundeId: string;
  kundeName: string;
  zaehlerstand: number;
  datum: string;
  notiz?: string;
}

export type CreateKundeDTO = Omit<Kunde, 'id'>;
export type UpdateKundeDTO = Partial<CreateKundeDTO>;

export type CreateAblesungDTO = Omit<Ablesung, 'id' | 'kundeName'>;
export type UpdateAblesungDTO = Partial<CreateAblesungDTO>; 