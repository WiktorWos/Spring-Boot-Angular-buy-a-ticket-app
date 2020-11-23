import {Component, HostListener} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular-client';
  isChangeToRegistrationClicked = false;
  isMobileSize = false;
  isLoginPage: boolean;

  constructor(private router: Router) {
    this.checkIfMobileSize();
  }

  onFormChangeClick() {
    if (this.isLoginPage) {
      this.router.navigate(['/register']);
    } else {
      this.router.navigate(['/login']);
    }
    this.isLoginPage = !this.isLoginPage;
  }

  onRouterOutletActivate(event: any) {
    const loginUrl = '/login';
    this.isLoginPage = window.location.pathname === loginUrl;
  }

  @HostListener('window:resize', ['$event'])
  checkIfMobileSize() {
    const mobileSize = 530;
    this.isMobileSize = window.innerWidth < mobileSize;
  }
}
