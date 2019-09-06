import { Component, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';
import { RestService } from '../rest.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  template: `
    <br><br><br><br><br>
    <div>
    <label for="uname"><b style="padding-right: 1.8em">Username</b></label>
    <input #username type="text" placeholder="Enter Username" name="uname" required>

    <br>
    <label for="psw"><b style="padding-right: 1.8em">Password </b></label>
    <input #password type="password" placeholder="Enter Password" name="psw" required><br>

    <br>
    <b style="padding-right: 1em">
    <button (click)="login(username.value, password.value)">Login</button>
  `,
  styles: []
})
export class LoginComponent implements OnInit {

  constructor(private _restService: RestService, private _appComponent: AppComponent, private router: Router) { }

  ngOnInit() {
    this._appComponent.linkName = "Login";
    this._restService.setAuthorization("");
    this.router.navigateByUrl("/login");
  }


  login(username, password, email) {
    if (username === "" || password === "") {
      return;
    }

    this._restService.logIn(username, password, this._appComponent);
  }

}
