import { environment } from './../../../environments/environment';
import { Product } from './product.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class ProductService {

    baseUrl = `${environment.BASE_URL}`;

    constructor(
        private snackBar: MatSnackBar,
        private http: HttpClient
    ) { }

    showMessage(msg: string): void {
        this.snackBar.open(msg, 'X', {
            duration: 3000,
            horizontalPosition: "right",
            verticalPosition: "top"
        });
    }

    create(product: Product): Observable<Product> {
        return this.http.post<Product>(this.baseUrl, product);
    }

    read(): Observable<Array<Product>> {

        return this.http.get<Array<Product>>(this.baseUrl);
    }

    readById(id: string): Observable<Product> {
        const url = `${this.baseUrl}/${id}`;

        return this.http.get<Product>(url);
    }

    update(product: Product): Observable<Product> {
        const url = `${this.baseUrl}/${product.id}`;

        return this.http.put<Product>(url, product);
    }

    delete(id: string): Observable<Product> {
        const url = `${this.baseUrl}/${id}`;

        return this.http.delete<Product>(url);
    }

}
