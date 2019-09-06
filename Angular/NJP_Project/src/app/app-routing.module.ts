import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { AllUsersComponent } from './all-users/all-users.component';
import { UserComponent } from './user/user.component';
import { PostComponent } from './post/post.component';

const routes: Routes = [
	{ path: '', redirectTo: '/login', pathMatch: 'full' },
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'home', component: HomeComponent },
	{ path: 'profile', component: ProfileComponent },
	{ path: 'allusers', component: AllUsersComponent },
	{ path: 'user', component: UserComponent },
	{ path: 'post', component: PostComponent },
	{ path: '**', component: PageNotFoundComponent }
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = [PageNotFoundComponent, LoginComponent, RegisterComponent, HomeComponent, ProfileComponent, AllUsersComponent, UserComponent, PostComponent]