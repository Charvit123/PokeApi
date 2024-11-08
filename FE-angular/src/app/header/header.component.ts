import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  title: string = 'PokeAPI';

  constructor(private loaction: Location, private route: ActivatedRoute) { }

  searchPokemon(e: SubmitEvent) {
    e.preventDefault()
  }

}
