package com.miempresa.usocontrolesv1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.DecimalFormat

class StudentAverageActivity : AppCompatActivity() {
    
    // Campos de examenes teoricos
    private lateinit var txtTeorico1: EditText
    private lateinit var txtTeorico2: EditText
    private lateinit var txtTeorico3: EditText
    private lateinit var txtTeorico4: EditText
    
    // Campos de laboratorio
    private lateinit var txtLab1: EditText
    private lateinit var txtLab2: EditText
    private lateinit var txtLab3: EditText
    private lateinit var txtLab4: EditText
    private lateinit var txtLab5: EditText
    private lateinit var txtLab6: EditText
    private lateinit var txtLab7: EditText
    private lateinit var txtLab8: EditText
    
    // Campos de resultados
    private lateinit var lblPromedioTeorico: TextView
    private lateinit var lblPromedioLaboratorio: TextView
    private lateinit var lblPromedioFinal: TextView
    private lateinit var lblEstadoEstudiante: TextView
    
    // Seccion de sustitutorio
    private lateinit var layoutSustitutorio: LinearLayout
    private lateinit var txtSustitutorio: EditText
    private lateinit var btnRecalcular: Button
    
    // Botones principales
    private lateinit var btnCalcularPromedio: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnVolverPropinas: Button
    
    private val formatter = DecimalFormat("#0.00")
    private var notasTeoricas = mutableListOf<Double>()
    private var notasLaboratorio = mutableListOf<Double>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_average)
        
        inicializarVistas()
        configurarListeners()
    }
    
    private fun inicializarVistas() {
        // Exmenes teóricos
        txtTeorico1 = findViewById(R.id.txtTeorico1)
        txtTeorico2 = findViewById(R.id.txtTeorico2)
        txtTeorico3 = findViewById(R.id.txtTeorico3)
        txtTeorico4 = findViewById(R.id.txtTeorico4)
        
        // Laboratorios
        txtLab1 = findViewById(R.id.txtLab1)
        txtLab2 = findViewById(R.id.txtLab2)
        txtLab3 = findViewById(R.id.txtLab3)
        txtLab4 = findViewById(R.id.txtLab4)
        txtLab5 = findViewById(R.id.txtLab5)
        txtLab6 = findViewById(R.id.txtLab6)
        txtLab7 = findViewById(R.id.txtLab7)
        txtLab8 = findViewById(R.id.txtLab8)
        
        // Resultados
        lblPromedioTeorico = findViewById(R.id.lblPromedioTeorico)
        lblPromedioLaboratorio = findViewById(R.id.lblPromedioLaboratorio)
        lblPromedioFinal = findViewById(R.id.lblPromedioFinal)
        lblEstadoEstudiante = findViewById(R.id.lblEstadoEstudiante)
        
        // Sustitutorio
        layoutSustitutorio = findViewById(R.id.layoutSustitutorio)
        txtSustitutorio = findViewById(R.id.txtSustitutorio)
        btnRecalcular = findViewById(R.id.btnRecalcular)
        
        // Botones principales
        btnCalcularPromedio = findViewById(R.id.btnCalcularPromedio)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnVolverPropinas = findViewById(R.id.btnVolverPropinas)
    }
    
    private fun configurarListeners() {
        btnCalcularPromedio.setOnClickListener {
            calcularPromedio()
        }
        
        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
        
        btnRecalcular.setOnClickListener {
            recalcularConSustitutorio()
        }
        
        btnVolverPropinas.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun calcularPromedio() {
        try {
            // Obtener notas teóricas
            notasTeoricas.clear()
            val teorico1 = txtTeorico1.text.toString().toDoubleOrNull() ?: 0.0
            val teorico2 = txtTeorico2.text.toString().toDoubleOrNull() ?: 0.0
            val teorico3 = txtTeorico3.text.toString().toDoubleOrNull() ?: 0.0
            val teorico4 = txtTeorico4.text.toString().toDoubleOrNull() ?: 0.0
            
            // Validar notas teóricas
            if (!validarNota(teorico1) || !validarNota(teorico2) || 
                !validarNota(teorico3) || !validarNota(teorico4)) {
                mostrarError("Las notas teóricas deben estar entre 0 y 20")
                return
            }
            
            notasTeoricas.addAll(listOf(teorico1, teorico2, teorico3, teorico4))
            
            // Obtener notas de laboratorio
            notasLaboratorio.clear()
            val lab1 = txtLab1.text.toString().toDoubleOrNull() ?: 0.0
            val lab2 = txtLab2.text.toString().toDoubleOrNull() ?: 0.0
            val lab3 = txtLab3.text.toString().toDoubleOrNull() ?: 0.0
            val lab4 = txtLab4.text.toString().toDoubleOrNull() ?: 0.0
            val lab5 = txtLab5.text.toString().toDoubleOrNull() ?: 0.0
            val lab6 = txtLab6.text.toString().toDoubleOrNull() ?: 0.0
            val lab7 = txtLab7.text.toString().toDoubleOrNull() ?: 0.0
            val lab8 = txtLab8.text.toString().toDoubleOrNull() ?: 0.0
            
            // Validar notas de laboratorio
            val notasLab = listOf(lab1, lab2, lab3, lab4, lab5, lab6, lab7, lab8)
            if (notasLab.any { !validarNota(it) }) {
                mostrarError("Las notas de laboratorio deben estar entre 0 y 20")
                return
            }
            
            notasLaboratorio.addAll(notasLab)
            
            // Calcular promedios
            val promedioTeorico = calcularPromedioTeorico()
            val promedioLaboratorio = calcularPromedioLaboratorio()
            val promedioFinal = (promedioTeorico * 0.30) + (promedioLaboratorio * 0.70)
            
            // Mostrar resultados
            mostrarResultados(promedioTeorico, promedioLaboratorio, promedioFinal)
            
        } catch (e: Exception) {
            mostrarError("Error en el cálculo: ${e.message}")
        }
    }
    
    private fun validarNota(nota: Double): Boolean {
        return nota >= 0.0 && nota <= 20.0
    }
    
    private fun calcularPromedioTeorico(): Double {

        // Ordenar de mayor a menor y tomar los 3 mejores
        val mejores3 = notasTeoricas.sortedDescending().take(3)
        return mejores3.average()
    }
    
    private fun calcularPromedioLaboratorio(): Double {
        return notasLaboratorio.average()
    }
    
    private fun mostrarResultados(promedioTeorico: Double, promedioLaboratorio: Double, promedioFinal: Double) {
        lblPromedioTeorico.text = formatter.format(promedioTeorico)
        lblPromedioLaboratorio.text = formatter.format(promedioLaboratorio)
        lblPromedioFinal.text = formatter.format(promedioFinal)
        
        if (promedioFinal >= 10.5) {
            lblEstadoEstudiante.text = "ESTADO: APROBADO"
            lblEstadoEstudiante.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            layoutSustitutorio.visibility = View.GONE
        } else {
            lblEstadoEstudiante.text = "ESTADO: DESAPROBADO"
            lblEstadoEstudiante.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            layoutSustitutorio.visibility = View.VISIBLE
        }
    }
    
    private fun recalcularConSustitutorio() {
        try {
            val notaSustitutoria = txtSustitutorio.text.toString().toDoubleOrNull() ?: 0.0
            
            if (!validarNota(notaSustitutoria)) {
                mostrarError("La nota sustitutoria debe estar entre 0 y 20")
                return
            }
            
            // Combinar todas las notas (teóricas + laboratorio)

            val todasLasNotas = mutableListOf<Double>()
            todasLasNotas.addAll(notasTeoricas)
            todasLasNotas.addAll(notasLaboratorio)
            
            // Encontrar la nota más baja y reemplazarla

            val notaMasBaja = todasLasNotas.minOrNull() ?: 0.0
            val indiceNotaMasBaja = todasLasNotas.indexOf(notaMasBaja)
            
            if (indiceNotaMasBaja < 4) {
                // Es una nota teórica

                notasTeoricas[indiceNotaMasBaja] = notaSustitutoria
            } else {
                // Es una nota de laboratorio

                notasLaboratorio[indiceNotaMasBaja - 4] = notaSustitutoria
            }
            
            // Recalcular

            val promedioTeorico = calcularPromedioTeorico()
            val promedioLaboratorio = calcularPromedioLaboratorio()
            val promedioFinal = (promedioTeorico * 0.30) + (promedioLaboratorio * 0.70)
            
            mostrarResultados(promedioTeorico, promedioLaboratorio, promedioFinal)
            
        } catch (e: Exception) {
            mostrarError("Error en el recálculo: ${e.message}")
        }
    }
    
    private fun limpiarCampos() {
        // Limpiar exámenes teóricos

        txtTeorico1.setText("")
        txtTeorico2.setText("")
        txtTeorico3.setText("")
        txtTeorico4.setText("")
        
        // Limpiar laboratorios

        txtLab1.setText("")
        txtLab2.setText("")
        txtLab3.setText("")
        txtLab4.setText("")
        txtLab5.setText("")
        txtLab6.setText("")
        txtLab7.setText("")
        txtLab8.setText("")
        
        // Limpiar sustitutorio

        txtSustitutorio.setText("")
        
        // Limpiar resultados

        lblPromedioTeorico.text = "0.00"
        lblPromedioLaboratorio.text = "0.00"
        lblPromedioFinal.text = "0.00"
        lblEstadoEstudiante.text = "ESTADO: DESAPROBADO"
        lblEstadoEstudiante.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        
        // Ocultar sección de sustitutorio

        layoutSustitutorio.visibility = View.GONE
        
        // Limpiar listas

        notasTeoricas.clear()
        notasLaboratorio.clear()
    }
    
    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
