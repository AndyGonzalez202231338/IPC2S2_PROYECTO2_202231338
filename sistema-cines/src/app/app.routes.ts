import { Routes } from '@angular/router';
import { LoginForm } from '../components/login/login-form/login-form';
import { CreateAccountForm } from '../components/login/create-account-form/create-account-form';

export const routes: Routes = [
    { 
        path: '', 
        component: LoginForm 
    },
    { 
        path: 'login', 
        component: LoginForm 
    },
    { 
        path: 'create-account-form', 
        component: CreateAccountForm,
        data: { isAdminMode: false } // ✅ Esto pasa el parámetro a través de la ruta
    },
    // Si tienes una ruta para admin que use el mismo componente:
    { 
        path: 'admin/create-user', 
        component: CreateAccountForm,
        data: { isAdminMode: true } // ✅ Modo administrador
    },
    { 
        path: '**', 
        redirectTo: 'login' 
    }
];