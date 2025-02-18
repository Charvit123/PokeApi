import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PowerModel } from 'src/app/model/power.model';
import { environment } from 'src/environments/environment';
import { PokemonModel, PokemonUpdateModel } from '../../model/pokemon.model';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Notification } from '../../model/notification.model';

@Injectable()
export class PokemonService {
  baseUrl = environment.baseUrl;
  pokemonUrl = this.baseUrl + 'pokemon/';
  powerUrl = this.baseUrl + 'power/';

  private pokemonsSubject = new BehaviorSubject<PokemonModel[]>([]); // BehaviorSubject
  pokemons$ = this.pokemonsSubject.asObservable();

  private notificationSubject = new BehaviorSubject<Notification | null>(null);
  notification$: Observable<Notification | null> =
    this.notificationSubject.asObservable();

  constructor(private http: HttpClient) {}

  sendNotification(notification: Notification) {
    this.notificationSubject.next(notification);
  }

  getPokemons(): Observable<PokemonModel[]> {
    return this.http.get<PokemonModel[]>(this.pokemonUrl).pipe(
      tap((pokemons) => {
        this.pokemonsSubject.next(pokemons);
      })
    );
  }

  savePokemon(pokemon: PokemonModel): Observable<PokemonModel> {
    return this.http.post<PokemonModel>(this.pokemonUrl, pokemon).pipe(
      tap((savedPokemon) => {
        const currentPokemons = this.pokemonsSubject.getValue();
        this.pokemonsSubject.next([...currentPokemons, savedPokemon]);
      })
    );
  }

  updatePokemon(pokemon: PokemonUpdateModel): Observable<PokemonModel> {
    return this.http.put<PokemonModel>(this.pokemonUrl, pokemon).pipe(
      tap((updatedPokemon) => {
        const currentPokemons = this.pokemonsSubject.getValue();
        const index = currentPokemons.findIndex(
          (p) => p.id === updatedPokemon.id
        );
        if (index !== -1) {
          currentPokemons[index] = updatedPokemon;
          this.pokemonsSubject.next([...currentPokemons]);
        }
      })
    );
  }

  deletePokemon(id: number): Observable<PokemonModel> {
    return this.http.delete<PokemonModel>(this.pokemonUrl + id).pipe(
      tap(() => {
        const currentPokemons = this.pokemonsSubject.getValue();
        const updatedPokemons = currentPokemons.filter((p) => p.id !== id);
        this.pokemonsSubject.next(updatedPokemons);
      })
    );
  }

  getPowers() {
    return this.http.get<PowerModel[]>(this.powerUrl);
  }
}
