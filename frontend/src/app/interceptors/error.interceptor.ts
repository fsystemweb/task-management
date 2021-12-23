import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../services/notification.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private notificationService: NotificationService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((err: any) => {
        console.log(err);

        let msg = '';
        let code = '';

        if (err instanceof HttpErrorResponse) {
          msg = err.error.message;
          code = err.status.toString();
        } else {
          msg = err.message;
          code = err.status;
        }

        this.notificationService.show(msg);

        const error = msg || code;
        return throwError(error);
      })
    );
  }
}
