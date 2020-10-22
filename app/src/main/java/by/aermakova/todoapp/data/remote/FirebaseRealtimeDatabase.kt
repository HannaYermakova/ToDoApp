package by.aermakova.todoapp.data.remote

import by.aermakova.todoapp.data.remote.model.BaseRemoteModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observer


abstract class FirebaseRealtimeDatabase<Type : BaseRemoteModel>(
    private val databaseReference: DatabaseReference
) : RemoteDatabase<Type> {

    companion object {
        private const val ID_FIELD = "id"
    }

    private val uid: String? = FirebaseAuthUtil.getUid()

    override fun saveData(data: Type) {
        uid?.let {
            databaseReference
                .child(uid)
                .push()
                .setValue(data)
        }
    }

    override fun removeData(id: String) {
        val query = uid?.let {
            databaseReference
                .child(uid)
                .orderByChild(ID_FIELD)
                .equalTo(id)
        }

        query?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    appleSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }
        })
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