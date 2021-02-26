package vip.ablog.confession.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import vip.ablog.confession.R;
import vip.ablog.confession.global.Constant;
import vip.ablog.confession.ui.activity.ui.modules.ModuleDataActivity;
import vip.ablog.confession.ui.model.ModuleItemBean;
import vip.ablog.confession.utils.FileUtils;
import vip.ablog.confession.utils.ToastUtils;

public class ModuleItemAdapter extends BaseQuickAdapter<ModuleItemBean, BaseViewHolder> {

    private Context context;
    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public ModuleItemAdapter(List<ModuleItemBean> list, Context context) {
        super(R.layout.item_fragment_module, list);
        this.context = context;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull ModuleItemBean item) {
        helper.setText(R.id.tv_fragment_module_name, item.getName());
        ImageView view = (ImageView)helper.getView(R.id.iv_fragment_module_cover);
        view.setImageURI(Uri.fromFile(new File(item.getCover())));
        helper.getView(R.id.btn_fragment_module_mgr).setOnClickListener(v -> {
            Intent intent = new Intent(context, ModuleDataActivity.class);
            intent.putExtra(Constant.MODULE_URL, item.getModulePath());
            intent.putExtra(Constant.MODULE_NAME, item.getName());
            context.startActivity(intent);
        });
        helper.getView(R.id.btn_fragment_module_del).setOnLongClickListener(v->{
            FileUtils.deleteFiles(new File(Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + item.getName()));
            ToastUtils.showToast(context,"删除成功!",0);
            helper.setGone(helper.itemView.getId(),true);
            return true;
        });
    }


}