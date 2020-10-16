package by.aermakova.todoapp.data.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import io.reactivex.Observer


class FirebaseRealtimeDatabase<Type>(
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

    override fun addDataListener(dataObserver: Observer<Collection<Type>>) {
        val uid: String? = FirebaseAuthUtil.getUid()
        uid?.let {
            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<HashMap<String, Type>>()
                    Log.d("A_FirebaseRealtime", value.toString())
                    value?.let {
                        val model: Collection<Type> = it.values
                        dataObserver.onNext(model)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    print(error.message)
                }
            })
        }
    }
}