package com.fuicuiedu.xc.easyshop_20170413.user.login;

import android.util.Log;

import com.fuicuiedu.xc.easyshop_20170413.model.CachePreferences;
import com.fuicuiedu.xc.easyshop_20170413.model.User;
import com.fuicuiedu.xc.easyshop_20170413.model.UserResult;
import com.fuicuiedu.xc.easyshop_20170413.network.EasyShopClient;
import com.fuicuiedu.xc.easyshop_20170413.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    // TODO: 2017/4/19 0019 环信相关

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call !=null) call.cancel();
    }

    public void login(String username, String password){
        getView().showPrb();
        call = EasyShopClient.getInstance().login(username, password);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hidePrb();
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult userResult = new Gson().fromJson(body,UserResult.class);
                if (userResult.getCode() == 1){
                    //保存用户登录信息到本地配置
                    User user = userResult.getData();
                    Log.e("aaa","name = " + user.getName());
                    CachePreferences.setUser(user);
                    getView().loginSuccess();
                    getView().showMsg("登录成功");
                }else if (userResult.getCode() == 2){
                    getView().hidePrb();
                    getView().showMsg(userResult.getMessage());
                    getView().loginFailed();
                }else{
                    getView().hidePrb();
                    getView().showMsg("未知错误");
                }
            }
        });
    }
}
