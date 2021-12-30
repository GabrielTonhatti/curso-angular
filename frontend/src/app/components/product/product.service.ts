import { map, catchError } from 'rxjs/operators';
import { environment } from './../../../environments/environment';
import { Product } from './product.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EMPTY, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class ProductService {

    baseUrl = `${environment.BASE_URL}`;

    constructor(
        private snackBar: MatSnackBar,
        private http: HttpClient
    ) { }

    showMessage(msg: string, isError: boolean = false): void {
        this.snackBar.open(msg, 'X', {
            duration: 3000,
            horizontalPosition: "right",
            verticalPosition: "top",
            panelClass: isError ? ['msg-error'] : ['msg-success'],
        });
    }

    create(product: Product): Observable<Product> {
        return this.http.post<Product>(this.baseUrl, product).pipe(
            map(obj => obj),
            catchError(e => this.errorHandler(e))
        );
    }

    read(): Observable<Array<Product>> {

        return this.http.get<Array<Product>>(this.baseUrl).pipe(
            map(obj => obj),
            catchError(e => this.errorHandler(e))
        );
    }

    readById(id: number): Observable<Product> {
        const url = `${this.baseUrl}/${id}`;

        return this.http.get<Product>(url).pipe(
            map(obj => obj),
            catchError(e => this.errorHandler(e))
        );
    }

    update(product: Product): Observable<Product> {
        const url = `${this.baseUrl}/${product.id}`;

        return this.http.put<Product>(url, product).pipe(
            map(obj => obj),
            catchError(e => this.errorHandler(e))
        );
    }

    delete(id: number): Observable<Product> {
        const url = `${this.baseUrl}/${id}`;

        return this.http.delete<Product>(url).pipe(
            map(obj => obj),
            catchError(e => this.errorHandler(e))
        );
    }

    errorHandler(e: any): Observable<any> {
        console.log(e);
        if (e.error.message) {
            this.showMessage(e.error.message, true);
        } else {
            this.showMessage("Ocorreu um erro ao se conectar com o servidor!", true);
        }

        return EMPTY;
    }

}
