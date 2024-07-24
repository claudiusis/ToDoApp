package com.example.todoapp.ui.appinfopage

import android.content.Context
import android.net.Uri
import com.example.todoapp.R
import com.example.todoapp.navigation.Router
import com.example.todoapp.navigation.RouterImpl
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction
import javax.inject.Inject

class NavigationDivActionHandler(
    private val router: Router
): DivActionHandler() {


    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {

        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME_SAMPLE && handleNavDivAction(url, view.view.context)) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }

    }


    private fun handleNavDivAction(action: Uri, context: Context) : Boolean {
        return when(action.host){
            "navigate" -> {
                router.navigate(R.id.action_appInfoFragment2_to_main_page_fragment)
                true
            }
            else -> false
        }
    }

    companion object {
        const val SCHEME_SAMPLE = "custom-action"
    }
}