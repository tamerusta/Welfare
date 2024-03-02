package com.works.solutionchallange2024



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.works.solutionchallange2024.manager.AppPref

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appPref = AppPref(this)

        if (appPref.getIsChecked() && appPref.getMail() != null && appPref.getPassword() != null) {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}