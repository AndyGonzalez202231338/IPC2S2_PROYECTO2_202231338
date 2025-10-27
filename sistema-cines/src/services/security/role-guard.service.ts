import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

    constructor(private router: Router) {

    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
        if (!this.userRoleInAllowedRoles(route.data['allowedRoles'])) {
            this.router.navigate(['/']);
            return false;
        }

        return true; 
    }

    userRoleInAllowedRoles(allowedRoles: string[]): boolean {
        let role = localStorage.getItem('role');
        return role != null && allowedRoles.includes(role);
    }
}