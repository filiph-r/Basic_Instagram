import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';

@Component({
  selector: 'app-register',
  template: `
    <br><br><br><br><br>
    <div>
    <label for="uname"><b style="padding-right: 1.8em">Username</b></label>
    <input #username type="text" placeholder="Enter Username" name="uname" required>

    <br>
    <label for="psw"><b style="padding-right: 1.8em">Password </b></label>
    <input #password type="password" placeholder="Enter Password" name="psw" required>

    <br>
    <label for="email"><b style="padding-right: 2.9em">E-mail </b></label>
    <input #email type="text" placeholder="Enter E-mail" name="email" required><br>

    <br>
    <b style="padding-right: 0.5em">
    <button (click)="register(username.value, password.value, email.value)">Register</button><br><br>

    {{message}}
  `,
  styles: []
})
export class RegisterComponent implements OnInit {

  message = "";

  constructor(private _restService: RestService) { }

  ngOnInit() {
  }

  register(username, password, email) {
    this.message = "please wait";
    this._restService.register(username, password, email, this);
  }

}
