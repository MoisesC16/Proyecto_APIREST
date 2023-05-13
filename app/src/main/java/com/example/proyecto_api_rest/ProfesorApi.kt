package com.example.proyecto_api_rest

import retrofit2.Call
import retrofit2.http.*

interface ProfesorApi {
    @GET("profesor.php")
    fun obtenerProfesores(): Call<List<Profesor>>

    @GET("profesor/{id}")
    fun obtenerProfesorPorId(@Path("id") id: Int): Call<Profesor>

    @POST("profesor")
    fun crearProfesor(@Body profesor: Profesor): Call<Profesor>

    @PUT("profesor/{id}")
    fun actualizarProfesor(@Path("id") id: Int, @Body profesor: Profesor): Call<Profesor>

    @PUT("profesor/{id}")
    fun eliminarProfesor(@Path("id") id: Int, @Body profesor: Profesor): Call<Void>
}