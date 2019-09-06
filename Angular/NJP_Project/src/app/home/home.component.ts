import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  template: `
    <br><br><br><br><br>
    <div *ngIf=show>

    <div *ngFor="let post of postList">
    <b>By: </b>{{post.owner}}<br>
    <b>Description: </b>{{post.opis}}<br><br>
    <img [src]="post.path"><br><br>
    <b>Likes: </b>{{post.nummberOfLikes}}
    <button (click)="like(post)">{{getLikeTxt(post)}}</button><button (click)="share(post)">share</button><br>
    <b>Comments:</b> <br>
    <a *ngFor="let k of post.komentari">
    {{k.value}}<br>
    </a>
    <input #com type="text" placeholder="comment" name="com" required>
    <button (click)="comment(post.owner, post.postnum, com.value, post)">submit</button>

    <br><br><br><br><br>
    </div>
  `,
  styles: []
})

export class HomeComponent implements OnInit {

  startAt;
  amount;
  postList = Array();
  show = false;
  isSharing = false;

  constructor(private _restService: RestService, private router: Router) {
    window.onscroll = () => {
      let windowHeight = "innerHeight" in window ? window.innerHeight
        : document.documentElement.offsetHeight;
      let body = document.body, html = document.documentElement;
      let docHeight = Math.max(body.scrollHeight,
        body.offsetHeight, html.clientHeight,
        html.scrollHeight, html.offsetHeight);
      let windowBottom = windowHeight + window.pageYOffset;
      if (windowBottom >= docHeight) {
        this.next();
      }
    };
  }

  ngOnInit() {
    if (!this._restService.isLogedIn()) {
      this.router.navigateByUrl("/login");
    } else {
      this.startAt = 0;
      this.loadPosts();

    }
  }

  loadPosts() {
    this._restService.getPostList(this.startAt).subscribe(resp => {
      console.log(resp);

      this.postList = this.postList.concat(<[]>resp);
      for (let i = 0; i < this.postList.length; i++) {
        this.postList[i].path = this._restService.convertB64ToFilePath(this.postList[i].file);
      }

      this.amount = (<[]>resp).length;
      this.show = true;
    });
  }

  comment(username, postNum, comment, post) {
    if (comment === "") {
      return;
    }

    this._restService.comment(username, postNum, comment).subscribe(resp => {
      if (resp == true) {
        let k = { value: this._restService.getUsername() + ": " + comment }
        post.komentari.push(k);
      }
    });
  }

  next() {
    this.startAt += this.amount;

    this.loadPosts();
  }

  back() {
    this.startAt -= 5;

    if (this.startAt < 0) {
      this.startAt = 0;
    }

    this.loadPosts();
  }

  getLikeTxt(post) {
    if (post.liked == true) {
      return "dislike"
    } else {
      return "like";
    }

  }

  like(post) {
    if (post.liked == true) {
      this._restService.dislike(post).subscribe(resp => {
        if (resp == true) {
          post.liked = false;
          post.nummberOfLikes--;
        }
      });
    } else {
      this._restService.like(post).subscribe(resp => {
        if (resp == true) {
          post.liked = true;
          post.nummberOfLikes++;
        }
      });
    }
  }

  share(post) {
    if (this.isSharing == false) {
      this.isSharing = true;
      this._restService.upload("(shared from " + post.owner + ") " + post.opis, post.file).subscribe(resp => {
        console.log(resp);
        this.isSharing = false;
      });
    }
  }



}
