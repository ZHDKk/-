package com.example.sumec.wash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sumec.wash.R;
import com.example.sumec.wash.model.DailyForecastBean;
import com.example.sumec.wash.utils.UiUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private List<DailyForecastBean> lists;
    private LayoutInflater inflater;

    public WeatherAdapter(Context context, List<DailyForecastBean> datas) {
        context = context;
        lists = datas;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(R.layout.weather_item, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        String time = lists.get(position+1).getDate();
        Date date = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tvWeek, UiUtils.DateToWeek(date));
        String condCode = lists.get(position+1).getCond_code_d();
//        String pa = context.getPackageName();
//         int imgId = context.getResources().getIdentifier("cond_"+condCode, "id", context.getPackageName());
        if (condCode.equals("100")) {
            holder.setImg(R.id.condImg, R.drawable.cond_100);
        } else if (condCode.equals("101")) {
            holder.setImg(R.id.condImg, R.drawable.cond_101);
        } else if (condCode.equals("102")) {
            holder.setImg(R.id.condImg, R.drawable.cond_102);
        } else if (condCode.equals("103")) {
            holder.setImg(R.id.condImg, R.drawable.cond_103);
        } else if (condCode.equals("104")) {
            holder.setImg(R.id.condImg, R.drawable.cond_104);
        } else if (condCode.equals("200")) {
            holder.setImg(R.id.condImg, R.drawable.cond_200);
        } else if (condCode.equals("201")) {
            holder.setImg(R.id.condImg, R.drawable.cond_201);
        } else if (condCode.equals("202")) {
            holder.setImg(R.id.condImg, R.drawable.cond_202);
        } else if (condCode.equals("203")) {
            holder.setImg(R.id.condImg, R.drawable.cond_203);
        } else if (condCode.equals("204")) {
            holder.setImg(R.id.condImg, R.drawable.cond_204);
        } else if (condCode.equals("205")) {
            holder.setImg(R.id.condImg, R.drawable.cond_205);
        } else if (condCode.equals("206")) {
            holder.setImg(R.id.condImg, R.drawable.cond_206);
        } else if (condCode.equals("207")) {
            holder.setImg(R.id.condImg, R.drawable.cond_207);
        } else if (condCode.equals("213")) {
            holder.setImg(R.id.condImg, R.drawable.cond_213);
        } else if (condCode.equals("300")) {
            holder.setImg(R.id.condImg, R.drawable.cond_300);
        } else if (condCode.equals("301")) {
            holder.setImg(R.id.condImg, R.drawable.cond_301);
        } else if (condCode.equals("302")) {
            holder.setImg(R.id.condImg, R.drawable.cond_302);
        } else if (condCode.equals("303")) {
            holder.setImg(R.id.condImg, R.drawable.cond_303);
        } else if (condCode.equals("304")) {
            holder.setImg(R.id.condImg, R.drawable.cond_304);
        } else if (condCode.equals("305")) {
            holder.setImg(R.id.condImg, R.drawable.cond_305);
        } else if (condCode.equals("306")) {
            holder.setImg(R.id.condImg, R.drawable.cond_306);
        } else if (condCode.equals("307")) {
            holder.setImg(R.id.condImg, R.drawable.cond_307);
        } else if (condCode.equals("309")) {
            holder.setImg(R.id.condImg, R.drawable.cond_309);
        } else if (condCode.equals("310")) {
            holder.setImg(R.id.condImg, R.drawable.cond_310);
        } else if (condCode.equals("311")) {
            holder.setImg(R.id.condImg, R.drawable.cond_311);
        } else if (condCode.equals("312")) {
            holder.setImg(R.id.condImg, R.drawable.cond_312);
        } else if (condCode.equals("313")) {
            holder.setImg(R.id.condImg, R.drawable.cond_313);
        } else if (condCode.equals("314")) {
            holder.setImg(R.id.condImg, R.drawable.cond_314);
        } else if (condCode.equals("315")) {
            holder.setImg(R.id.condImg, R.drawable.cond_315);
        } else if (condCode.equals("316")) {
            holder.setImg(R.id.condImg, R.drawable.cond_316);
        } else if (condCode.equals("317")) {
            holder.setImg(R.id.condImg, R.drawable.cond_317);
        } else if (condCode.equals("318")) {
            holder.setImg(R.id.condImg, R.drawable.cond_318);
        } else if (condCode.equals("399")) {
            holder.setImg(R.id.condImg, R.drawable.cond_399);
        } else if (condCode.equals("400")) {
            holder.setImg(R.id.condImg, R.drawable.cond_400);
        } else if (condCode.equals("402")) {
            holder.setImg(R.id.condImg, R.drawable.cond_402);
        } else if (condCode.equals("403")) {
            holder.setImg(R.id.condImg, R.drawable.cond_403);
        } else if (condCode.equals("404")) {
            holder.setImg(R.id.condImg, R.drawable.cond_404);
        } else if (condCode.equals("405")) {
            holder.setImg(R.id.condImg, R.drawable.cond_405);
        } else if (condCode.equals("406")) {
            holder.setImg(R.id.condImg, R.drawable.cond_406);
        } else if (condCode.equals("407")) {
            holder.setImg(R.id.condImg, R.drawable.cond_407);
        } else if (condCode.equals("407n")) {
            holder.setImg(R.id.condImg, R.drawable.cond_407n);
        } else if (condCode.equals("408")) {
            holder.setImg(R.id.condImg, R.drawable.cond_408);
        } else if (condCode.equals("409")) {
            holder.setImg(R.id.condImg, R.drawable.cond_409);
        } else if (condCode.equals("410")) {
            holder.setImg(R.id.condImg, R.drawable.cond_410);
        } else if (condCode.equals("499")) {
            holder.setImg(R.id.condImg, R.drawable.cond_499);
        } else if (condCode.equals("500")) {
            holder.setImg(R.id.condImg, R.drawable.cond_500);
        } else if (condCode.equals("502")) {
            holder.setImg(R.id.condImg, R.drawable.cond_502);
        } else if (condCode.equals("503")) {
            holder.setImg(R.id.condImg, R.drawable.cond_503);
        } else if (condCode.equals("504")) {
            holder.setImg(R.id.condImg, R.drawable.cond_504);
        } else if (condCode.equals("507")) {
            holder.setImg(R.id.condImg, R.drawable.cond_507);
        } else if (condCode.equals("508")) {
            holder.setImg(R.id.condImg, R.drawable.cond_508);
        } else if (condCode.equals("509")) {
            holder.setImg(R.id.condImg, R.drawable.cond_509);
        } else if (condCode.equals("511")) {
            holder.setImg(R.id.condImg, R.drawable.cond_511);
        } else if (condCode.equals("512")) {
            holder.setImg(R.id.condImg, R.drawable.cond_512);
        } else if (condCode.equals("513")) {
            holder.setImg(R.id.condImg, R.drawable.cond_513);
        } else if (condCode.equals("514")) {
            holder.setImg(R.id.condImg, R.drawable.cond_514);
        } else if (condCode.equals("515")) {
            holder.setImg(R.id.condImg, R.drawable.cond_515);
        } else if (condCode.equals("900")) {
            holder.setImg(R.id.condImg, R.drawable.cond_900);
        } else if (condCode.equals("901")) {
            holder.setImg(R.id.condImg, R.drawable.cond_901);
        } else if (condCode.equals("999")) {
            holder.setImg(R.id.condImg, R.drawable.cond_999);
        }

        holder.setText(R.id.tvTmpMax, lists.get(position+1).getTmp_max());
        holder.setText(R.id.tvTmpMin, lists.get(position+1).getTmp_min());
    }

    @Override
    public int getItemCount() {
        return lists.size()-1;
    }
}
