import { Component, OnInit } from '@angular/core';
import { TaskStatus } from 'src/app/enums';
import { Task } from 'src/app/models';
import { TaskService } from 'src/app/services/task.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  tasks: Task[] = [];
  flagShowList = true;
  selectedTask!: Task;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.taskService.getAll().subscribe((data) => {
      this.tasks = data;
    });
  }

  deleteTask(id: string) {
    this.taskService
      .delete(id)
      .subscribe(
        (data) => (this.tasks = this.tasks.filter((obj) => obj.id !== id))
      );
  }

  cancelTask(id: string) {
    this.taskService
      .cancel(id)
      .subscribe((data) => this.cancelTaskSetStatus(id));
  }

  private cancelTaskSetStatus(id: string) {
    let task = this.tasks.find((obj) => obj.id == id);
    if (task) task.status = TaskStatus.CANCELLED;
  }

  startWizard() {
    this.flagShowList = false;
  }

  closeWizard() {
    this.flagShowList = true;
    window.location.reload();
  }

  goToTask(task: Task) {
    this.taskService.getTask(task.id).subscribe((data) => {
      this.selectedTask = data;
      this.flagShowList = false;
    });
  }
}
