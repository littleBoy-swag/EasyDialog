package com.nqy.easydialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: EasyDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            val list = arrayListOf<String>("Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7")
            dialog = EasyDialog(this)
                .bindLayout(R.layout.easy_dialog_test)
                .bindViewJust(R.id.tv_1)
                .bindClick(R.id.btn_1, {
                    showToast("你好呀")
                }, true)
                .bindClick(R.id.btn_2, {
                    (dialog.getViewMap()[R.id.tv_1] as TextView).text = "修改后的文字，dialog不消失"
                })
                .bindClick(R.id.btn_3, {
                    if (!(dialog.getViewMap()[R.id.cb_1] as CheckBox).isChecked) {
                        showToast("先勾选checkbox才消失")
                        return@bindClick
                    }
                    dialog.dismiss()
                })
                .bindImage(R.id.img_1) {
//                    it.background =
//                        ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)
                    Glide.with(this)
                        .load("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2877684918,3107111380&fm=26&gp=0.jpg")
                        .into(it)
                }
                .bindCheckBox(R.id.cb_1) { _, c ->
                    if (c) {
                        showToast("选中了")
                    } else {
                        showToast("取消了")
                    }
                }
                .bindRecyclerView(R.id.rv, adapter = MyAdapter(list), f = { rv ->
                    rv.viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val lp = rv.layoutParams
                            rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val maxHeight = dip(150) //最大高度
                            if (rv.height < maxHeight) {
                                lp.height = rv.height
                            } else {
                                lp.height = maxHeight
                            }
                            rv.layoutParams = lp
                        }
                    })
                })
                .setCancelable(true)
                .setDialogStyle(8)
                .show()
        }

    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private inner class MyAdapter(private val list: ArrayList<String>) :
        RecyclerView.Adapter<ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                LayoutInflater.from(this@MainActivity).inflate(
                    R.layout.rv_item,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.tv.text = list[position]
        }

    }

    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.tv_rv_item)
    }

    private fun dip(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

}
