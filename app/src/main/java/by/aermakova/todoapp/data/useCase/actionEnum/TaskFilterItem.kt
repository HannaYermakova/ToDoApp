package by.aermakova.todoapp.data.useCase.actionEnum

import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.TaskEntity
import java.util.*
import kotlin.Comparator

enum class TaskFilterItem(val listId: Int, val titleText: Int) {

    TODAY(R.string.filter_today, R.string.filter_today_title),
    TOMORROW(R.string.filter_tomorrow, R.string.filter_tomorrow_title),
    THIS_WEEK(R.string.filter_this_week, R.string.filter_this_week_title),
    NEXT_WEEK(R.string.filter_next_week, R.string.filter_next_week_title),
    LATER(R.string.filter_later, R.string.filter_later_title),
    SOMEDAY(R.string.filter_someday, R.string.filter_someday_title),
    DONE(R.string.filter_done, R.string.filter_done_title),
    ALL_TASKS(R.string.filter_all, R.string.filter_all_title)
}

fun TaskFilterItem.filterTasksList(task: TaskEntity): Boolean {
    val deadline = task.finishDate
    deadline?.let {
        val today = Calendar.getInstance()
        val finishDate = Calendar.getInstance()
        finishDate.timeInMillis = deadline

        return when (this) {
            TaskFilterItem.TODAY -> {
                !task.taskStatusDone && today.get(Calendar.DATE) == finishDate.get(Calendar.DATE)
            }
            TaskFilterItem.TOMORROW -> {
                !task.taskStatusDone && today.get(Calendar.DATE) + 1 == finishDate.get(Calendar.DATE)
            }
            TaskFilterItem.THIS_WEEK -> {
                !task.taskStatusDone && finishDate.get(Calendar.DATE) - today.get(Calendar.DATE) < 7
            }
            TaskFilterItem.NEXT_WEEK -> {
                !task.taskStatusDone &&
                        finishDate.get(Calendar.DATE) - today.get(Calendar.DATE) in 7..13
            }
            TaskFilterItem.SOMEDAY -> !task.taskStatusDone
            TaskFilterItem.DONE -> task.taskStatusDone
            TaskFilterItem.LATER -> {
                !task.taskStatusDone && finishDate.get(Calendar.DATE) - today.get(Calendar.DATE) > 13
            }
            else -> true
        }
    }

    return when (this) {
        TaskFilterItem.SOMEDAY -> task.finishDate == null
        TaskFilterItem.DONE -> task.taskStatusDone
        TaskFilterItem.ALL_TASKS -> true
        else -> false
    }
}
