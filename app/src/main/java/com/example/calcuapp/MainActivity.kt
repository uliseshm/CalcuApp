package com.example.calcuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calcuapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    //Definiendo el ViewBinding
    private lateinit var binding: ActivityMainBinding
    //Definicion de la libreria para las operaciones
    private lateinit var expression: Expression

    //Variables auxiliares
    var lastNumeric = false
    var lastDot = false
    var stateError = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Implementando el binding para la relacion de las vistas
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onDigitClick(view: View) {

        if(stateError){

            binding.tvData.text = (view as Button).text
            stateError = false
        }else{
            //Se añade el digito presionado
            binding.tvData.append((view as Button).text)
        }

        lastNumeric = true
        //Se llama al metodo para actualizar el resultado
        //cada que se ingrese un digito
        onEqual()
    }


    fun onAllclearClick(view: View) {

        //Se vuelve al estado inicial
        binding.tvData.text = ""
        binding.tvResult.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
        binding.tvResult.visibility = View.GONE
    }


    fun onEqualClick(view: View) {

        //Se genera el resultado final
        //Primero se llama al metodo onEqual
        onEqual()

        //Y se imprime el resultado final en la parte superior
        binding.tvData.text = binding.tvResult.text.toString()
        binding.tvResult.text = ""
        binding.tvResult.visibility = View.GONE
    }


    fun onOperatorClick(view: View) {

        //Checamos si no hay error y el ultimo numero ingresado
        if(!stateError && lastNumeric){
            //se agrega el numero ingresado
            binding.tvData.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            //se llama al metodo para actualizar el resultado
            onEqual()
        }
    }


    fun onBackClick(view: View) {

        //Se elimina el ultimo caracter o digito ingresado
        binding.tvData.text = binding.tvData.text.toString().dropLast(1)

        try {
            val lastChar = binding.tvData.text.toString().last()

            //Se revisa si fue un digito o un operador
            if(lastChar.isDigit()){
                onEqual()
            }
        }catch (e: Exception){
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
            Log.e("Ultimo caracter error", e.toString())
        }
    }


    fun onClearClick(view: View) {

        //Se limpia la pantalla de entrada y se reestablece la variable
        binding.tvData.text = ""
        lastNumeric = false

    }

    //Funcion para evaluar la expresion que el usuario ingrese
    fun onEqual(){

        if(lastNumeric && !stateError){

            val input = binding.tvData.text.toString()

            //Enviamos la entrada del usuario como parametro
            expression = ExpressionBuilder(input).build()

            try {
                //La evaluacion de la expresion se guarda en una variable
                val result = expression.evaluate()

                //Se habilita la vista para el resultado
                binding.tvResult.visibility = View.VISIBLE
                //Se imprime el resultado en la pantalla
                binding.tvResult.text = result.toString()

            }catch (e:ArithmeticException){
                Log.e("Evaluar error", e.toString())
                binding.tvResult.text = "- ERROR -"
                stateError = true
                lastNumeric = false
            }
        }
    }
}