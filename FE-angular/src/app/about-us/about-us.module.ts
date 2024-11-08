import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AboutUsRouting } from './about-us-routing.module';
import { AboutComponent } from './about/about.component';



@NgModule({
  declarations: [
    AboutComponent
  ],
  imports: [
    CommonModule,
    AboutUsRouting
  ],
  exports: [

  ]
})
export class AboutUsModule { }
