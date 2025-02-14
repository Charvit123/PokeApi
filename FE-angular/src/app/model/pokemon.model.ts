import { PowerModel } from './power.model';

export class PokemonModel {
  id?: number = 0;
  name: string = '';
  power: PowerModel = new PowerModel();
  imageUrl?: string = '';
}
