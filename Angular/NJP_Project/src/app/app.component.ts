import { Component, OnInit } from '@angular/core';
import { RestService } from './rest.service';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

	title = 'NJP Instagram';
	linkName = "Login"


	constructor(private _restService: RestService) { }

	ngOnInit() {

		if (this.isLogedIn()) {
			this.linkName = "Logout";
		} else {
			this.linkName = "Login";
		}
	}

	isLogedIn() {
		return this._restService.isLogedIn();
	}
}
