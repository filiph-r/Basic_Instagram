import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-profile',
  template: `
    <br><br><br><br><br>
    <div *ngIf=show>
    <font size="20" color="black">{{name}}</font><br><br>

    <img [src]="imagePath"><br><br>

    <input #profile style="padding-left: 6em" type="file"
       id="profile" name="profile" 
       accept="image/png, image/jpeg"
       (change)="changeProfilePic($event.target.files)">

    <br><br><br>

    <font size="5" color="black">Folowers: {{folowers}}</font><br>
    <font size="5" color="black">Folowing: {{folowing}}</font><br>
    <font size="5" color="black">Nummber of pictures: {{picNum}}</font><br><br>

    <input #password type="password" placeholder="Enter New Password" name="pw" required>
    <button (click)="changePW(password.value)">change password</button><br>

    <input #email type="text" placeholder="Enter New E-Mail" name="email" required>
    <button style="padding-right: 2.4em" (click)="changeEmail(email.value)">change email</button><br><br>
    {{message}}
    </div>
  `,
  styles: []
})

export class ProfileComponent implements OnInit {

  name = "";
  imagePath = null;
  show = false;
  folowers = 0;
  folowing = 0;
  picNum = 0;

  message = "";

  constructor(private _restService: RestService, private router: Router) { }

  ngOnInit() {
    if (!this._restService.isLogedIn()) {
      this._restService.setAuthorization("");
      this.router.navigateByUrl("/");
    }
    else {
      this.name = this._restService.getUsername();
      this.refreshInfo();
    }

  }

  changePW(password) {
    if (password === "") {
      return;
    }

    this._restService.changePassword(password).subscribe(resp => {
      console.log("changePW: " + resp);
      if (resp == true) {
        this.message = "password successfuly changed";
      } else {
        this.message = "password change Error";
      }
    });
  }

  changeEmail(email) {
    if (email === "" || ((<string>email).search(/@/gi)) == -1) {
      return;
    }

    this._restService.changeEmail(email).subscribe(resp => {
      console.log("changeEmail: " + resp);
      if (resp == true) {
        this.message = "email successfuly changed";
      } else {
        this.message = "email change Error";
      }
    });
  }

  changeProfilePic(files: FileList) {
    this._restService.convertFileToB64(files.item(0)).subscribe(resp => {
      this._restService.changeProfilePic(resp.toString()).subscribe(res => {
        console.log("changeProfilePic: " + res);
        this.refreshInfo();
      });
    });

  }

  refreshInfo() {
    this._restService.getUserInfo(this.name).subscribe(data => {
      let inf = <info>data;

      this.imagePath = this._restService.convertB64ToFilePath(inf.profilePic);
      this.folowers = inf.folowers;
      this.folowing = inf.folowing;
      this.picNum = inf.brojSlika;
      this.show = true;
    });
  }


}
