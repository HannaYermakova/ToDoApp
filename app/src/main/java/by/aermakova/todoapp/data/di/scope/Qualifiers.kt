package by.aermakova.todoapp.data.di.scope

import javax.inject.Qualifier


@Qualifier
annotation class AddIdeaUseCase

@Qualifier
annotation class AddTaskUseCase

@Qualifier
annotation class AddStepUseCase

@Qualifier
annotation class NavigationIdeas

@Qualifier
annotation class NavigationSteps

@Qualifier
annotation class NavigationTasks

@Qualifier
annotation class NavigationGoals

@Qualifier
annotation class NavigationConvertIdea

@Qualifier
annotation class DialogAddItem

@Qualifier
annotation class DialogConfirm

@Qualifier
annotation class DialogConvertIdea

@Qualifier
annotation class DialogSelectKeyResult

@Qualifier
annotation class DialogPickDate

@Qualifier
annotation class SortTaskMenu

@Qualifier
annotation class FilterTaskMenu

@Qualifier
annotation class TaskActionMenu

@Qualifier
annotation class ErrorWhileLoading

@Qualifier
annotation class ErrorStepIsDoneTask

@Qualifier
annotation class ErrorStepIsDoneIdea

@Qualifier
annotation class ErrorDeleteGoal

@Qualifier
annotation class ErrorDeleteStep

@Qualifier
annotation class ErrorDeleteTask

@Qualifier
annotation class ErrorDeleteIdea

@Qualifier
annotation class ErrorGoalIsDoneTask

@Qualifier
annotation class ErrorGoalIsDoneStep

@Qualifier
annotation class ErrorGoalIsDoneIdea

@Qualifier
annotation class ErrorAddKeyResult

@Qualifier
annotation class ErrorFindGoal

@Qualifier
annotation class ErrorFindStep

@Qualifier
annotation class ErrorChangeTitleGoal

@Qualifier
annotation class ErrorEditTask

@Qualifier
annotation class ErrorEditIdea

@Qualifier
annotation class ErrorEmptyField

@Qualifier
annotation class ErrorSetTaskFields

@Qualifier
annotation class TitleAddKeyResult

@Qualifier
annotation class TitleDialogAddItem

@Qualifier
annotation class TitleDialogDeleteGoal

@Qualifier
annotation class TitleDialogDeleteStep

@Qualifier
annotation class TitleDialogDeleteTask

@Qualifier
annotation class TitleDialogDeleteIdea

@Qualifier
annotation class TitleSelectKeyResult

@Qualifier
annotation class LoginFacebook

@Qualifier
annotation class LoginGoogle
