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
package com.william_chow.contact.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.william_chow.contact.R;
import com.william_chow.contact.app.utils.Utils;
import com.william_chow.contact.mvp.presenter.ContactPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.base.DefaultAdapter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

/**
 * ================================================
 * 展示 View 的用法
 * <p>
 * Created by JessYan on 09/04/2016 10:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ActContact extends BaseActivity<ContactPresenter> implements IView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.ivLeftIcon)
    ImageView ivLeftIcon;

    @BindView(R.id.tvMiddleText)
    TextView tvMiddleText;

    @BindView(R.id.ivRightIcon)
    ImageView ivRightIcon;

    @BindView(R.id.rvContact)
    RecyclerView rvContact;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private RxPermissions mRxPermissions;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.act_contact;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (null != mPresenter) {
            mPresenter.setupTopBar(ivLeftIcon, tvMiddleText, ivRightIcon);
            initRecyclerView();
        }
    }

    @Override
    @Nullable
    public ContactPresenter obtainPresenter() {
        this.mRxPermissions = new RxPermissions(ActContact.this);
        return new ContactPresenter(ArtUtils.obtainAppComponentFromContext(ActContact.this), mRxPermissions, ActContact.this.getApplication(), ActContact.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mPresenter) {
            mPresenter.updateOnResume();
        }
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArtUtils.snackbarText(message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    @Optional
    @OnClick({R.id.rlLeftIcon, R.id.rlRightIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlLeftIcon:
                Toast.makeText(ActContact.this, ActContact.this.getResources().getString(R.string.searchLog), Toast.LENGTH_LONG).show();
                break;
            case R.id.rlRightIcon:
                Utils.returnBundleIntentWithoutFinish(ActContact.this, ActContactInfo.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.requestContacts(Message.obtain(this, new Object[]{true}), rvContact, true);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArtUtils.configRecyclerView(rvContact, new LinearLayoutManager(ActContact.this));
        mPresenter.requestContacts(Message.obtain(this, new Object[]{true}), rvContact, false);//打开app时自动加载列表
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(rvContact);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
