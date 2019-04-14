package com.william_chow.contact.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.william_chow.contact.R;
import com.william_chow.contact.app.utils.Constant;
import com.william_chow.contact.app.utils.PrefUtils;
import com.william_chow.contact.app.utils.Utils;
import com.william_chow.contact.mvp.model.UserRepository;
import com.william_chow.contact.mvp.model.entity.Contact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
public class ContactInfoPresenter extends BasePresenter<UserRepository> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private Activity activity;

    public ContactInfoPresenter(AppComponent appComponent, Application _mApplication, Activity _activity) {
        super(appComponent.repositoryManager().createRepository(UserRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
        this.mApplication = _mApplication;
        this.activity = _activity;
    }

    public void setupTopBar(TextView tvLeft, TextView tvMiddleText, TextView tvRight) {
        tvLeft.setText(activity.getResources().getString(R.string.cancelLabel));
        tvRight.setText(activity.getResources().getString(R.string.saveLabel));
    }

    public void setupContactInfo(Contact contact, CircleImageView civContactInfo, EditText etFirstName, EditText etLastName, EditText etEmail, EditText etPhone) {
        Glide.with(activity).load("").apply(RequestOptions.bitmapTransform(new CircleCrop()).format(DecodeFormat.PREFER_ARGB_8888).placeholder(new ColorDrawable(ContextCompat.getColor(activity, R.color.orange)))).into(civContactInfo);
        if (null != contact.getFirstName() && contact.getFirstName().length() > 0) {
            etFirstName.setText(contact.getFirstName());
        } else {
            etFirstName.setText("");
        }
        if (null != contact.getLastName() && contact.getLastName().length() > 0) {
            etLastName.setText(contact.getLastName());
        } else {
            etLastName.setText("");
        }
        if (null != contact.getEmail() && contact.getEmail().length() > 0) {
            etEmail.setText(contact.getEmail());
        } else {
            etEmail.setText("");
        }
        if (null != contact.getPhone() && contact.getPhone().length() > 0) {
            etPhone.setText(contact.getPhone());
        } else {
            etPhone.setText("");
        }
    }

    public void inputChecking(Message msg, Contact contact, String etFirstName, String etLastName, String etEmail, String etPhone) {
        IView view = msg.getTarget();
        String error;
        if (etFirstName.length() == 0) {
            error = activity.getResources().getString(R.string.firstNameValidation);
            view.showMessage(error);
        } else if (etLastName.length() == 0) {
            error = activity.getResources().getString(R.string.lastNameValidation);
            view.showMessage(error);
        } else if(!Utils.isEmail_(etEmail)){
            error = activity.getResources().getString(R.string.emailValidation);
            view.showMessage(error);
        }else {
            updateJson(msg, contact, etFirstName, etLastName, etEmail, etPhone);
        }
    }

    private void updateJson(Message msg, Contact contact, String etFirstName, String etLastName, String etEmail, String etPhone) {
        IView view = msg.getTarget();
        ArrayList<Contact> myContact = PrefUtils.getContactArrayList(activity, Constant.CONTACT_KEY);
        if (null != myContact) {
            if (null != contact) {
                for (int i = 0; i < myContact.size(); i++) {
                    if(contact.getId().equalsIgnoreCase(myContact.get(i).getId())){
                        myContact.get(i).setFirstName(etFirstName);
                        myContact.get(i).setLastName(etLastName);
                        myContact.get(i).setEmail(etEmail);
                        myContact.get(i).setPhone(etPhone);
                    }
                }
                addUpdateSuccess(msg, myContact, activity.getResources().getString(R.string.updateSuccessMessage));
            } else {
                Contact newContact = new Contact();
                newContact.setId(Utils.getRandomString(25));
                newContact.setFirstName(etFirstName);
                newContact.setLastName(etLastName);
                newContact.setEmail(etEmail);
                newContact.setPhone(etPhone);
                myContact.add(newContact);

                addUpdateSuccess(msg, myContact, activity.getResources().getString(R.string.addSuccessMessage));
            }
        }
    }

    private void addUpdateSuccess(Message msg, ArrayList<Contact> myContact, String message){
        IView view = msg.getTarget();
        PrefUtils.getInstance(mApplication).removeContactArrayList();
        PrefUtils.saveContactArrayList(activity, myContact, Constant.CONTACT_KEY);
        view.showMessage(message);
        activity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}