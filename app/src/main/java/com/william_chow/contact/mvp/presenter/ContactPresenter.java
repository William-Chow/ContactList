/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.william_chow.contact.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.william_chow.contact.R;
import com.william_chow.contact.app.utils.Constant;
import com.william_chow.contact.app.utils.PrefUtils;
import com.william_chow.contact.app.utils.Utils;
import com.william_chow.contact.mvp.model.UserRepository;
import com.william_chow.contact.mvp.model.entity.Contact;
import com.william_chow.contact.mvp.ui.activity.ActContactInfo;
import com.william_chow.contact.mvp.ui.adapter.ContactAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.PermissionUtil;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

/**
 * ================================================
 * 展示 Presenter 的用法
 * <p>
 * Created by JessYan on 09/04/2016 10:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ContactPresenter extends BasePresenter<UserRepository> {
    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;
    private Application mApplication;
    private Activity activity;

    private ContactAdapter contactAdapter;

    public ContactPresenter(AppComponent appComponent, RxPermissions rxPermissions, Application _mApplication, Activity _activity) {
        super(appComponent.repositoryManager().createRepository(UserRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
        this.mRxPermissions = rxPermissions;
        this.mApplication = _mApplication;
        this.activity = _activity;
    }

    public void setupTopBar(ImageView ivLeftIcon, TextView tvMiddleText, ImageView ivRightIcon) {
        ivLeftIcon.setImageResource(android.R.drawable.ic_menu_search);
        tvMiddleText.setText(activity.getResources().getString(R.string.contactsTitle));
        ivRightIcon.setImageResource(android.R.drawable.ic_menu_add);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
    }

    public void requestContacts(final Message msg, RecyclerView rvContact, Boolean pullToRefresh) {
        IView view = msg.getTarget();
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                requestFromModel(msg, rvContact, pullToRefresh);
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                view.showMessage("Request permissions failure");
                view.hideLoading();//隐藏下拉刷新的进度条
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                view.showMessage("Need to go to the settings");
                view.hideLoading();//隐藏下拉刷新的进度条
            }
        }, mRxPermissions, mErrorHandler);
    }


    private void requestFromModel(Message msg, RecyclerView rvContact, Boolean pullToRefresh) {
        /*
          Usually is Retrofit
          - Temporary get data from assets --> data.json
         */
        ArrayList<Contact> contactArrayList = PrefUtils.getContactArrayList(activity, Constant.CONTACT_KEY);
        List<Contact> myContact;
        if (null == contactArrayList) {
            myContact = getContactData();
            PrefUtils.saveContactArrayList(activity, myContact, Constant.CONTACT_KEY);
        } else {
            myContact = contactArrayList;
        }

        if (myContact.size() > 0) {
            if (pullToRefresh) {
                if (null != contactAdapter) {
                    // Get the List from Assets File
                    msg.getTarget().hideLoading();
                    msg.recycle();
                    contactAdapter.clear();
                    myContact = getContactData();
                    contactAdapter.addContact(myContact);
                    contactAdapter.notifyDataSetChanged();
                }
            } else {
                rvContact.setHasFixedSize(true);
                rvContact.setItemAnimator(new DefaultItemAnimator());
                contactAdapter = new ContactAdapter(activity, myContact);
                contactAdapter.setOnClickListener((_position, contacts) -> Utils.returnBundleIntentWithoutFinish(activity, ActContactInfo.class, contacts.get(_position)));
                rvContact.setAdapter(contactAdapter);
            }
        }
    }

    private List<Contact> getContactData() {
        String jsonString = Utils.getAssetsJSON(activity, Constant.JSON_FILE);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Contact>>() {
        }.getType();
        return gson.fromJson(jsonString, listType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
