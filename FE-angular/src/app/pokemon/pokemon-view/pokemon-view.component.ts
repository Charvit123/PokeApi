import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PokemonModel } from '../../model/pokemon.model';
import { PokemonService } from '../services/pokemon.service';
import { ErrorPageComponent } from 'src/app/error-page/error-page.component';

@Component({
  selector: 'app-pokemon-view',
  templateUrl: './pokemon-view.component.html',
  styleUrls: ['./pokemon-view.component.css'],
})
export class PokemonViewComponent implements OnInit {
  id: number;
  pokemon: PokemonModel;
  isNotFound: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private pokemonService: PokemonService
  ) {
    this.id = 0;
    this.pokemon = new PokemonModel();
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

  goBack() {
    this.location.back();
  }
}
