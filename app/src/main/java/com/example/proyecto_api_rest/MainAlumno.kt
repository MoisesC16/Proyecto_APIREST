package com.example.proyecto_api_rest

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainAlumno : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlumnoAdapter
    private lateinit var api: AlumnoApi


    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_alumno)

        val fab_agregar: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab_agregar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", Credentials.basic(auth_username, auth_password))
                    .build()
                chain.proceed(request)
            }
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("http://200.33.51.36/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(AlumnoApi::class.java)

        cargarDatos(api)


        fab_agregar.setOnClickListener(View.OnClickListener {
            val i = Intent(getBaseContext(), CrearAlumnoActivity::class.java)
            i.putExtra("auth_username", auth_username)
            i.putExtra("auth_password", auth_password)
            startActivity(i)
        })
    }

    override fun onResume() {
        super.onResume()
        cargarDatos(api)
    }

    private fun cargarDatos(api: AlumnoApi) {
        val call = api.obtenerAlumnos()
        call.enqueue(object : Callback<List<Alumno>> {
            override fun onResponse(call: Call<List<Alumno>>, response: Response<List<Alumno>>) {
                if (response.isSuccessful) {
                    val alumnos = response.body()
                    if (alumnos != null) {
                        adapter = AlumnoAdapter(alumnos)
                        recyclerView.adapter = adapter

                        // Establecemos el escuchador de clics en el adaptador
                        adapter.setOnItemClickListener(object : AlumnoAdapter.OnItemClickListener {
                            override fun onItemClick(alumno: Alumno) {
                                val opciones = arrayOf("Modificar Alumno", "Eliminar Alumno")

                                AlertDialog.Builder(this@MainAlumno)
                                    .setTitle(alumno.nombre)
                                    .setItems(opciones) { dialog, index ->
                                        when (index) {
                                            0 -> Modificar(alumno)
                                            1 -> eliminarAlumno(alumno, api)
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener los alumnos: $error")
                    Toast.makeText(
                        this@MainAlumno,
                        "Error al obtener los alumnos 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Alumno>>, t: Throwable) {
                Log.e("API", "Error al obtener los alumnos: ${t.message}")
                Toast.makeText(
                    this@MainAlumno,
                    "Error al obtener los alumnos 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun Modificar(alumno: Alumno) {
        // Creamos un intent para ir a la actividad de actualización de alumnos
        val i = Intent(getBaseContext(), ActualizarAlumnoActivity::class.java)
        // Pasamos el ID del alumno seleccionado a la actividad de actualización
        i.putExtra("alumno_id", alumno.id)
        i.putExtra("nombre", alumno.nombre)
        i.putExtra("apellido", alumno.apellido)
        i.putExtra("edad", alumno.edad)
        // Iniciamos la actividad de actualización de alumnos
        startActivity(i)
    }

    private fun eliminarAlumno(alumno: Alumno, api: AlumnoApi) {
        val alumnoTMP = Alumno(alumno.id,"", "", -987)
        Log.e("API", "id : $alumno")
        val llamada = api.eliminarAlumno(alumno.id, alumnoTMP)
        llamada.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainAlumno, "Alumno eliminado", Toast.LENGTH_SHORT).show()
                    cargarDatos(api)
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al eliminar alumno : $error")
                    Toast.makeText(this@MainAlumno, "Error al eliminar alumno 1", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error al eliminar alumno : $t")
                Toast.makeText(this@MainAlumno, "Error al eliminar alumno 2", Toast.LENGTH_SHORT).show()
            }
        })
    }
}