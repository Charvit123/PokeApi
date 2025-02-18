import { Location } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PokemonModel, PokemonUpdateModel } from '../../model/pokemon.model';
import { PokemonService } from '../services/pokemon.service';
import { Notification } from '../../model/notification.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PowerModel } from 'src/app/model/power.model';

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
  updatePokemonForm!: FormGroup;
  allPowers!: PowerModel[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private pokemonService: PokemonService,
    private modalService: NgbModal,
    private fb: FormBuilder
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
    this.pokemonService.getPowers().subscribe({
      next: (powers) => (this.allPowers = powers),
    });
  }

  initForm() {
    this.updatePokemonForm = this.fb.group({
      name: [this.pokemon.name, Validators.required],
      power: [this.pokemon.power.name, Validators.required],
      imageUrl: [this.pokemon.imageUrl, Validators.required],
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

  openModel(content: any) {
    this.initForm();
    this.modalService.open(content, { centered: true });
  }

  get name() {
    return this.updatePokemonForm.get('name');
  }

  get power() {
    return this.updatePokemonForm.get('power');
  }

  get image() {
    return this.updatePokemonForm.get('imageUrl');
  }

  updatePokemon() {
    const updatedValues = this.updatePokemonForm.value;
    const updatedPokemon: PokemonUpdateModel = {
      ...this.pokemon,
      name: updatedValues.name,
      power: updatedValues.power,
      imageUrl: updatedValues.imageUrl,
    };
    this.pokemonService.updatePokemon(updatedPokemon).subscribe({
      next: (response: PokemonModel) => {
        this.pokemon = response;
        this.pokemonService.sendNotification(
          new Notification('success', `Pokemon ${this.pokemon.name} updated`)
        );
        this.modalService.dismissAll();
      },
      error: (error) => {
        this.pokemonService.sendNotification(
          new Notification('error', error.message)
        );
      },
    });
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
