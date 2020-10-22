package ch.ost.rj.mge.drinktracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.ost.rj.mge.drinktracker.dao.DrinkTrackerDatabase
import ch.ost.rj.mge.drinktracker.entity.Person
import ch.ost.rj.mge.drinktracker.repositroy.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PersonRepository
    val user: Person?

    init {
        val personDao = DrinkTrackerDatabase.getDatabase(application, viewModelScope).personDao()
        repository = PersonRepository(personDao)
        user = repository.user
    }

    fun insert(user: Person) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}