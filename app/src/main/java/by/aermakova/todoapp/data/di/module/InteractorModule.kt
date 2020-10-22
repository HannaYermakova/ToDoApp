package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.remote.FirebaseRealtimeDatabase
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.*
import by.aermakova.todoapp.data.repository.GoalRepository
import by.aermakova.todoapp.data.repository.IdeaRepository
import by.aermakova.todoapp.data.repository.StepRepository
import by.aermakova.todoapp.data.repository.TaskRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

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
    fun provideGoalInteractor(
        goalRepository: GoalRepository,
        goalsRemoteDatabase: RemoteDatabase<GoalRemoteModel>,
        keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>
    ): GoalInteractor =
        GoalInteractor(goalRepository, goalsRemoteDatabase, keyResRemoteDatabase)

    @Provides
    fun provideRemoteIdeaDatabase(): RemoteDatabase<IdeaRemoteModel> {
        return object : FirebaseRealtimeDatabase<IdeaRemoteModel>(
            Firebase.database.reference.child(IDEA_REMOTE_DATA_BASE)
        ) {
            override fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<IdeaRemoteModel> {
                val list = arrayListOf<IdeaRemoteModel>()
                for (snapshot in iterable) {
                    val model = snapshot.getValue(IdeaRemoteModel::class.java)
                    model?.let { list.add(it) }
                }
                return list
            }
        }
    }

    @Provides
    fun provideRemoteStepDatabase(): RemoteDatabase<StepRemoteModel> {
        return object : FirebaseRealtimeDatabase<StepRemoteModel>(
            Firebase.database.reference.child(STEP_REMOTE_DATA_BASE)
        ) {
            override fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<StepRemoteModel> {
                val list = arrayListOf<StepRemoteModel>()
                for (snapshot in iterable) {
                    val model = snapshot.getValue(StepRemoteModel::class.java)
                    model?.let { list.add(it) }
                }
                return list
            }
        }
    }

    @Provides
    fun provideRemoteTaskDatabase(): RemoteDatabase<TaskRemoteModel> {
        return object : FirebaseRealtimeDatabase<TaskRemoteModel>(
            Firebase.database.reference.child(TASK_REMOTE_DATA_BASE)
        ) {
            override fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<TaskRemoteModel> {
                val list = arrayListOf<TaskRemoteModel>()
                for (snapshot in iterable) {
                    val model = snapshot.getValue(TaskRemoteModel::class.java)
                    model?.let { list.add(it) }
                }
                return list
            }
        }
    }

    @Provides
    fun provideRemoteDataBase(): RemoteDatabase<GoalRemoteModel> {
        return object : FirebaseRealtimeDatabase<GoalRemoteModel>(
            Firebase.database.reference.child(GOALS_REMOTE_DATA_BASE)
        ) {
            override fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<GoalRemoteModel> {
                val list = arrayListOf<GoalRemoteModel>()
                for (snapshot in iterable) {
                    val model = snapshot.getValue(GoalRemoteModel::class.java)
                    model?.let { list.add(it) }
                }
                return list
            }
        }
    }

    @Provides
    fun provideKeyResRemoteDataBase(): RemoteDatabase<KeyResultRemoteModel> {
        return object : FirebaseRealtimeDatabase<KeyResultRemoteModel>(
            Firebase.database.reference.child(KEY_RESULTS_REMOTE_DATA_BASE)
        ) {
            override fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<KeyResultRemoteModel> {
                val list = arrayListOf<KeyResultRemoteModel>()
                for (snapshot in iterable) {
                    val model = snapshot.getValue(KeyResultRemoteModel::class.java)
                    model?.let { list.add(it) }
                }
                return list
            }
        }
    }
}

private const val GOALS_REMOTE_DATA_BASE = "goals_remote_data_base"
private const val KEY_RESULTS_REMOTE_DATA_BASE = "key_results_remote_data_base"
private const val TASK_REMOTE_DATA_BASE = "task_remote_data_base"
private const val STEP_REMOTE_DATA_BASE = "step_remote_data_base"
private const val IDEA_REMOTE_DATA_BASE = "idea_remote_data_base"