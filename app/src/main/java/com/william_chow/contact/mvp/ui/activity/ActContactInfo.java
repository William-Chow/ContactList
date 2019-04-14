package com.william_chow.contact.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.william_chow.contact.R;
import com.william_chow.contact.app.utils.Utils;
import com.william_chow.contact.mvp.model.entity.Contact;
import com.william_chow.contact.mvp.presenter.ContactInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 04/14/2019 14:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ActContactInfo extends BaseActivity<ContactInfoPresenter> implements IView {

    @BindView(R.id.tvLeft)
    TextView tvLeft;

    @BindView(R.id.tvMiddleText)
    TextView tvMiddleText;

    @BindView(R.id.tvRight)
    TextView tvRight;

    @BindView(R.id.civContactInfo)
    CircleImageView civContactInfo;

    @BindView(R.id.etFirstName)
    EditText etFirstName;

    @BindView(R.id.etLastName)
    EditText etLastName;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPhone)
    EditText etPhone;

    Contact contact = new Contact();

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.act_contact_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (null != mPresenter) {
            mPresenter.setupTopBar(tvLeft, tvMiddleText, tvRight);
            Bundle extras = getIntent().getExtras();
            if (null != extras) {
                contact = (Contact) extras.getSerializable("list");
            }

            if (null != contact) {
                mPresenter.setupContactInfo(contact, civContactInfo, etFirstName, etLastName, etEmail, etPhone);
            }
        }
    }

    @Override
    @Nullable
    public ContactInfoPresenter obtainPresenter() {
        return new ContactInfoPresenter(ArtUtils.obtainAppComponentFromContext(ActContactInfo.this), ActContactInfo.this.getApplication(), ActContactInfo.this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
    @OnClick({R.id.rlLeft, R.id.rlRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlLeft:
                finish();
                break;
            case R.id.rlRight:
                if(null != mPresenter){
                   Utils.hideKeyboard(ActContactInfo.this);
                   mPresenter.inputChecking(Message.obtain(this, new Object[]{true}), contact, etFirstName.getText().toString().trim(), etLastName.getText().toString().trim(), etEmail.getText().toString().trim(), etPhone.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }
}
