package by.aermakova.todoapp.util

import by.aermakova.todoapp.R

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