package vip.ablog.confession.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import vip.ablog.confession.R;
import vip.ablog.confession.ui.model.MarketItemBean;
import vip.ablog.confession.utils.GlideImgManager;

public class MarketItemAdapter extends BaseQuickAdapter<MarketItemBean, BaseViewHolder> {

    private Context context;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public MarketItemAdapter(List<MarketItemBean> list, Context context) {
        super(R.layout.item_fragment_market, list);
        this.context = context;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull MarketItemBean item) {
        helper.setText(R.id.tv_fragment_market_name, item.getName());
        helper.setText(R.id.tv_fragment_market_update, "更新："+item.getCreateTime());
        String s = item.getFree() == 1 ? "免费" : "会员免费";
        helper.setText(R.id.tv_fragment_market_free, "状态：" + s);
        GlideImgManager.glideLoader(context,item.getCover(),
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                (ImageView) helper.getView(R.id.iv_fragment_market_cover));
      /*  helper.getView(R.id.btn_fragment_module_del).setOnLongClickListener(v->{
            FileUtils.deleteFiles(new File(Constant.APP_BASE_DIR + Constant.BASE_DIR_TEMPLATE + File.separator + item.getName()));
            ToastUtils.showToast(context,"删除成功!",0);
            helper.setGone(helper.itemView.getId(),true);
            return true;
        });*/
    }




}