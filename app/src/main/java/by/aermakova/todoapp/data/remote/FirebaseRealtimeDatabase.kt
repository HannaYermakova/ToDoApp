package by.aermakova.todoapp.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observer


abstract class FirebaseRealtimeDatabase<Type>(
    private val databaseReference: DatabaseReference
) : RemoteDatabase<Type> {

    override fun saveData(data: Type) {
        val uid: String? = FirebaseAuthUtil.getUid()
        uid?.let {
            databaseReference
                .child(uid)
                .push()
                .setValue(data)
        }
    }

    override fun addDataListener(dataObserver: Observer<List<Type>>) {
        val uid: String? = FirebaseAuthUtil.getUid()
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