package com.nqy.easydialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 添加类注释
 *
 * @author PAN FEI
 * @since 2020/8/4 10:35
 */
class EasyDialog(private var context: Context) {

    private var display: Display

    private lateinit var dialog: Dialog
    private lateinit var view: View
    /**
     * key为控件的id，value为控件
     */
    private val viewMap = HashMap<Int, View>()

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = wm.defaultDisplay
    }

    /**
     * 绑定视图
     * @param id 视图id
     */
    fun bindLayout(id: Int): EasyDialog {
        view = LayoutInflater.from(context).inflate(id, null)
        dialog = Dialog(context, R.style.EasyDialogStyle)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false) // 默认不可取消
        dialog.setCancelable(false) // 默认不可取消
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val param = window?.attributes
        val point = Point()
        display.getSize(point)
        param?.width = (point.x * 0.75).toInt()
        return this
    }

    /**
     * 设置Gravity
     */
    private fun setGravity(gravity: Gravity): EasyDialog {
        return this
    }

    /**
     * 动态设置dialog的宽高，内部自动转成dp
     * @param width dialog的宽
     * @param height dialog的高
     */
    fun setLayoutRect(width: Int, height: Int): EasyDialog {
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val param = window?.attributes
        param?.width = dip(width)
        param?.height = dip(height)
        window?.attributes = param
        return this
    }

    /**
     * 只绑定视图
     * @param id View的id
     */
    fun bindViewJust(id: Int): EasyDialog {
        val item = view.findViewById<View>(id)
        viewMap[id] = item
        return this
    }

    /**
     * 绑定ImageView
     * @param id ImageView的id
     * @param f 设置ImageView的加载，调用方用自己的图片加载类进行加载
     */
    fun bindImage(id: Int, f: (v: ImageView) -> Unit = {}): EasyDialog {
        val item = view.findViewById<ImageView>(id)
        viewMap[id] = item
        f.invoke(item)
        return this
    }

    /**
     * 绑定RecyclerView
     * @param id RecyclerView的id
     * @param layoutManager RecyclerView.LayoutManager 默认为线性垂直布局
     * @param adapter RecyclerView.Adapter
     * @param f 执行一些对RecyclerView的额外操作，比如动态设置它的高度
     */
    fun bindRecyclerView(
        id: Int, layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
        f: (v: RecyclerView) -> Unit = {}
    ): EasyDialog {
        val rv = view.findViewById<RecyclerView>(id)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        f.invoke(rv)
        viewMap[id] = rv
        return this
    }

    /**
     * 绑定CheckBox
     * @param id CheckBox的id
     *
     */
    fun bindCheckBox(id: Int, f: (view: View, checked: Boolean) -> Unit): EasyDialog {
        val item = view.findViewById<CheckBox>(id)
        viewMap[id] = item
        item.setOnCheckedChangeListener { buttonView, isChecked ->
            f.invoke(buttonView, isChecked)
        }
        return this
    }

    /**
     * 绑定点击事件
     * @param id 控件的id
     * @param f 点击后的操作
     * @param autoDismiss 点击后是否自动消失 默认不消失
     *
     */
    fun bindClick(id: Int, f: () -> Unit = {}, autoDismiss: Boolean = false): EasyDialog {
        val item = view.findViewById<View>(id)
        viewMap[id] = item
        item.setOnClickListener {
            f.invoke()
            if (autoDismiss) {
                dialog.dismiss()
            }
        }
        return this
    }

    /**
     * 设置Dialog的样式，圆角半径和背景色
     * @param radius 圆角半径
     * @param backgroundColor 背景色 默认白色
     */
    fun setDialogStyle(radius: Int, @ColorRes backgroundColor: Int = android.R.color.white): EasyDialog {
        val gd = GradientDrawable()
        gd.setColor(ContextCompat.getColor(context, backgroundColor))
        gd.cornerRadius = dip(radius).toFloat()
        view.background = gd
        return this
    }

    /**
     * dialog是否可取消
     *
     * @param cancelable 是否可取消
     */
    fun setCancelable(cancelable: Boolean): EasyDialog {
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)
        return this
    }

    /**
     * 获取当前dialog所绑定的控件
     * @return HashMap
     */
    fun getViewMap(): HashMap<Int, View> {
        return viewMap
    }

    fun show(): EasyDialog {
        dialog.show()
        return this
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
            viewMap.clear()
        }
    }

    private fun dip(value: Int): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }

}