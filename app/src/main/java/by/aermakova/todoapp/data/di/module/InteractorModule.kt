package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.remote.FirebaseRealtimeDatabase
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.GoalRemoteModel
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import by.aermakova.todoapp.data.repository.GoalRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    fun provideGoalInteractor(
        goalRepository: GoalRepository,
        goalsRemoteDatabase: RemoteDatabase<GoalRemoteModel>,
        keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>
    ): GoalInteractor =
        GoalInteractor(goalRepository, goalsRemoteDatabase, keyResRemoteDatabase)

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