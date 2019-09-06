import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

import { AppRoutingModule, routingComponents } from './app-routing.module';

import { RestService } from './rest.service';

import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './register/register.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { AllUsersComponent } from './all-users/all-users.component';
import { UserComponent } from './user/user.component';
import { PostComponent } from './post/post.component';




@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    RegisterComponent,
    PageNotFoundComponent,
    HomeComponent,
    ProfileComponent,
    AllUsersComponent,
    UserComponent,
    PostComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [RestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
