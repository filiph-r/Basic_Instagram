import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  template: `
    <br><br><br><br><br>
    <div>

    <input #post style="padding-left: 6em" type="file"
       id="post" name="post" 
       accept="image/png, image/jpeg"
       (change)="setPost($event.target.files)">

    <br><br><br>

    <input #decription type="text" placeholder="Post Description" name="decription" required>
    <button (click)="upload(decription.value)">Upload</button><br><br>
    {{message}}
    </div>
  `,
  styles: []
})
export class PostComponent implements OnInit {

  post = "";
  message = "";

  constructor(private _restService: RestService, private router: Router) { }

  ngOnInit() {
    if (!this._restService.isLogedIn()) {
      this._restService.setAuthorization("");
      this.router.navigateByUrl("/");
    } else {

    }
  }


  upload(description) {
    if (description == "" || this.post == "") {
      return;
    } else {
      this._restService.upload(description, this.post).subscribe(resp => {
        if (resp == true) {
          this.message = "Post successfuly uploaded"
        }
      });
    }
  }

  setPost(files: FileList) {
    this._restService.convertFileToB64(files.item(0)).subscribe(resp => {
      this.post = resp.toString();
    });

  }


}
