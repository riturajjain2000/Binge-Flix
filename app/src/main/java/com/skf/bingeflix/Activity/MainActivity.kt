package com.skf.bingeflix.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.common.internal.SignInButtonImpl
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.skf.bingeflix.R
import com.skf.bingeflix.fragment.Account
import com.skf.bingeflix.fragment.Explore
import java.util.*

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var hindi: CheckBox? = null
    private var english: CheckBox? = null
    private var other: CheckBox? = null
    private var h = false
    private var e = false
    private var o = false

    var selectedfragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            showDialog()
        }
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                Explore()).commit()
    }

    fun onCheckboxClicked(view: View) {
        val checked = (view as CheckBox).isChecked
        when (view.getId()) {
            R.id.language_1 -> h = checked
            R.id.language_2 -> e = checked
            R.id.language_3 -> o = checked
        }
    }

    fun showDialog() {
        val builder = Dialog(this@MainActivity)
        builder.setCancelable(false)
        builder.setContentView(R.layout.dialog_choose_language)
        builder.show()
        hindi = builder.findViewById(R.id.language_1)
        english = builder.findViewById(R.id.language_2)
        other = builder.findViewById(R.id.language_3)
        val done = builder.findViewById<Button>(R.id.button_done)
        done.setOnClickListener {
            showStartDialog()
            builder.dismiss()
        }
    }

    override fun onBackPressed() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes") { dialogInterface, i -> finishAffinity() }.show()
        }
    }

    private fun showStartDialog() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = if (user != null) user.displayName else "User"
        val email = if (user != null) user.email else "user@gmail.com"
        assert(user != null)
        val uid = user!!.uid
        val uri = user.photoUrl
        val link = uri?.toString() ?: ""
        val info: MutableMap<String, Any> = HashMap()
        info["Name"] = name ?: "User"
        info["Email"] = email ?: "user@email.com"
        info["Uid"] = uid
        info["Profile"] = link
        info["Gender"] = ""
        info["Date of Birth"] = ""
        info["Phone no "] = ""
        info["id"] = uid
        info["username"] = name ?: "User"
        info["imageURL"] = link
        info["status"] = "offline"
        info["Hindi"] = h
        info["English"] = e
        info["Others"] = o
        val userid = user.uid
        val ref: DatabaseReference
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid)
        ref.setValue(info)
        db.collection("Users").document("$name $uid")[info] = SetOptions.merge()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.account -> {
                @SuppressLint("CommitPrefEdits") val editor = getSharedPreferences("PREPS", MODE_PRIVATE).edit()
                editor.putString("Userid", Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)?.uid)
                editor.apply()
                selectedfragment = Account()
            }
            R.id.explore -> selectedfragment = Explore()
        }
        if (selectedfragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    selectedfragment!!).commit()
        }
        true
    }
}