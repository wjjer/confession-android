package vip.ablog.confession.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import vip.ablog.confession.R;
import vip.ablog.confession.ui.model.ResourceItemBean;

public class HomeResAdapter extends BaseQuickAdapter<ResourceItemBean, BaseViewHolder> {

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public HomeResAdapter(List<ResourceItemBean> list) {
        super(R.layout.item_resource, list);
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull ResourceItemBean item) {
        helper.setText(R.id.tv_home_item_title, item.getTitle());
    }
}