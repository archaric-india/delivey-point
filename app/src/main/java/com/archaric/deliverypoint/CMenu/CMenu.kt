package com.archaric.deliverypoint.CMenu

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.archaric.deliverypoint.R


class CMenu(val context: Context, val root: LinearLayout) {

    var items: List<CMenuItem> = arrayListOf(
        CMenuItem(R.string.menu_home, R.drawable.ic_baseline_home_24, false, null),
        CMenuItem(R.string.order_history, R.drawable.ic_invoice, false, null),
        CMenuItem(R.string.offers, R.drawable.ic_discount__2_, false, null),
        CMenuItem(R.string.share_app, R.drawable.ic_baseline_share_24, false, null),
        CMenuItem(
            R.string.contact_us, R.drawable.ic_contactus, true, arrayListOf(
                CMenuItem(R.string.customer_support, R.drawable.ic_baseline_home_24, false, null),
                CMenuItem(R.string.live_chat, R.drawable.ic_baseline_home_24, false, null),
            )
        ),
        CMenuItem(R.string.logout, R.drawable.ic_log_out, false, null),
    )

    init {

        items.forEach{
            it.draw(context, root)
        }
    }

    fun update(){
        items.forEach{
            it.update(context);
        }
    }
}

var CURRENT = R.string.menu_home;

class CMenuItem{
    val title: Int;
    val drawable: Int;
    val expandle: Boolean;
    var items: List<CMenuItem> ?= null;

    lateinit var view: TextView;

    constructor(title: Int, drawable: Int, expandle: Boolean, items: List<CMenuItem>?) {
        this.title = title
        this.drawable = drawable
        this.expandle = expandle
        this.items = items
    }

    fun draw(context: Context, root: LinearLayout){
        view = LayoutInflater.from(context).inflate(R.layout.item_cmenu, root as ViewGroup, false) as TextView

        view.setText(context.resources.getString(title))

        root.addView(view)

        view.setCompoundDrawablesWithIntrinsicBounds(
            drawable,
            0,
            (if (this.expandle) R.drawable.ic_baseline_keyboard_arrow_down_24 else 0),
            0
        );

        if(this.expandle){
            this.items?.forEach{
                it.view = LayoutInflater.from(context).inflate(
                    R.layout.item_cmenu,
                    root as ViewGroup,
                    false
                ) as TextView
                it.view.setText(context.resources.getString(it.title))

                var params = it.view.layoutParams as LinearLayout.LayoutParams;
                params.setMargins(150, 0, 100, 0)

                it.view.visibility = View.GONE
                root.addView(it.view)
                it.view.setOnClickListener{ v ->
                    if(!it.expandle){
                        CURRENT = it.title;
                        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("MENU"))

                    } else {

                    }
                }
            }
        }

        view.setOnClickListener{
            if(!expandle){
                CURRENT = title;
                LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("MENU"))
            } else {
                items?.forEach {
                    if(it.view.visibility == View.GONE)
                    it.view.visibility = View.VISIBLE
                    else
                        it.view.visibility = View.GONE
                }
            }
        }
        update(context);
    }

    fun update(context: Context){
        if(expandle){
            items?.forEach{
                it.update(context);
            }
        }
        if(CURRENT == this.title){
            view.background = context.getDrawable(R.drawable.menu_selected)
            view.setTextColor(context.resources.getColor(R.color.purple))
            tintViewDrawable(view, R.color.purple)
        } else {
            view.setTextColor(context.resources.getColor(R.color.black))
            view.background = context.getDrawable(R.drawable.menu_unselected)
            tintViewDrawable(view, R.color.black)
        }
    }
}

private fun tintViewDrawable(view: TextView, c: Int) {
    val drawables = view.compoundDrawables
    for (drawable in drawables) {
        drawable?.setColorFilter(
            view.context.getResources().getColor(c),
            PorterDuff.Mode.SRC_ATOP
        )
    }
}