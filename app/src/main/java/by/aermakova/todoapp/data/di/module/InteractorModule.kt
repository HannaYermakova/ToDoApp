package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.di.scope.ApplicationScope
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.FirebaseRealtimeDatabase
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.*
import by.aermakova.todoapp.data.repository.GoalRepository
import by.aermakova.todoapp.data.repository.IdeaRepository
import by.aermakova.todoapp.data.repository.StepRepository
import by.aermakova.todoapp.data.repository.TaskRepository
import by.aermakova.todoapp.util.Status
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@Module
class InteractorModule {

    @ApplicationScope
    @Provides
    fun provideStateListener(): Subject<Status> = PublishSubject.create()

    @Provides
    fun provideTaskInteractor(
        taskRepository: TaskRepository,
        taskRemoteDatabase: RemoteDatabase<TaskRemoteModel>
    ): TaskInteractor = TaskInteractor(taskRepository, taskRemoteDatabase)

    @Provides
    fun provideIdeaInteractor(
        ideaRepository: IdeaRepository,
        ideaRemoteDatabase: RemoteDatabase<IdeaRemoteModel>
    ): IdeaInteractor = IdeaInteractor(ideaRepository, ideaRemoteDatabase)

    @Provides
    fun provideStepInteractor(
        stepRepository: StepRepository,
        stepRemoteDatabase: RemoteDatabase<StepRemoteModel>
    ): StepInteractor = StepInteractor(stepRepository, stepRemoteDatabase)

    @Provides
    fun provideKeyResultInteractor(
        goalRepository: GoalRepository,
        keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>
    ): KeyResultInteractor = KeyResultInteractor(goalRepository, keyResRemoteDatabase)

    @Provides
    fun provideGoalInteractor(
        goalRepository: GoalRepository,
        stepRepository: StepRepository,
        taskRepository: TaskRepository,
        goalsRemoteDatabase: RemoteDatabase<GoalRemoteModel>,
        keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>,
        stepRemoteDatabase: RemoteDatabase<StepRemoteModel>,
        taskRemoteDatabase: RemoteDatabase<TaskRemoteModel>
    ): GoalInteractor =
        GoalInteractor(
            goalRepository,
            stepRepository,
            taskRepository,
            goalsRemoteDatabase,
            keyResRemoteDatabase,
            stepRemoteDatabase,
            taskRemoteDatabase
        )

    @Provides
    fun provideRemoteIdeaDatabase(): RemoteDatabase<IdeaRemoteModel> =
        FirebaseRealtimeDatabase(
            Firebase.database.reference.child(IDEA_REMOTE_DATA_BASE),
            IdeaRemoteModel::class.java
        )

    @Provides
    fun provideRemoteStepDatabase(): RemoteDatabase<StepRemoteModel> =
        FirebaseRealtimeDatabase(
            Firebase.database.reference.child(STEP_REMOTE_DATA_BASE),
            StepRemoteModel::class.java
        )


    @Provides
    fun provideRemoteTaskDatabase(): RemoteDatabase<TaskRemoteModel> =
        FirebaseRealtimeDatabase(
            Firebase.database.reference.child(TASK_REMOTE_DATA_BASE),
            TaskRemoteModel::class.java
        )

    @Provides
    fun provideRemoteDataBase(): RemoteDatabase<GoalRemoteModel> =
        FirebaseRealtimeDatabase(
            Firebase.database.reference.child(GOALS_REMOTE_DATA_BASE),
            GoalRemoteModel::class.java
        )

    @Provides
    fun provideKeyResRemoteDataBase(): RemoteDatabase<KeyResultRemoteModel> =
        FirebaseRealtimeDatabase(
            Firebase.database.reference.child(KEY_RESULTS_REMOTE_DATA_BASE),
            KeyResultRemoteModel::class.java
        )
}

private const val GOALS_REMOTE_DATA_BASE = "goals_remote_data_base"
private const val KEY_RESULTS_REMOTE_DATA_BASE = "key_results_remote_data_base"
private const val TASK_REMOTE_DATA_BASE = "task_remote_data_base"
private const val STEP_REMOTE_DATA_BASE = "step_remote_data_base"
private const val IDEA_REMOTE_DATA_BASE = "idea_remote_data_base"