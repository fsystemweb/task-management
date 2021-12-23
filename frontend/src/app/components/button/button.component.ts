import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { TaskStatus } from 'src/app/enums';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
})
export class ButtonComponent implements OnInit, OnChanges {
  @Input() type: TaskStatus = TaskStatus.NEW;
  @Input() title: string = '';
  class = '';
  constructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.type) return;
    if (!this.type) return;
    let string = this.type;

    if (string.toString().match(TaskStatus.RUNNING)) this.class = 'blue';
  }

  ngOnInit(): void {}
}
