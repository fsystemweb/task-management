import { TaskStatus, TaskType } from '../enums';

export class Task {
  id!: string;
  name!: string;
  date!: Date;
  type!: TaskType;
  start!: number;
  end!: number;
  status!: TaskStatus;

  constructor() {}
}
