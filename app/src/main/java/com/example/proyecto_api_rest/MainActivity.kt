package com.example.proyecto_api_rest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.opcion1) {
            Toast.makeText(this, "CRUD PROFESOR", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainProfesor::class.java)
            startActivity(intent)
        }

        if (id == R.id.opcion2) {
            Toast.makeText(this, "CRUD ALUMNO", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainAlumno::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}