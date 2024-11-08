import { Injectable, OnInit } from '@angular/core';
import { PokemonModel } from '../../model/pokemon.model';
import { PokemonService } from './pokemon.service';

@Injectable({
  providedIn: 'root'
})
export class SharedServiceService implements OnInit {
  allPokemons: PokemonModel[];
  constructor(private pokemonService: PokemonService) {
    this.allPokemons = [];
  }

  ngOnInit() {
    const handleFetchAllPokemons = (fetchedPokemons: PokemonModel[]) => {
      this.allPokemons = fetchedPokemons
    }

    this.pokemonService.getPokemons().subscribe({
      next: handleFetchAllPokemons.bind(this),
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}
