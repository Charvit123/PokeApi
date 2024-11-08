import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PowerModel } from 'src/app/model/power.model';
import { environment } from 'src/environments/environment';
import { PokemonModel } from '../../model/pokemon.model';

@Injectable()

export class PokemonService {
  baseUrl = environment.baseUrl
  pokemonUrl = this.baseUrl + "pokemon"
  powerUrl = this.baseUrl + "power"

  public pokemons: PokemonModel[] = [];

  constructor(private http: HttpClient) {
  }

  public setPokemons(pokemons: PokemonModel[]) {
    this.pokemons = pokemons
  }

  getPokemons() {
    return this.http.get<PokemonModel[]>(this.pokemonUrl)
  }

  savePokemon(pokemon: PokemonModel) {
    return this.http.post<PokemonModel>(this.pokemonUrl, pokemon)
  }

  getPowers() {
    return this.http.get<PowerModel[]>(this.powerUrl);
  }
}
