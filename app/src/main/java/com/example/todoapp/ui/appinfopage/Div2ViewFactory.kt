package com.example.todoapp.ui.appinfopage

import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div2.DivData
import org.json.JSONObject

class Div2ViewFactory(
    private val context: Div2Context,
    private val templateJson : JSONObject? = null
) {
   private val environment = DivParsingEnvironment(ParsingErrorLogger.ASSERT).apply {
       if (templateJson!=null) parseTemplates(templateJson)
   }

    fun createView(cardJson : JSONObject) : Div2View {
        val divData = DivData(environment, cardJson)
        return Div2View(context).apply {
            setData(divData, DivDataTag("div"))
        }
    }
}