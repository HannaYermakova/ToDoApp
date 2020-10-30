package by.aermakova.todoapp.data.remote

import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.remote.model.BaseRemoteModel
import com.google.firebase.database.*
import io.reactivex.Observer

private const val ID_FIELD = "id"

abstract class FirebaseRealtimeDatabase<Type : BaseRemoteModel>(
    private val databaseReference: DatabaseReference
) : RemoteDatabase<Type> {

    private val uid: String? = FirebaseAuthUtil.getUid()

    override fun saveData(data: Type) {
        uid?.let {
            databaseReference
                .child(uid)
                .push()
                .setValue(data)
        }
    }

    override fun updateData(data: Type) {
        val query = createQuery(data.id)
        query?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.setValue(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }
        })
    }

    override fun removeData(id: String) {
        val query = createQuery(id)
        query?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }
        })
    }

    private fun createQuery(id: String): Query? {
        return uid?.let {
            databaseReference
                .child(uid)
                .orderByChild(ID_FIELD)
                .equalTo(id)
        }
    }

    override fun addDataListener(dataObserver: Observer<List<Type>>) {
        uid?.let {
            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val model: List<Type> = convertDataSnapshotToList(snapshot.children)
                    dataObserver.onNext(model)
                }

                override fun onCancelled(error: DatabaseError) {
                    print(error.message)
                }
            })
        }
    }

    abstract fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<Type>
}