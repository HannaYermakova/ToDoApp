package by.aermakova.todoapp.data.remote

import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.remote.model.BaseRemoteModel
import com.google.firebase.database.*
import io.reactivex.Observer

class FirebaseRealtimeDatabase<Type : BaseRemoteModel>(
    private val databaseReference: DatabaseReference,
    private val valueType: Class<Type>
) : RemoteDatabase<Type> {

    companion object {
        private const val ID_FIELD = "id"
    }

    private val uid: String?
        get() = FirebaseAuthUtil.getUid()

    override fun saveData(data: Type) {
        uid?.let {
            databaseReference
                .child(it)
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

    override fun removeData(id: Long) {
        val query = createQuery(id.toString())
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
                .child(it)
                .orderByChild(ID_FIELD)
                .equalTo(id)
        }
    }

    override fun addDataListener(dataObserver: Observer<List<Type>>) {
        uid?.let {
            databaseReference.child(it).addValueEventListener(object : ValueEventListener {
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

    private fun convertDataSnapshotToList(iterable: Iterable<DataSnapshot>): List<Type> {
        val list = arrayListOf<Type>()
        for (snapshot in iterable) {
            val model = snapshot.getValue(valueType)
            model?.let { list.add(it) }
        }
        return list
    }
}