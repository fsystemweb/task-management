import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { TaskStatus } from 'src/app/enums';
import { Task } from 'src/app/models';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit, OnChanges {
  @Input() tasks: Task[] = [];

  @Input() showButton: boolean = true;

  @Output() eventDelete = new EventEmitter<string>();
  @Output() eventCancel = new EventEmitter<string>();
  @Output() eventClickItem = new EventEmitter<Task>();

  constructor() {}

  public isRunning(task: Task) {
    if (!task.status) return;

    if (task.status.match(TaskStatus.RUNNING)) return true;

    return false;
  }

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.tasks.length == 0) return;
  }

  emitDelete(id: string) {
    this.eventDelete.emit(id);
  }

  emitClickOnItem(task: Task) {
    this.eventClickItem.emit(task);
  }

  emitCancel(id: string) {
    this.eventCancel.emit(id);
  }
}
