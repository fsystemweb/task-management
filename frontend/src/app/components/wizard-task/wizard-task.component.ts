import {
  Component,
  OnInit,
  Output,
  Input,
  EventEmitter,
  ViewChild,
  OnDestroy,
  AfterViewInit,
  ChangeDetectorRef,
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { timer } from 'rxjs/internal/observable/timer';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { Subscription } from 'rxjs/internal/Subscription';
import {
  CHECK_PROGRESS_EVERY_SECONDS,
  COUNT_TASK_NAME,
  GENERATE_SAMPLE_PROJECT_NAME,
} from 'src/app/constants/constants';
import { TaskStatus, TaskType } from 'src/app/enums';
import { Task } from 'src/app/models';
import { TaskService } from 'src/app/services/task.service';

@Component({
  selector: 'app-wizard-task',
  templateUrl: './wizard-task.component.html',
  styleUrls: ['./wizard-task.component.scss'],
})
export class WizardTaskComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('stepper') private stepper?: MatStepper;
  @Input() task!: Task;
  @Output() eventCloseWizard = new EventEmitter();
  @Output() eventCancel = new EventEmitter<string>();

  selectionTask: Task[] = [];
  taskIsRunning = false;
  progressSubscription!: Subscription;
  progress: number = 0;

  form = new FormGroup({
    start: new FormControl('', Validators.required),
    end: new FormControl('', Validators.required),
  });

  constructor(
    private taskService: TaskService,
    private cdref: ChangeDetectorRef
  ) {}

  ngAfterViewInit() {
    this.setCorrectStep();
    this.cdref.detectChanges();
  }

  ngOnInit(): void {
    this.createSelectionTask();
  }

  setCorrectStep() {
    if (!this.stepper || !this.task) return;
    if (!this.task.id) {
      this.stepper.selectedIndex = 0;
      return;
    }

    if (this.task.status == TaskStatus.NEW) {
      this.stepper.selectedIndex = 2;

      return;
    }

    if (this.task.status == TaskStatus.COMPLETED) {
      this.stepper.selectedIndex = 3;
      return;
    }

    if (this.task.status == TaskStatus.RUNNING) {
      this.taskIsRunning = true;
      if (this.task.type == TaskType.COUNT_TASK) this.getProgress();
    }

    this.stepper.selectedIndex = 2;
  }

  createSelectionTask() {
    const generateProject = new Task();
    generateProject.type = TaskType.GENERATE_SAMPLE_PROJECT;
    generateProject.name = GENERATE_SAMPLE_PROJECT_NAME;
    this.selectionTask.push(generateProject);

    const taskCount = new Task();
    taskCount.type = TaskType.COUNT_TASK;
    taskCount.name = COUNT_TASK_NAME;

    this.selectionTask.push(taskCount);
  }

  close() {
    this.eventCloseWizard.emit();
  }

  goForward() {
    if (this.stepper) this.stepper.next();
  }

  setTypeToTask(task: Task) {
    if (!this.task) this.task = new Task();
    this.task.type = task.type;
    this.task.name = task.name;

    if (TaskType.GENERATE_SAMPLE_PROJECT == task.type) this.createTask();
    else this.goForward();
  }

  createTask() {
    if (this.task)
      this.taskService.createTask(this.task).subscribe((data) => {
        this.task = data;
        this.setCorrectStep();
      });
  }

  validateForm(): boolean {
    if (!this.form.valid) {
      for (let i in this.form.controls) {
        this.form.controls[i].markAsTouched();
      }
      return false;
    } else {
      return true;
    }
  }

  createCountTask() {
    if (!this.task) return;
    if (!this.validateForm()) return;

    const start = this.form.get('start');
    const end = this.form.get('end');

    if (!start || !end) return;
    this.task.start = start.value;
    this.task.end = end.value;

    this.createTask();
  }

  executeTask() {
    if (this.task)
      this.taskService.execute(this.task.id).subscribe(() => {
        if (this.task?.type == TaskType.COUNT_TASK) {
          this.taskIsRunning = true;
          this.task!.status = TaskStatus.RUNNING;
          this.getProgress();
        } else {
          this.task!.status = TaskStatus.COMPLETED;
          this.goForward();
        }
      });
  }

  emitCancel() {
    if (!this.task || !this.task.id) return;
    this.eventCancel.emit(this.task!.id);
    this.close();
  }

  isCountTask() {
    if (!this.task || this.task.type !== TaskType.COUNT_TASK) return false;

    return true;
  }

  getProgress() {
    if (!this.task) return;

    this.progressSubscription = timer(0, CHECK_PROGRESS_EVERY_SECONDS)
      .pipe(switchMap(() => this.taskService.getProgress(this.task!.id)))
      .subscribe((result) => {
        this.progress = result.status;
        if (this.progress == this.task.end) {
          this.task.status = TaskStatus.COMPLETED;
          this.goForward();
        }
      });
  }

  ngOnDestroy() {
    this.progressSubscription.unsubscribe();
  }
}
