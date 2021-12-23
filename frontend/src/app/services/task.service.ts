import { Injectable } from '@angular/core';
import { Task } from '../models';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { interval } from 'rxjs/internal/observable/interval';
import { StatusResponse } from '../models/status-response';

@Injectable({ providedIn: 'root' })
export class TaskService {
  constructor(private http: HttpClient) {}

  public createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(`${environment.api}tasks/`, task);
  }

  public update(task: Task): Observable<Task> {
    return this.http.put<Task>(`${environment.api}tasks/${task.id}`, task);
  }

  public delete(taskId: string) {
    return this.http.delete(`${environment.api}tasks/${taskId}`);
  }

  public execute(taskId: string) {
    return this.http.post(`${environment.api}tasks/${taskId}/execute`, null);
  }

  public cancel(taskId: string) {
    return this.http.post(`${environment.api}tasks/${taskId}/cancel`, null);
  }

  public getAll(): Observable<Task[]> {
    return this.http.get<Task[]>(`${environment.api}tasks/`);
  }

  public getTask(id: string): Observable<Task> {
    return this.http.get<Task>(`${environment.api}tasks/${id}`);
  }

  public getProgress(id: string): Observable<StatusResponse> {
    return this.http.get<StatusResponse>(
      `${environment.api}/tasks/${id}/status/`
    );
  }
}
