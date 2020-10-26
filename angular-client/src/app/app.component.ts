import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular-client';
  isChangeToRegistrationClicked = false;
  isMobileSize = false;

  constructor() {
    this.getScreenSize();
  }

  onFormChangeClick() {
    this.isChangeToRegistrationClicked = !this.isChangeToRegistrationClicked;
  }
  @HostListener('window:resize', ['$event'])
  getScreenSize() {
    const mobileSize = 530;
    this.isMobileSize = window.innerWidth < mobileSize;
  }
}
