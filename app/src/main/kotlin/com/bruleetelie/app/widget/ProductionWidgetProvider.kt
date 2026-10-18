package com.bruleetelie.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.bruleetelie.app.MainActivity
import com.bruleetelie.app.R
import com.bruleetelie.domain.ProductionOrder
import com.bruleetelie.domain.ProductionStatus
import com.bruleetelie.domain.SaleSnapshot

class ProductionWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { manager.updateAppWidget(it, views(context)) }
    }

    companion object {
        private const val PREFS = "production_widget"

        fun updateSnapshot(context: Context, orders: List<ProductionOrder>, sales: List<SaleSnapshot>) {
            val active = orders.filter { it.status != ProductionStatus.Completed && it.status != ProductionStatus.Cancelled }
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putInt("pending", active.size)
                .putString("next", active.firstOrNull()?.productName ?: "Nenhuma produção pendente")
                .putString("revenue", sales.fold(com.bruleetelie.domain.Money.Zero) { total, sale -> total + sale.salePrice }.format())
                .apply()
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, ProductionWidgetProvider::class.java)
            manager.getAppWidgetIds(component).forEach { manager.updateAppWidget(it, views(context)) }
        }

        private fun views(context: Context): RemoteViews {
            val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val pending = prefs.getInt("pending", 0)
            val next = prefs.getString("next", "Abra o app para sincronizar")
            val revenue = prefs.getString("revenue", "R$ 0,00")
            val launchIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            return RemoteViews(context.packageName, R.layout.production_widget).apply {
                setTextViewText(R.id.widget_pending, "$pending produção(ões) pendente(s)")
                setTextViewText(R.id.widget_next, next)
                setTextViewText(R.id.widget_revenue, "Faturamento registrado: $revenue")
                setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            }
        }
    }
}
