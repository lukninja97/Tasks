package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)
    private var mTaskFilter = 0

    private val mList = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mList

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    fun list(taskFilter: Int) {
        mTaskFilter = taskFilter

        val listener = object : APIListener<List<TaskModel>> {
            override fun onSuceccess(model: List<TaskModel>) {
                mList.value = model
            }

            override fun onFailure(str: String) {
                mList.value = arrayListOf()
                mValidation.value = ValidationListener(str)
            }
        }

        if (mTaskFilter == TaskConstants.FILTER.ALL){
            mTaskRepository.all(listener)
        } else if (mTaskFilter == TaskConstants.FILTER.NEXT){
            mTaskRepository.nextWeek(listener)
        }else{
            mTaskRepository.overdue(listener)
        }
    }

    fun complete(id: Int) {
        updateStatus(id, true)
    }

    fun undo(id: Int) {
        updateStatus(id, false)
    }

    private fun updateStatus(id: Int, complete: Boolean) {
        mTaskRepository.updateStatus(id, complete, object : APIListener<Boolean> {
            override fun onSuceccess(model: Boolean) {
                list(mTaskFilter)
                mValidation.value = ValidationListener()
            }
            override fun onFailure(str: String) {
                mValidation.value = ValidationListener(str)
            }
        })
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuceccess(model: Boolean) {
                list(mTaskFilter)
                mValidation.value = ValidationListener()
            }
            override fun onFailure(str: String) {
                mValidation.value = ValidationListener(str)
            }
        })
    }
}