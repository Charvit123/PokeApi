import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PokemonListComponent } from './pokemon-list/pokemon-list.component';
import { PokemonViewComponent } from './pokemon-view/pokemon-view.component';

const routes: Routes = [
  {
    path: '', title: "PokeAPI - List",
    component: PokemonListComponent
  },
  {
    path: ':id', title: "PokeAPI - View",
    component: PokemonViewComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PokemonRoutingModule { }
