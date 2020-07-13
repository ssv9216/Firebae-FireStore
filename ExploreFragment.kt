package com.droidproject.socialmedia.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.solver.widgets.Snapshot
import androidx.fragment.app.Fragment
import com.droidproject.socialmedia.R
import com.droidproject.socialmedia.models.Note
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*

class ExploreFragment : Fragment(R.layout.explore_fragment) {

    private lateinit var db: FirebaseFirestore
    private lateinit var ref: DocumentReference
    private var title: EditText? = null
    private lateinit var desc: EditText
    private lateinit var save: Button
    private lateinit var load: Button
    private lateinit var data: TextView
    private lateinit var update:Button
    private lateinit var deleteField :Button
    private lateinit var deleteDoc:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        ref = db.document("NoteBook/FirstOne")

        //another way
        //db.collection("NoteBook").document("FirstOne")

        title = v?.findViewById(R.id.etTitle)!!
        desc = v.findViewById<EditText>(R.id.etDesc)
        save = v.findViewById<Button>(R.id.btnSave)
        load = v.findViewById(R.id.btnLoad)
        data = v.findViewById(R.id.tvData)
        update = v.findViewById(R.id.btnUpdate)
        deleteField = v.findViewById(R.id.deleteField)
        deleteDoc = v.findViewById(R.id.deleteDocument)

        save.setOnClickListener(View.OnClickListener {
            addData()
        })
        load.setOnClickListener(View.OnClickListener {
            retrieveData()
        })

        update.setOnClickListener(View.OnClickListener {
            updateData()
        })

        deleteField.setOnClickListener(View.OnClickListener {
            deleteField()
        })

        deleteDoc.setOnClickListener(View.OnClickListener {
            deleteDoc()
        })

        return v
    }





    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        //offline
        //immediate

        ref.addSnapshotListener { snapShot, e ->
            if (e != null) {

                Log.d("TAG", e.toString())
                return@addSnapshotListener
            }

            if (snapShot != null && snapShot.exists()) {

                val note: Note? = snapShot.toObject(Note::class.java)

                data.text = "title: ${note?.title},\n description: ${note?.desc}"

            }else{
                data.text =""
            }
        }
    }



    @SuppressLint("SetTextI18n")
    private fun retrieveData() {
        //onclick load
        ref.get()
            .addOnSuccessListener(OnSuccessListener {
                if (it.exists()) {


                    val note: Note? = it.toObject(Note::class.java)

                    data.text = "title: ${note?.title},\n description: ${note?.desc}"


                }
            })
            .addOnFailureListener {
                OnFailureListener {
                    Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun addData() {
//        val docData = hashMapOf(
//            "title" to title.text.toString(),
//            "desc" to desc.text.toString()
//        )
//
      val note = Note(title = title?.text.toString(),desc = desc.text.toString())

        ref.set(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Note Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateData() {
//        val docData = hashMapOf(
//            "desc" to "adad"
//        )
//
//        //it doesn't affect title only updates description field
//        ref.set(docData, SetOptions.merge())

        //other similar way but here key as argument
        ref.update("desc","aafaf")


    }

    private fun deleteField(){
//        val docData = HashMap<Any, Any>()
//        docData["desc"] = FieldValue.delete()
//
//        ref.set(docData)

        //another way

        ref.update("desc",FieldValue.delete())
    }

    private fun deleteDoc(){
        ref.delete()
    }
}
