package com.example.addressproject;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    //以下跟选择地址有关
    private AddressPickerDialog mAddressPickerDialog;
    private List<AddressCountryBean> addressCountryBeans;
    private List<CountryHeadSection> headSections;
    private int[] i;
    private String villageId;
    private String[] letter = new String[]{
            "A", "B", "D", "E", "F", "G", "O", "P", "R", "S", "T"
    };

    private AddressSelectResultBean mAddressSelectResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        textView = findViewById(R.id.text_view);

        AddressModel model = new AddressModel();
        List<AddressCountryBean> addressCountryBeans = model.getCountryData(this);
        mAddressPickerDialog = new AddressPickerDialog();

//        areaPickerView = new AreaPickerView2(this, R.style.Dialog, addressCountryBeans);
//        areaPickerView.setAreaPickerViewCallback(new AreaPickerView2.AreaPickerViewCallback() {
//            @Override
//            public void callback(String address, int... value) {
//                i = value;
//                textView.setText(address);
//                villageId=addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).getValue();

//            }
//        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerAddressDialog();
            }
        });
    }


    private void showPickerAddressDialog() {
//        areaPickerView.setSelect(i);
//        areaPickerView.show();
        if(!mAddressPickerDialog.isAdded()) {
            mAddressPickerDialog.setResultBean(mAddressSelectResultBean);
            mAddressPickerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
            mAddressPickerDialog.show(getSupportFragmentManager(), "pickerAddress");
        }
//        AddressPickerDialogFragment addressPickerDialogFragment = new AddressPickerDialogFragment();
//        addressPickerDialogFragment.show(getSupportFragmentManager(),"pick");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddressResult(AddressSelectResultBean addressSelectResultBean) {

        if (null != addressSelectResultBean) {

            this.mAddressSelectResultBean = addressSelectResultBean;

            StringBuilder mAddress = new StringBuilder();
            mAddress.append(null != addressSelectResultBean.getAddressCountryBean() ? addressSelectResultBean.getAddressCountryBean().getLabel() : "");
            mAddress.append(null != addressSelectResultBean.getProvinceBean() ? addressSelectResultBean.getProvinceBean().getLabel() : "");
            mAddress.append(null != addressSelectResultBean.getCityBean() ? addressSelectResultBean.getCityBean().getLabel() : "");
            mAddress.append(null != addressSelectResultBean.getAreaBean() ? addressSelectResultBean.getAreaBean().getLabel() : "");
            textView.setText(mAddress);
        }

        if (null != mAddressPickerDialog) {
            mAddressPickerDialog.dismiss();
        }

    }


}
