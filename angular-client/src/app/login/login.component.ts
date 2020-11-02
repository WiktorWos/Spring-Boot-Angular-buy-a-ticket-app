import { Component, OnInit } from '@angular/core';
import {AuthService} from '../_services/auth.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService, private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    if (this.tokenStorageService.getToken()) {
      this.isLoggedIn = true;
    }
  }

  onSubmit(f: NgForm) {
    if (f.valid) {
      this.authService.login(this.form).subscribe(
        data => {
          this.tokenStorageService.saveToken(data.jwt);
          this.tokenStorageService.saveUser(data);

          this.isLoginFailed = false;
          this.isLoggedIn = true;

          this.reloadPage();
        },
        err => {
          this.errorMessage = err.message[0];
          this.isLoginFailed = true;
        }
      );
    }
  }

  reloadPage() {
    window.location.reload();
  }

}
