package com.miempresa.usocontrolesv1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    
    private lateinit var txtMontoTotal: EditText
    private lateinit var spnPorcentajePropina: Spinner
    private lateinit var txtNumeroPersonas: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnIrPromedios: Button
    private lateinit var lblPropina: TextView
    private lateinit var lblTotalPagar: TextView
    private lateinit var lblPorPersona: TextView
    
    private val porcentajes = arrayOf(5, 10, 15, 20)
    private val formatter = DecimalFormat("#0.00")
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Inicializar las vistas
        inicializarVistas()
        
        // Configurar spinner
        configurarSpinner()
        
        // Configurar listeners
        configurarListeners()
    }
    
    private fun inicializarVistas() {
        txtMontoTotal = findViewById(R.id.txtMontoTotal)
        spnPorcentajePropina = findViewById(R.id.spnPorcentajePropina)
        txtNumeroPersonas = findViewById(R.id.txtNumeroPersonas)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnIrPromedios = findViewById(R.id.btnIrPromedios)
        lblPropina = findViewById(R.id.lblPropina)
        lblTotalPagar = findViewById(R.id.lblTotalPagar)
        lblPorPersona = findViewById(R.id.lblPorPersona)
    }
    
    private fun configurarSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.porcentajes_propina,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnPorcentajePropina.adapter = adapter
    }
    
    private fun configurarListeners() {
        btnCalcular.setOnClickListener {
            calcularPropina()
        }
        
        btnIrPromedios.setOnClickListener {
            val intent = Intent(this, StudentAverageActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun calcularPropina() {
        try {
            // Obtener valores de entrada
            val montoTotal = txtMontoTotal.text.toString().toDoubleOrNull() ?: 0.0
            val numeroPersonas = txtNumeroPersonas.text.toString().toIntOrNull() ?: 1
            val porcentajeSeleccionado = porcentajes[spnPorcentajePropina.selectedItemPosition]
            
            // Validar entrada
            if (montoTotal <= 0) {
                mostrarError("El monto total debe ser mayor a 0")
                return
            }
            
            if (numeroPersonas <= 0) {
                mostrarError("El número de personas debe ser mayor a 0")
                return
            }
            
            // Calcular propina
            val propina = montoTotal * (porcentajeSeleccionado / 100.0)
            val totalPagar = montoTotal + propina
            val porPersona = totalPagar / numeroPersonas
            
            // Mostrar resultados
            lblPropina.text = "S/${formatter.format(propina)}"
            lblTotalPagar.text = "S/${formatter.format(totalPagar)}"
            lblPorPersona.text = "S/${formatter.format(porPersona)}"
            
        } catch (e: Exception) {
            mostrarError("Error en el cálculo: ${e.message}")
        }
    }
    
    private fun mostrarError(mensaje: String) {
        lblPropina.text = "Error"
        lblTotalPagar.text = "Error"
        lblPorPersona.text = "Error"
        
        // Mostrar toast o alerta
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
}