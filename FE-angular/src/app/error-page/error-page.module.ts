import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorPageComponent } from './error-page.component';
import { ErrorPageRouting } from './error-page-routing.module';

@NgModule({
  declarations: [ErrorPageComponent],
  imports: [CommonModule, ErrorPageRouting],
  exports: [],
})
export class ErrorModule {}
