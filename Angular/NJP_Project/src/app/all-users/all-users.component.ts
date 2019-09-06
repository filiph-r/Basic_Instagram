import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-all-users',
  template: `
  <div *ngIf=show>
    <p *ngFor="let user of usersList">
      <button (click)="openProfile(user)">{{user}}</button><br><br>
    </p>
  </div>
  `,
  styles: []
})
export class AllUsersComponent implements OnInit {

  usersList = null;
  show = false;

  constructor(private _restService: RestService, private router: Router) { }

  ngOnInit() {
    if (!this._restService.isLogedIn()) {
      this._restService.setAuthorization("");
      this.router.navigateByUrl("/");
    } else {
         this._restService.getAllUsers().subscribe(resp => {
        this.usersList = resp;
        this.show = true;
      });
   
   } }


   openProfile(username){
     this._restService.setProfile(username);

     this.router.navigateByUrl("/user");

   }

}
