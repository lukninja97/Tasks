package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorityList = MutableLiveData<List<PriorityModel>>()
    var priorities: LiveData<List<PriorityModel>> = mPriorityList

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    private val mTask = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = mTask

    fun listPriorities() {
        mPriorityList.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel) {
        if (task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSuceccess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }

            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSuceccess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }

            })
        }
    }

    fun load(id: Int) {
        mTaskRepository.load(id, object : APIListener<TaskModel> {
            override fun onSuceccess(model: TaskModel) {
                mTask.value = model
            }

            override fun onFailure(str: String) {
            }
        })
    }

}