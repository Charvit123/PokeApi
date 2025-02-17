import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { PowerModel } from 'src/app/model/power.model';
import { PokemonService } from 'src/app/pokemon/services/pokemon.service';
import { Notification } from '../../model/notification.model';
import { PokemonModel } from '../../model/pokemon.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-pokemon-list',
  templateUrl: './pokemon-list.component.html',
  styleUrls: ['./pokemon-list.component.css'],
  providers: [NgbModalConfig, NgbModal],
})
export class PokemonListComponent implements OnInit {
  addPokemonForm: FormGroup;
  allPokemons: PokemonModel[];
  allPowers: PowerModel[];
  pokemonToDisplay: PokemonModel[];
  isNotificationOn: boolean = false;
  notification: Notification | null;
  searchText = '';
  selectedPower: string;
  pokemonsSubscription: Subscription | undefined;

  page = 1;
  pageSize = 8;
  collectionSize = 0;

  constructor(
    private fb: FormBuilder,
    private pokemonService: PokemonService,
    config: NgbModalConfig,
    private modalService: NgbModal
  ) {
    this.addPokemonForm = fb.group({});
    this.allPokemons = [];
    this.allPowers = [];
    this.pokemonToDisplay = [];
    this.selectedPower = '';
    this.notification = null;
    config.backdrop = 'static';
    config.keyboard = false;
  }

  ngOnInit(): void {
    this.addPokemonForm = this.fb.group({
      name: this.fb.control('', [Validators.required]),
      power: this.fb.control('', [Validators.required]),
      imageUrl: this.fb.control('', [Validators.required]),
    });

    const handleFetchAllPokemons = (fetchedPokemons: PokemonModel[]) => {
      this.pokemonToDisplay = fetchedPokemons;
      this.allPokemons = fetchedPokemons;
      this.collectionSize = this.allPokemons.length;
      this.refreshPokemons();
    };

    const handleFetchAllPower = (fetchedPowers: PowerModel[]) => {
      this.allPowers = fetchedPowers;
    };

    this.pokemonService.getPokemons().subscribe({
      next: handleFetchAllPokemons.bind(this),
      error: (error) => {
        this.showNotification(new Notification('error', error.message));
      },
    });

    this.pokemonService.getPowers().subscribe({
      next: handleFetchAllPower.bind(this),
      error: (error) => {
        this.showNotification(new Notification('error', error.message));
      },
    });

    this.pokemonService.notification$.subscribe((notification) => {
      if (notification) {
        this.showNotification(notification);
      }
    });

    this.pokemonsSubscription = this.pokemonService.pokemons$.subscribe(
      (pokemons: PokemonModel[]) => {
        this.allPokemons = pokemons;
        this.collectionSize = pokemons.length;
        this.refreshPokemons();
      }
    );
  }

  openModel(content: any) {
    this.modalService.open(content);
  }

  refreshPokemons() {
    this.pokemonToDisplay = this.allPokemons
      .map((pokemon, i) => ({ id: i + 1, ...pokemon }))
      .slice(
        (this.page - 1) * this.pageSize,
        (this.page - 1) * this.pageSize + this.pageSize
      );
  }

  changePower(e: any) {
    this.Power.setValue(e.target.value, {
      onlySelf: true,
    });
  }

  public filterPokemon(event: any) {
    this.searchText = event.target.value.toLowerCase();
    this.pokemonToDisplay = this.allPokemons.filter((pokemon) => {
      return this.filterByPokemonDetails(pokemon);
    });
    if (event.target.value === '') {
      this.refreshPokemons();
    }
  }

  private filterByPokemonDetails(pokemon: PokemonModel): unknown {
    return (
      pokemon.name.toLowerCase().includes(this.searchText) ||
      pokemon.power.name.toLowerCase().includes(this.searchText) ||
      pokemon.id?.toString() === this.searchText
    );
  }

  public get Name(): FormControl {
    return this.addPokemonForm.get('name') as FormControl;
  }
  public get Image(): FormControl {
    return this.addPokemonForm.get('imageUrl') as FormControl;
  }
  public get Power(): FormControl {
    return this.addPokemonForm.get('power') as FormControl;
  }

  clearForm() {
    this.addPokemonForm.reset();
  }

  showNotification(notification: Notification) {
    this.isNotificationOn = true;
    this.notification = notification;
    setTimeout(() => {
      this.hideNotification();
    }, 3000);
  }

  hideNotification() {
    this.isNotificationOn = false;
    this.notification = null;
  }

  savePokemon() {
    let pokemon: PokemonModel = {
      name: this.Name.value,
      power: this.Power.value,
      imageUrl: this.Image.value,
    };

    const handleNextResponse = (savedPokemon: PokemonModel) => {
      this.allPokemons = this.allPokemons.concat([savedPokemon]);
      this.showNotification(
        new Notification(
          'success',
          `Pokemon ${savedPokemon.name} added successfully!`
        )
      );
      this.clearForm();
      this.modalService.dismissAll();
    };

    const handleErrorResponse = (error: any) => {
      this.showNotification(new Notification('error', error.message));
      this.clearForm();
    };

    this.pokemonService.savePokemon(pokemon).subscribe({
      next: handleNextResponse.bind(this),
      error: handleErrorResponse.bind(this),
    });
  }
}
