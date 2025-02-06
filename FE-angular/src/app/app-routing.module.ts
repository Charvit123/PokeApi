import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  { path: '', title: 'PokeAPI', component: HomeComponent },
  {
    path: 'about',
    title: 'PokeAPI - About',
    loadChildren: () =>
      import('./about-us/about-us.module').then((a) => a.AboutUsModule),
  },
  {
    path: 'pokemon-list',
    loadChildren: () =>
      import('./pokemon/pokemon.module').then((p) => p.PokemonModule),
  },
  {
    path: 'error',
    title: 'PokeAPI - Error',
    loadChildren: () =>
      import('./error-page/error-page.module').then((e) => e.ErrorModule),
  },
  { path: '**', redirectTo: 'error' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
