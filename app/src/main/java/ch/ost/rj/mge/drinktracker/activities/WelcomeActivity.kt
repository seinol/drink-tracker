package ch.ost.rj.mge.drinktracker.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ch.ost.rj.mge.drinktracker.R
import ch.ost.rj.mge.drinktracker.entity.Gender
import ch.ost.rj.mge.drinktracker.entity.Person
import ch.ost.rj.mge.drinktracker.services.InputVerificationService
import ch.ost.rj.mge.drinktracker.viewmodel.WelcomeViewModel

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val FULL_VISIBLE_ALPHA = 1.0f
        const val HALF_VISIBLE_ALPHA = 0.5f
    }

    private var nameEditText: EditText? = null
    private var weightEditText: EditText? = null
    private var maleRadioButton: RadioButton? = null
    private var femaleRadioButton: RadioButton? = null
    private var selectedGender: Gender? = null

    private var loginButton: View? = null

    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeViewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        // TODO (priority low): outsource to a splash screen
        if (welcomeViewModel.user != null) {
            showHistoryActivity()
        }

        nameEditText = findViewById(R.id.welcome_name_input)
        nameEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateLoginButton()
            }
        })

        weightEditText = findViewById(R.id.welcome_weight_picker)
        weightEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateLoginButton()
            }
        })

        maleRadioButton = findViewById(R.id.welcome_gender_male)
        maleRadioButton?.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            selectedGender = Gender.MALE
            updateLoginButton()
        }
        femaleRadioButton = findViewById(R.id.welcome_gender_female)
        femaleRadioButton?.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            selectedGender = Gender.FEMALE
            updateLoginButton()
        }

        loginButton = findViewById(R.id.welcome_confirm_button)
        loginButton?.setOnClickListener { showHistoryActivity() }

        updateLoginButton()
    }

    private fun updateLoginButton() {
        val nameEditTextIsValid: Boolean = InputVerificationService.hasValidInput(nameEditText)
        val weightEditTextIsValid: Boolean =
            InputVerificationService.hasValidNumberInput(weightEditText)
        val genderRadioButtonIsValid: Boolean =
            InputVerificationService.hasValidInput(selectedGender)

        val buttonIsEnabled =
            nameEditTextIsValid && weightEditTextIsValid && genderRadioButtonIsValid
        val buttonAlpha: Float = if (buttonIsEnabled) FULL_VISIBLE_ALPHA else HALF_VISIBLE_ALPHA
        loginButton?.isEnabled = buttonIsEnabled
        loginButton?.alpha = buttonAlpha
    }

    private fun showHistoryActivity() {
        if (welcomeViewModel.user == null) {
            val name: String = nameEditText?.text.toString()
            val weight: Int = weightEditText?.text.toString().toInt()
            val gender: Gender = selectedGender!!
            val person = Person(name, weight, gender)
            welcomeViewModel.insert(person)
        }

        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}