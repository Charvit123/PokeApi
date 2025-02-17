import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PokemonModel } from '../../model/pokemon.model';
import { PokemonService } from '../services/pokemon.service';
import { Notification } from '../../model/notification.model';

@Component({
  selector: 'app-pokemon-view',
  templateUrl: './pokemon-view.component.html',
  styleUrls: ['./pokemon-view.component.css'],
})
export class PokemonViewComponent implements OnInit {
  id: number;
  pokemon: PokemonModel;
  isNotFound: boolean = false;
  isNotificationOn: boolean = false;
  notification: Notification | null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private pokemonService: PokemonService
  ) {
    this.id = 0;
    this.pokemon = new PokemonModel();
    this.notification = null;
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    const handleNextResponse = (fetchedPokemons: PokemonModel[]) => {
      let temp = fetchedPokemons.find((pokemon) => pokemon.id == this.id);
      if (temp) {
        this.pokemon = temp;
      } else {
        this.showError();
      }
    };

    const handleErrorResponse = (error: any) => {
      console.log(error);
    };
    this.pokemonService.getPokemons().subscribe({
      next: handleNextResponse.bind(this),
      error: handleErrorResponse.bind(this),
    });
  }

  showError() {
    this.isNotFound = true;
    var message = 'The ID you are looking for was not found.';
    this.router.navigate(['/error'], { queryParams: { msg: message } });
  }

  hideError() {
    this.isNotFound = false;
  }

  updatePokemon() {
    this.location.back();
  }
  deletePokemon() {
    const handleNextResponse = () => {
      this.pokemonService.sendNotification(
        new Notification('success', `Pokemon ${this.pokemon?.name} Deleted`)
      );
      this.router.navigate(['/pokemon-list']);
    };

    const handleErrorResponse = (error: any) => {
      this.pokemonService.sendNotification(
        new Notification('error', error.message)
      );
    };

    if (this.pokemon?.id !== undefined) {
      this.pokemonService.deletePokemon(this.pokemon.id).subscribe({
        next: handleNextResponse.bind(this),
        error: handleErrorResponse.bind(this),
      });
    } else {
      console.error('Pokemon ID is undefined, cannot delete.');
    }
  }

  goBack() {
    this.location.back();
  }
}
