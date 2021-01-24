package com.skf.bingeflix.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.skf.bingeflix.Activity.PersonalInfo
import com.skf.bingeflix.Activity.SplashScreen
import com.skf.bingeflix.Activity.TermsActivity
import com.skf.bingeflix.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Account : Fragment() {
    private val username: TextView? = view?.findViewById(R.id.profileName)
    private var profilepic: CircleImageView? = null
    private var language: Button? = view?.findViewById(R.id.language_button)

    private var h: Boolean? = false
    private var e: Boolean? = false
    private var o: Boolean? = false
    private var firebaseAuth: FirebaseAuth? = null
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val tv = view.findViewById<TextView>(R.id.logout)
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth!!.currentUser

        val persInfo = view.findViewById<Button>(R.id.persInfo)
        val terms = view.findViewById<Button>(R.id.terms)
        profilepic = view.findViewById(R.id.profile_image)
        language?.setOnClickListener { showDialog() }
        persInfo.setOnClickListener { startActivity(Intent(activity, PersonalInfo::class.java)) }
        terms.setOnClickListener { startActivity(Intent(activity, TermsActivity::class.java)) }
        tv.setOnClickListener {
            firebaseAuth!!.signOut()
            startActivity(Intent(activity, SplashScreen::class.java))
        }
        val name = if (user != null) user.displayName else "User"
        val uid = Objects.requireNonNull(user)?.uid
        val documentReference = db.collection("Users").document("$name $uid")
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val Name = documentSnapshot.getString("Name")
                username?.setText("Hi, $Name")
                val tImage = documentSnapshot.getString("Profile")
                if (tImage != "") {
                    Picasso.get().load(tImage).into(profilepic)
                }
            } else {
                Toast.makeText(activity, "Document does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", e.toString())
        }
        return view
    }

    fun onCheckboxClicked(view: View) {
        // Is the view now checked?
        val checked = (view as CheckBox).isChecked
        when (view.getId()) {
            R.id.language_1 -> h = checked
            R.id.language_2 -> e = checked
            R.id.language_3 -> o = checked
        }
    }

    fun showDialog() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = if (user != null) user.displayName else "User"
        val uid = Objects.requireNonNull(user)?.uid
        val documentReference = FirebaseFirestore.getInstance().collection("Users").document("$name $uid")
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                h = documentSnapshot.getBoolean("Hindi")
                e = documentSnapshot.getBoolean("English")
                o = documentSnapshot.getBoolean("Others")
            } else {
                Toast.makeText(context, "Document does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", e.toString())
        }
        val builder = Dialog(Objects.requireNonNull(context)!!)
        builder.setCancelable(true)
        builder.setContentView(R.layout.dialog_choose_language)
        builder.show()
        val done: Button  = builder.findViewById(R.id.button_done)
        val c1: CheckBox = builder.findViewById(R.id.language_1)
        val c2: CheckBox = builder.findViewById(R.id.language_2)
        val c3: CheckBox = builder.findViewById(R.id.language_3)
        if (h!!) {
            c1.setChecked(true)
        }
        if (e!!) {
            c2.setChecked(true)
        }
        if (o!!) {
            c3.setChecked(true)
        }
        done.setOnClickListener {
            showStartDialog()
            builder.dismiss()
        }
    }

    private fun showStartDialog() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = if (user != null) user.displayName else "User"
        assert(user != null)
        val uid = user!!.uid
        val info: MutableMap<String, Any?> = HashMap()
        info["Hindi"] = h
        info["English"] = e
        info["Others"] = o
        val userid = user.uid
        val ref: DatabaseReference
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid)
        ref.setValue(info)
        db.collection("Users").document("$name $uid")[info] = SetOptions.merge()
    }
}