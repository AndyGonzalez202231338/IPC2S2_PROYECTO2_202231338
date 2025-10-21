import { Routes } from '@angular/router';
import { LoginForm } from '../components/login/login-form/login-form';
import { Home } from '../components/Home/home/home';
import { UsersPage } from '../pages/users-page/users-page';


import { CinesPage } from '../pages/cines-page/cines-page';
import { createAccountComponent } from '../components/login/create-account-form/create-account-form';
import { UpdateUserPageComponent } from '../pages/update-user-page/update-user-page';
import { CreateUserPageComponent } from '../pages/create-user-page/create-user-page';

export const routes: Routes = [
    // Rutas públicas
    { 
        path: '', 
        redirectTo: 'login', 
        pathMatch: 'full' 
    },
    { 
        path: 'login', 
        component: LoginForm 
    },
    { 
        path: 'create-account', 
        component: createAccountComponent,
        data: { 
            isAdminMode: false,
            isEditMode: false 
        }
    },

    // Rutas de administración
    { 
        path: 'admin',
        children: [
            { 
                path: 'home', 
                component: Home 
            },
            { 
                path: 'users', 
                component: UsersPage 
            },
            { 
                path: 'users/create', 
                component: createAccountComponent,
                data: { 
                    isAdminMode: true,
                    isEditMode: false 
                }
            },
            { 
                path: 'users/update/:email', 
                component: UpdateUserPageComponent,
                data: { 
                    isAdminMode: true,
                    isEditMode: true 
                }
            },
            { 
                path: 'cines', 
                component: CinesPage 
            }
        ]
    },

    // Rutas legacy (mantener compatibilidad)
    { 
        path: 'home', 
        component: Home 
    },
    { 
        path: 'users', 
        component: UsersPage 
    },
    { 
        path: 'create-user', 
        component: CreateUserPageComponent 
    },
    { 
        path: 'users/update/:email', 
        component: UpdateUserPageComponent 
    },
    { 
        path: 'create-account-form', 
        redirectTo: 'create-account' 
    },
    { 
        path: 'admin/create-user', 
        redirectTo: 'admin/users/create' 
    },

    // Ruta comodín
    { 
        path: '**', 
        redirectTo: 'login' 
    }
];