package com.example.todoapp.data.repository

import android.util.Log
import com.example.todoapp.core.Importance
import java.util.Date

class TodoItemsRepository {

    private val _toDoList : MutableList<TodoItem> = mutableListOf(
        TodoItem("0", "Первая строка", Importance.URGENT, deadLine = Date(), isCompleted =  false, creationDate =  Date()),
        TodoItem("1", "Много много много текстаМного много много текстаМного много много текстаМного много много текстаМного много много текста", Importance.NORMAL, isCompleted =  true, creationDate =  Date()),
        TodoItem("2", "Text", Importance.LOW, isCompleted =  true, creationDate =  Date()),
        TodoItem("3", "Text textText textText textText textText textText textText text", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("4", "Text", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("5", "1\n2\n3\n4\n5\n", Importance.URGENT, isCompleted =  false, creationDate =  Date()),
        TodoItem("6", "Купить что-то\nКупить что-то", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("7", "Text", Importance.NORMAL, isCompleted =  false, creationDate =  Date()),
        TodoItem("8", "Середина", Importance.LOW, isCompleted =  true,deadLine = Date(), creationDate =  Date()),
        TodoItem("9", "Text", Importance.NORMAL, isCompleted =  false, creationDate =  Date()),
        TodoItem("10", "Новая идея", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("11", "Посмотреть лекции", Importance.URGENT, isCompleted =  true,
            deadLine = Date(), creationDate =  Date()),
        TodoItem("12", "Text", Importance.NORMAL, isCompleted =  false, creationDate =  Date()),
        TodoItem("13", "Сходить в зал", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("14", "Integer felis diam, rhoncus pulvinar maximus ac, volutpat eu erat. Vestibulum gravida ac est eu commodo. Suspendisse nulla augue, aliquet non augue quis, mollis finibus massa. Nulla condimentum neque lorem, sed porta dui euismod tempus. Vestibulum tincidunt ligula nunc, lobortis tincidunt turpis sollicitudin nec. Suspendisse consectetur ullamcorper ex non tempor. Sed accumsan hendrerit est eu lobortis. Mauris fringilla, lorem et sollicitudin dapibus, tellus nisi consectetur neque, id porttitor neque eros at velit. Maecenas felis lectus, rhoncus auctor pretium pellentesque, hendrerit in eros. Cras quis ante ligula. Etiam fringilla a ante eu lacinia. Nam at vehicula purus. Curabitur id vulputate lorem. Nulla imperdiet dapibus sapien vitae eleifend. Vestibulum turpis tellus, consectetur ac ex vitae, sollicitudin maximus dolor. Pellentesque vitae", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("15", "Text", Importance.NORMAL, isCompleted =  false, creationDate =  Date()),
        TodoItem("16", "Text", Importance.LOW, isCompleted =  false, creationDate =  Date()),
        TodoItem("17", "Text", Importance.NORMAL, isCompleted =  false, creationDate =  Date()),
        TodoItem("18", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.  ullamcorper lorem.", Importance.URGENT, isCompleted =  false, creationDate =  Date()),
        TodoItem("19", "Последняя строка", Importance.NORMAL, isCompleted =  false, creationDate =  Date())
    )

    val toDoList : List<TodoItem> = _toDoList

    fun addTodoItem(item : TodoItem) {
        _toDoList.add(item)
    }

    fun deleteItem(id: String){
        _toDoList.remove(_toDoList.find { id==it.id })
    }

    fun updateItem(item: TodoItem){
        val index = _toDoList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _toDoList[index] = item
        }
        Log.d("QWERTY", _toDoList[index].toString())
    }

    fun getUnCompleted() : List<TodoItem> {
        return _toDoList.filter { !it.isCompleted }.toList()
    }

    fun getCompletedAmount() : Int {
        return _toDoList.count { it.isCompleted }
    }

}