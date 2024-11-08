import { PowerModel } from "./power.model";

export class PokemonModel {
    id?: number = 0;
    name: string = '';
    power: PowerModel = new PowerModel();
    imageId?: number = 0;
    imageUrl?: string = '';
}