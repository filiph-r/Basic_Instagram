import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { Router } from '@angular/router';
import { interval } from 'rxjs';

@Component({
  selector: 'app-user',
  template: `
    <br><br><br><br><br>
    <div *ngIf=show>
    <font size="20" color="black">{{profile}}</font><br><br>

    <img [src]="imagePath"><br><br>

    <br><br><br>

    <font size="5" color="black">Folowers: {{folowers}}</font><br>
    <font size="5" color="black">Folowing: {{folowing}}</font><br>
    <font size="5" color="black">Nummber of pictures: {{picNum}}</font><br><br>

    <button (click)="folow()">{{followBtnTxt()}}</button><br><br><br><br>

    <b>chat:</b>
    <p *ngFor="let msg of messages">
      {{msg}}<br>
    </p>
    <br>
    <input #mes type="text" placeholder="message" name="mes" required><button (click)="send(mes.value)">send</button><br>
    </div>
    <br><br><br><br>
  `,
  styles: []
})
export class UserComponent implements OnInit {

  profile = "";

  imagePath = null;
  show = false;
  folowers = 0;
  folowing = 0;
  picNum = 0;

  allFolowing = Array();
  isFolowing = false;

  messages = null;

  timer;

  constructor(private _restService: RestService, private router: Router) { }

  ngOnInit() {
    if (!this._restService.isLogedIn()) {
      this._restService.setAuthorization("");
      this.router.navigateByUrl("/");
    } else {
      this.profile = this._restService.getProfile();

      this._restService.getUserInfo(this.profile).subscribe(data => {
        let inf = <info>data;

        this.imagePath = this._restService.convertB64ToFilePath(inf.profilePic);
        this.folowers = inf.folowers;
        this.folowing = inf.folowing;
        this.picNum = inf.brojSlika;

        this._restService.getAllFolowing().subscribe(resp => {
          this.allFolowing = <[]>resp;

          for (let i = 0; i < this.allFolowing.length; i++) {
            if (this.profile == this.allFolowing[i]) {
              this.isFolowing = true;
              break;
            } else {
              this.isFolowing = false;
            }
          }

          this._restService.getMessages(this.profile).subscribe(resp => {
            this.messages = resp;
          });

          this.timer = interval(500).subscribe(x => {
            this._restService.getMessages(this.profile).subscribe(resp => {
              if ((<[]>resp).length > this.messages.length) {
                this.messages = resp;
                let body = document.body;
                window.scroll(0, body.scrollHeight);
              }

            })
          });

          this.show = true;
        });

      });


    }
  }

  ngOnDestroy() {
    this.timer.unsubscribe();
  }

  followBtnTxt() {
    if (this.isFolowing)
      return "unfolow";
    else
      return "folow";
  }

  folow() {
    if (this.isFolowing) {
      this._restService.unfolow(this.profile).subscribe(resp => {
        if (resp == true) {
          this.isFolowing = false;
          this.folowers--;
        }
      });
    } else {
      this._restService.folow(this.profile).subscribe(resp => {
        if (resp == true) {
          this.isFolowing = true;
          this.folowers++;
        }
      });
    }

  }

  send(mes) {
    if (mes == "") {
      return;
    }

    this._restService.sendMessage(this.profile, mes).subscribe(resp => {
      if (resp == true) {
        this._restService.getMessages(this.profile).subscribe(resp => {
          this.messages = resp;
          console.log(resp);
        });
      }
    });

  }

}
