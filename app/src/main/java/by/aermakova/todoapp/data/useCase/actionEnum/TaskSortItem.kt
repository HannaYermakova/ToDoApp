package by.aermakova.todoapp.data.useCase.actionEnum

import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.TaskEntity


enum class TaskSortItem(val listId: Int) {
    BY_NAME(R.string.sort_by_name),
    BY_START_DATE(R.string.sort_by_start_date),
    BY_FINISH_DATE(R.string.sort_by_finish_date)
}

fun TaskSortItem.createTasksComparator(): Comparator<TaskEntity> {
    return when (this) {
        TaskSortItem.BY_NAME -> Comparator { one: TaskEntity, two: TaskEntity ->
            one.text.compareTo(
                two.text
            )
        }
        TaskSortItem.BY_START_DATE -> Comparator { one: TaskEntity, two: TaskEntity ->
            one.startTime.compareTo(two.startTime)
        }
        else -> Comparator { one: TaskEntity, two: TaskEntity ->
            val finishOne = one.finishDate ?: 0
            val finishTwo = two.finishDate ?: 0
            finishOne.compareTo(finishTwo)
        }
    }
}