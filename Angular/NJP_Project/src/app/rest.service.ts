import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { HttpResponse } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';
import { Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
	providedIn: 'root'
})
export class RestService {

	constructor(private httpClient: HttpClient, private router: Router, private _sanitizer: DomSanitizer) { }

	getAuthorization() {
		return localStorage.getItem('authorization');
	}

	setAuthorization(value) {
		localStorage.setItem('authorization', value);
	}

	getUsername() {
		return localStorage.getItem('username');
	}

	setUsername(value) {
		localStorage.setItem('username', value);
	}


	getProfile() {
		return localStorage.getItem('profile');
	}

	setProfile(value) {
		localStorage.setItem('profile', value);
	}


	isLogedIn() {
		if (this.getAuthorization().startsWith("Basic")) {
			return true;
		} else {
			return false;
		}

	}

	logIn(username, password, _appComponent) {
		this.httpClient.post('http://hadziserver.ddns.net:8080/login', {
			"username": username,
			"password": password
		}, { observe: 'response' }).subscribe(resp => {
			console.log(resp.headers.get('Authorization'));
			this.setAuthorization(resp.headers.get('Authorization'));

			if (this.isLogedIn()) {
				_appComponent.linkName = "Logout";
				this.setUsername(username);
				this.router.navigateByUrl('/home');
			} else {
				_appComponent.linkName = "Login";
			}
		});
	}


	register(username, password, email, regComp) {
		this.httpClient.post('http://hadziserver.ddns.net:8080/users/registration', {
			"username": username,
			"password": password,
			"email": email
		}).subscribe(resp => {
			if (resp.toString().match("true")) {
				regComp.message = "Registration successfull. Please check email and verify account with link."
			} else {
				regComp.message = "Registration Failed"
			}
		});
	}

	getUserInfo(username) {
		let HDRS = {
			headers: new HttpHeaders({
				'Content-Type': 'application/json',
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/users/info/' + username, HDRS);
	}

	changeProfilePic(picture: string) {
		let HDRS = {
			headers: new HttpHeaders({
				'Content-Type': 'application/json',
				'Authorization': this.getAuthorization()
			})
		};


		return this.httpClient.post('http://hadziserver.ddns.net:8080/users/change/profile', {
			"data": picture
		}, HDRS);
	}

	changePassword(password) {
		let HDRS = {
			headers: new HttpHeaders({
				'Content-Type': 'application/json',
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.post('http://hadziserver.ddns.net:8080/users/change/password', {
			"password": password
		}, HDRS);

	}

	changeEmail(email) {
		let HDRS = {
			headers: new HttpHeaders({
				'Content-Type': 'application/json',
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.post('http://hadziserver.ddns.net:8080/users/change/email', {
			"email": email
		}, HDRS);

	}

	getAllFolowing() {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/users/getallfolowing', HDRS);
	}

	postDownload(user, postNum) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/post/download?username=' + user + '&postNum=' + postNum, HDRS);
	}

	getPostList(startAt: number) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/post/getPostList/' + startAt, HDRS);
	}


	comment(username, postNum, comment) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.post('http://hadziserver.ddns.net:8080/post/comment', {
			"username": username,
			"postNum": postNum,
			"comment": comment
		}, HDRS);

	}

	like(post) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/post/like?username=' + post.owner + '&postNum=' + post.postnum, HDRS);
	}


	dislike(post) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/post/dislike?username=' + post.owner + '&postNum=' + post.postnum, HDRS);
	}

	getAllUsers() {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/users/getAll', HDRS);
	}

	folow(user) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/users/folow/' + user, HDRS);
	}

	unfolow(user) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/users/unfolow/' + user, HDRS);
	}

	getMessages(user) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.get('http://hadziserver.ddns.net:8080/chat/get/' + user, HDRS);
	}

	sendMessage(user, message) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.post('http://hadziserver.ddns.net:8080/chat/send', {
			"receiver": user,
			"content": message
		}, HDRS);
	}

	upload(opis, data) {
		let HDRS = {
			headers: new HttpHeaders({
				'Authorization': this.getAuthorization()
			})
		};

		return this.httpClient.post('http://hadziserver.ddns.net:8080/post/upload', {
			"opis": opis,
			"data": data
		}, HDRS);
	}
















	convertFileToB64(file: File): Observable<Object> {

		let base64Observable = new ReplaySubject(1);

		let fileReader = new FileReader();
		fileReader.onload = event => {
			base64Observable.next((<string>(fileReader.result)).split("base64,")[1]);
		};
		fileReader.readAsDataURL(file);

		return base64Observable;
	}

	convertB64ToFilePath(B64String) {
		return this._sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + B64String);
	}


}
