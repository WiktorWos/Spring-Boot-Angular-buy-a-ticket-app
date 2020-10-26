import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-hamburger-menu',
  templateUrl: './hamburger-menu.component.html',
  styleUrls: ['./hamburger-menu.component.scss']
})
export class HamburgerMenuComponent implements OnInit {
  isHamburgerActive = false;
  isNavigationActive = false;
  constructor() { }

  ngOnInit(): void {
  }
  toggleClass() {
    this.isHamburgerActive = !this.isHamburgerActive;
    this.isNavigationActive = !this.isNavigationActive;
  }
}

